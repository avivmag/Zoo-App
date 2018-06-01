package com.zoovisitors.pl.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.cl.gps.ProviderBasedActivity;
import com.zoovisitors.bl.Memory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mapView;
    private MapResult mapData;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;
    private TranslateAnimation translateAnimationYOut, translateAnimationYIn;
    private LinearLayout getToKnowMeLayout;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 3500; //10 * 1000 ;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1000;
    private final float GET_TO_KNOW_ME_ANIMATION_START_Y = -200f;
    private final float GET_TO_KNOW_ME_ANIMATION_END_Y = 200f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mapView = findViewById(R.id.map_view_layout);
        getToKnowMeLayout = findViewById(R.id.map_get_to_know_me_layout);
        getToKnowMeTv = findViewById(R.id.map_get_to_know_me_textview);
        getToKnowMeIb = findViewById(R.id.map_get_to_know_me_imagebutton);
        getToKnowMeIb.setBackgroundColor(Color.TRANSPARENT);
        initializeAnimations();

        mapData = GlobalVariables.bl.getMapResult();
//                // TODO: remove dummies
//                enclosures[0].setClosestPointX(425);
//                enclosures[0].setClosestPointY(356);
////                enclosures[0].setClosestPointX(164);
////                enclosures[0].setClosestPointY(601);
//                enclosures[1].setClosestPointX(164);
//                enclosures[1].setClosestPointY(601);
////                enclosures[2].setClosestPointX(1210);
////                enclosures[2].setClosestPointY(935);

        mapDS = new DataStructure(mapData.getMapInfo().getPoints(),
                new Location(mapData.getMapInfo().getZooLocationLatitude(), mapData.getMapInfo()
                        .getZooLocationLongitude()),
                new Point(mapData.getMapInfo().getZooPointX(), mapData.getMapInfo().getZooPointY()),
                mapData.getMapInfo().getxLongitudeRatio(),
                mapData.getMapInfo().getyLatitudeRatio(),
                mapData.getMapInfo().getSinAlpha(),
                mapData.getMapInfo().getCosAlpha(),
                mapData.getMapInfo().getMinLatitude(),
                mapData.getMapInfo().getMaxLatitude(),
                mapData.getMapInfo().getMinLongitude(),
                mapData.getMapInfo().getMaxLongitude()
        );
        Enclosure[] enclosures = GlobalVariables.bl.getEnclosures();
        mapView.SetInitialValues(GlobalVariables.bl.getMapResult().getMapBitmap());
        for(int i = 0; i<enclosures.length; i++)
            if(enclosures[i].getMarkerBitmap() != null) {
                mapView.addEnclosure(enclosures[i], i);
            }
        for (Misc misc :
                GlobalVariables.bl.getMiscs()) {
            if(misc.getMarkerBitmap() != null) {
                mapView.addMiscIcon(misc);
            }
        }
        mapDS.addAnimalStoriesToPoints(enclosures, GlobalVariables.bl.getPersonalStories());
    }

    private void initializeAnimations() {
        translateAnimationYIn = new TranslateAnimation(0, 0, GET_TO_KNOW_ME_ANIMATION_START_Y,
                GET_TO_KNOW_ME_ANIMATION_END_Y);
        translateAnimationYIn.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        translateAnimationYIn.setFillAfter(true);

        translateAnimationYOut = new TranslateAnimation(0, 0, GET_TO_KNOW_ME_ANIMATION_END_Y,
                GET_TO_KNOW_ME_ANIMATION_START_Y);
        translateAnimationYOut.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        translateAnimationYOut.setFillAfter(true);
    }

    private boolean needToShowIcon = true;
    private AtomicBoolean movementInProgress = new AtomicBoolean(false);

    @Override
    public void onLocationChanged(android.location.Location location) {
//        // TODO: remove this
//        location.setLatitude(31.2591);
//        location.setLongitude(34.7438);
        if (location.getAccuracy() <= MAX_ALLOWED_ACCURACY || GlobalVariables.DEBUG) {
            synchronized (movementInProgress) {
                if (movementInProgress.get())
                    return;
                movementInProgress.set(true);
            }
            //Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG).show();

            if (location.getLatitude() < mapData.getMapInfo().getMinLatitude() - MAX_MARGIN ||
                    location.getLatitude() > mapData.getMapInfo().getMaxLatitude() + MAX_MARGIN ||
                    location.getLongitude() < mapData.getMapInfo().getMinLongitude() - MAX_MARGIN ||
                    location.getLongitude() > mapData.getMapInfo().getMaxLongitude() + MAX_MARGIN) {
                movementInProgress.set(false);
                return;
            }

            Point calibratedPointAndClosestPointFromPoints;
            Point p = mapDS.locationToPoint(new Location(location.getLatitude(), location
                    .getLongitude()));
            calibratedPointAndClosestPointFromPoints = mapDS
                    .getOnMapPosition(p);
            if (calibratedPointAndClosestPointFromPoints == null) {
                movementInProgress.set(false);
                return;
            }
//            // TODO: remove this
//            calibratedPointAndClosestPointFromPoints = new Point(164,601);

            updateVisitorPosition(calibratedPointAndClosestPointFromPoints);
            updateGetToKnowMe();

            movementInProgress.set(false);
        }
    }

    private void updateVisitorPosition(Point calibratedPointAndClosestPointFromPoints) {
        mapView.UpdateVisitorLocation(calibratedPointAndClosestPointFromPoints.getX(),
                calibratedPointAndClosestPointFromPoints.getY());
        if (needToShowIcon) {
            needToShowIcon = false;
            mapView.ShowVisitorIcon();
        }
    }

    private long lastTimeUpdatedGetToKnowMe = 0;
    private Animal.PersonalStories lastPersonalStoryShowed = null;

    private boolean isGetToKnowMeShown = false;
    private void updateGetToKnowMe() {
        if (System.currentTimeMillis() - lastTimeUpdatedGetToKnowMe >
                MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES) {
            Set<Animal.PersonalStories> animalPersonalStories = mapDS.getCloseAnimalStories();
            if (animalPersonalStories.isEmpty()) {
                if(isGetToKnowMeShown)
                    hideGetToKnowMe();
                return;
            }
            Animal.PersonalStories nextPersonalStory =
                    animalPersonalStories.toArray(
                            new Animal.PersonalStories[animalPersonalStories.size()])
                            [(int) (Math.random() * animalPersonalStories.size())];
            if (nextPersonalStory != lastPersonalStoryShowed) {
                updateGetToKnowMe(nextPersonalStory);
                lastPersonalStoryShowed = nextPersonalStory;
            }
            lastTimeUpdatedGetToKnowMe = System.currentTimeMillis();
        }
    }

    private void updateGetToKnowMe(Animal.PersonalStories nextPersonalStory) {
        if (isGetToKnowMeShown) {
            changeGetToKnowMe(nextPersonalStory.getName());
        } else {
            showGetToKnowMe(nextPersonalStory.getName());
        }

//        getToKnowMeIb.setImageDrawable(nextPersonalStory.getDrawable());
    }

    private void showGetToKnowMe(String text) {
        getToKnowMeTv.setText(text);
        getToKnowMeLayout.startAnimation(translateAnimationYIn);
        isGetToKnowMeShown = true;
    }

    private void changeGetToKnowMe(String text) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getToKnowMeLayout.clearAnimation();
                showGetToKnowMe(text);
            }
        }, translateAnimationYOut.getDuration());
        getToKnowMeLayout.startAnimation(translateAnimationYOut);
    }

    private void hideGetToKnowMe() {
        getToKnowMeLayout.startAnimation(translateAnimationYOut);
        isGetToKnowMeShown = false;
    }

    @Override
    public void onProviderEnabled() {
        needToShowIcon = true;
    }

    @Override
    public void onProviderDisabled() {
        mapView.HideVisitorIcon();
    }

    @Override
    public int getMinTime() {
        return 0;
    }

    @Override
    public int getMinDistance() {
        return 0;
    }
}