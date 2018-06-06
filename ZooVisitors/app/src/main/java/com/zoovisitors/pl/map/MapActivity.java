package com.zoovisitors.pl.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
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
import com.zoovisitors.backend.callbacks.GetObjectInterface;
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
    private int initialGetToKnowMeLayoutTop;
    private int initialGetToKnowMeLayoutBottom;
    private int getToKnowMeIndex = -1;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 10 * 1000;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1500;
    private int getToKnowMeAnimationDeltaPx;
    private Enclosure[] enclosures;
    private boolean firstRun = true;
    //    private boolean needFirstAnimation;
    private final AtomicBoolean movementInProgress = new AtomicBoolean(false);

    private enum GpsState {Off, On, Focused}

    ;
    private GpsState gpsState = GpsState.Off;
    private ImageButton gpsButton;

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
        gpsButton = findViewById(R.id.gps_button);

        initialGetToKnowMeLayoutTop = ((RelativeLayout.LayoutParams) getToKnowMeLayout
                .getLayoutParams()).topMargin;
        initialGetToKnowMeLayoutBottom = ((RelativeLayout.LayoutParams) getToKnowMeLayout
                .getLayoutParams()).bottomMargin;

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
        GlobalVariables.bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosures = (Enclosure[]) response;
            }

            @Override
            public void onFailure(Object response) {

            }
        });

        // Note: be aware that cancelFocus should be called only when touching the map view and
        // not other views in activity_map
        mapView.SetInitialValues(GlobalVariables.bl.getMapResult().getMapBitmap(), enclosures,
                GlobalVariables.bl.getMiscs(), getIntent().getIntExtra("enclosureID", -1), () -> {
                    cancelFocus();
                });

        mapDS.addAnimalStoriesToPoints(enclosures, GlobalVariables.bl.getPersonalStories());
    }

    @Override
    protected void onResume() {
        super.onResume();

        int encId = getIntent().getIntExtra("enclosureID", -1);
        // on the first run the map is not ready, so we need to run it in other place
        if (!firstRun && encId != -1) {
            mapView.focusOnIconAndRattle(encId);
        }
        getIntent().putExtra("enclosureID", -1);
        firstRun = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        // TODO: Tell the user its accuracy is bad
        //TODO: delete this
        if (true || location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
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
                calibratedPointAndClosestPointFromPoints.getY(),
                gpsState == GpsState.Focused);
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

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)
                        getToKnowMeLayout.getLayoutParams());
                params.topMargin =
                        (int) (initialGetToKnowMeLayoutTop + getToKnowMeAnimationDeltaPx * interpolatedTime);
                params.bottomMargin =
                        (int) (initialGetToKnowMeLayoutBottom + getToKnowMeAnimationDeltaPx * interpolatedTime);
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
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)
                        getToKnowMeLayout.getLayoutParams());
                params.topMargin =
                        (int) (initialGetToKnowMeLayoutTop + getToKnowMeAnimationDeltaPx * (1 - interpolatedTime));
                params.bottomMargin =
                        (int) (initialGetToKnowMeLayoutBottom + getToKnowMeAnimationDeltaPx * (1 - interpolatedTime));
                getToKnowMeLayout.setLayoutParams(params);
            }
        };
        a.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        getToKnowMeLayout.startAnimation(a);
        isGetToKnowMeShown = false;
    }

    @Override
    public void onBackPressed() {
        cancelFocus();
        moveTaskToBack(false);
    }

    @Override
    public int getMinTime() {
        return 0;
    }

    @Override
    public int getMinDistance() {
        return 0;
    }

    @Override
    public void onProviderEnabled() {
        gpsState = GpsState.On;
        gpsButton.setImageDrawable(getResources().getDrawable(R.mipmap
                .round_gps_not_fixed_black_24));
    }

    @Override
    public void onProviderDisabled() {
        gpsState = GpsState.Off;
        mapView.HideVisitorIcon();
        gpsButton.setImageDrawable(getResources().getDrawable(R.mipmap.round_gps_off_black_24));
    }

    public void onGpsButtonClick(View view) {
        switch (gpsState) {
            case Off:
                startGps();
                break;
            case On:
                gpsState = GpsState.Focused;
                gpsButton.setImageDrawable(getResources().getDrawable(R.mipmap
                        .round_gps_fixed_black_24));
                break;
            case Focused:
                mapView.animationInterrupt = true;
                cancelFocus();
                break;
        }
    }

    private void cancelFocus() {
        if (gpsState == GpsState.Focused) {
            gpsState = GpsState.On;
            gpsButton.setImageDrawable(getResources().getDrawable(R.mipmap
                    .round_gps_not_fixed_black_24));
        }
    }
}