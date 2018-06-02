package com.zoovisitors.pl.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.cl.gps.ProviderBasedActivity;
import com.zoovisitors.pl.personalStories.PersonalPopUp;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int GET_TO_KNOW_ME_ANIMATION_DELTA_DP = 220;
    private MapView mapView;
    private MapResult mapData;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;
    private LinearLayout getToKnowMeLayout;
    private int getToKnowMeIndex = -1;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 10 * 1000;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1500;
    private int getToKnowMeAnimationDeltaPx;

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
        getToKnowMeAnimationDeltaPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                GET_TO_KNOW_ME_ANIMATION_DELTA_DP,
                getResources().getDisplayMetrics()
        );

        getToKnowMeLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp
                            .class);
                    intent.putExtra("animalId", getToKnowMeIndex);
                    startActivity(intent);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        });

        mapData = GlobalVariables.bl.getMapResult();
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

        mapView.SetInitialValues(GlobalVariables.bl.getMapResult().getMapBitmap(), enclosures,
                GlobalVariables.bl.getMiscs(), getIntent().getIntExtra("enclosureID", -1));

        mapDS.addAnimalStoriesToPoints(enclosures, GlobalVariables.bl.getPersonalStories());
    }

    private boolean needToShowIcon = true;
    private final AtomicBoolean movementInProgress = new AtomicBoolean(false);

    @Override
    public void onLocationChanged(android.location.Location location) {
        // TODO: Tell the user its accuracy is bad
        if (location.getAccuracy() <= MAX_ALLOWED_ACCURACY || GlobalVariables.DEBUG) {
            synchronized (movementInProgress) {
                if (movementInProgress.get())
                    return;
                movementInProgress.set(true);
            }

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
                if (isGetToKnowMeShown)
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
            changeGetToKnowMe(nextPersonalStory);
        } else {
            showGetToKnowMe(nextPersonalStory);
        }
    }

    private void showGetToKnowMe(Animal.PersonalStories animalStory) {
        getToKnowMeIndex = animalStory.getId();
        getToKnowMeTv.setText(animalStory.getName());
        getToKnowMeIb.setImageBitmap(animalStory.getPersonalPicture());

        int currentTop = ((RelativeLayout.LayoutParams) getToKnowMeLayout.getLayoutParams())
                .topMargin;
        int currentBottom = ((RelativeLayout.LayoutParams) getToKnowMeLayout.getLayoutParams())
                .bottomMargin;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)
                        getToKnowMeLayout.getLayoutParams());
                params.topMargin =
                        (int) (currentTop + getToKnowMeAnimationDeltaPx * interpolatedTime);
                params.bottomMargin =
                        (int) (currentBottom + getToKnowMeAnimationDeltaPx * interpolatedTime);
                getToKnowMeLayout.setLayoutParams(params);
            }
        };
        a.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        getToKnowMeLayout.startAnimation(a);
        isGetToKnowMeShown = true;
    }

    private void changeGetToKnowMe(Animal.PersonalStories animalStory) {
        hideGetToKnowMe();
        new Handler().postDelayed(() -> {
            showGetToKnowMe(animalStory);
        }, GET_TO_KNOW_ME_ANIMATION_TIME);
    }

    private void hideGetToKnowMe() {
        int currentTop = ((RelativeLayout.LayoutParams) getToKnowMeLayout.getLayoutParams())
                .topMargin;
        int currentBottom = ((RelativeLayout.LayoutParams) getToKnowMeLayout.getLayoutParams())
                .bottomMargin;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)
                        getToKnowMeLayout.getLayoutParams());
                params.topMargin =
                        (int) (currentTop - getToKnowMeAnimationDeltaPx * interpolatedTime);
                params.bottomMargin =
                        (int) (currentBottom - getToKnowMeAnimationDeltaPx * interpolatedTime);
                getToKnowMeLayout.setLayoutParams(params);
            }
        };
        a.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        getToKnowMeLayout.startAnimation(a);
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