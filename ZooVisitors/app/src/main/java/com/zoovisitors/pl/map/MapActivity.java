package com.zoovisitors.pl.map;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.cl.gps.ProviderBasedActivity;
import com.zoovisitors.pl.personalStories.PersonalPopUp;

import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int GET_TO_KNOW_ME_ANIMATION_DELTA_DP = 230;
    public static final long OPEN_DOORS_ANIMATION_DURATION = 750;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 10 * 1000;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1500;
    private double DEMO_MOVEMENT = 0.00005;
    private enum ExplorationState {ON, OFF};

    private DataStructure mapDS;
    private Boolean navigatingOutOfActivity = false;
    private Location demoLocation;
    private Runnable demoExplorationRunnable;
    private Thread demoExplorationThread;
    private MapView mapView;
    private MapResult mapData;
    private RelativeLayout mapActivityLayout;
    private RelativeLayout getToKnowMeLayout;
    private int initialGetToKnowMeLayoutTop;
    private int initialGetToKnowMeLayoutBottom;
    private int getToKnowMeIndex = -1;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private int getToKnowMeAnimationDeltaPx;
    private final AtomicBoolean onLocationInProgress = new AtomicBoolean(false);
    private enum GpsState {Off, On, Focused};
    private GpsState gpsState = GpsState.Off;
    private ImageButton gpsButton;
    private ImageView logo;
    private ImageView leftDoor;
    private ImageView rightDoor;
    private long lastTimeUpdatedGetToKnowMe = 0;
    private Animal.PersonalStories lastPersonalStoryShowed = null;
    private boolean isGetToKnowMeShown = false;
    private boolean clickedGetToKnowMe = false;
    private ExplorationState explorationState = ExplorationState.OFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initiateVariables();

        Pair<Integer, Enclosure>[] enclosures = GlobalVariables.bl.getEnclosuresForMap();
        initiateDataStructure(enclosures);
        initiateMapView(enclosures);
        if(GlobalVariables.DEBUG) {
            initiateDemo();
        }
    }

    /**
     * Initiate the class variables
     */
    private void initiateVariables() {
        getSupportActionBar().hide();
        mapActivityLayout = findViewById(R.id.activity_map_layout);
        mapView = findViewById(R.id.map_view_layout);
        getToKnowMeLayout = findViewById(R.id.map_get_to_know_me_layout);
        getToKnowMeTv = findViewById(R.id.map_get_to_know_me_textview);
        getToKnowMeIb = findViewById(R.id.map_get_to_know_me_imagebutton);
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

        logo = findViewById(R.id.map_logo);
        leftDoor = findViewById(R.id.map_left_door);
        rightDoor = findViewById(R.id.map_right_door);
        mapData = GlobalVariables.bl.getMapResult();
    }

    /**
     * Initiate map datastructure
     * @param enclosures
     */
    private void initiateDataStructure(Pair<Integer, Enclosure>[] enclosures) {
        mapDS = new DataStructure(mapData.getMapInfo().getPoints(),
                mapData.getMapInfo().getRoutesMap(),
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
                mapData.getMapInfo().getMaxLongitude(),
                enclosures,
                GlobalVariables.bl.getPersonalStories()
        );
    }

    /**
     * Initiates the map view.
     * @param enclosures
     */
    private void initiateMapView(Pair<Integer, Enclosure>[] enclosures) {
        // Note: be aware that cancelFocus should be called only when touching the map view and
        // not other views in activity_map
        mapView.initiateVariables(GlobalVariables.bl.getMapResult().getMapBitmap(), enclosures,
                GlobalVariables.bl.getMiscs(),
                getIntent().getIntExtra("enclosureID", -1),
                // called when someone touches the map
                () -> { cancelFocus(); },
                // called when someone clicks on enclosure
                () -> {
                    synchronized (navigatingOutOfActivity) {
                        if(navigatingOutOfActivity)
                            return;
                        navigatingOutOfActivity = true;
                    }
                    cancelFocus();
                    moveDoors(false);
                },
                2*OPEN_DOORS_ANIMATION_DURATION);
    }

    /**
     * Initiates demo
     */
    private void initiateDemo() {
        findViewById(R.id.demo_layout).setVisibility(View.VISIBLE);
        demoLocation = new Location(mapData.getMapInfo().getZooLocationLatitude(), mapData.getMapInfo().getZooLocationLongitude());
        onLocationChangedDemo(demoLocation);

        demoExplorationRunnable =  () -> {
            final double[] lastDemoLocationLatitudeChange = {(Math.random() * 2 - 1) * DEMO_MOVEMENT};
            final double[] lastDemoLocationLongitudeChange = {(Math.random() * 2 - 1) * DEMO_MOVEMENT};
            while (explorationState == ExplorationState.ON) {
                Location nextDemoLocation = new Location (
                        demoLocation.getLatitude() + ((Math.random() * 2 - 1) * DEMO_MOVEMENT + lastDemoLocationLatitudeChange[0]) / 2,
                        demoLocation.getLongitude() + ((Math.random() * 2 - 1) * DEMO_MOVEMENT + lastDemoLocationLongitudeChange[0]) / 2);
                runOnUiThread(() -> {
                    if(onLocationChangedDemo(nextDemoLocation)) {
                        lastDemoLocationLatitudeChange[0] = nextDemoLocation.getLatitude() - demoLocation.getLatitude();
                        lastDemoLocationLongitudeChange[0] = nextDemoLocation.getLongitude() - demoLocation.getLongitude();
                        demoLocation = nextDemoLocation;
                    }
                });
                try { Thread.sleep(300); } catch (InterruptedException e) { }
            }
            demoExplorationThread = null;
        };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatingOutOfActivity = false;

        if(!clickedGetToKnowMe) {
            moveDoors(true);
        }
        clickedGetToKnowMe = false;
    }

    @Override
    public void onBackPressed() {
        synchronized (navigatingOutOfActivity) {
            if(navigatingOutOfActivity)
                return;
            navigatingOutOfActivity = true;
        }
        cancelFocus();
        moveDoors(false);
        mapView.InitiateExitMapAnimation();
        new Handler().postDelayed(super::onBackPressed, OPEN_DOORS_ANIMATION_DURATION * 2);
    }

    /**
     * Called when a new location is determined.
     * @param location
     */
    @Override
    public void onLocationChanged(android.location.Location location) {
        if (!GlobalVariables.DEBUG && location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
            synchronized (onLocationInProgress) {
                if (onLocationInProgress.get())
                    return;
                onLocationInProgress.set(true);
            }

            Location backendLocation = new Location(location.getLatitude(), location
                    .getLongitude());
            if (!mapDS.IsInPark(backendLocation)) {
                onLocationInProgress.set(false);
                return;
            }

            Point pointOnRoad = mapDS.getOnMapPosition(backendLocation);
            if (pointOnRoad == null) {
                onLocationInProgress.set(false);
                return;
            }

            updateVisitorPosition(pointOnRoad);
            refreshGetToKnowMe();

            onLocationInProgress.set(false);
        } else {
            Toast.makeText(this, R.string.map_bad_gps_signal, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns true if the given location was converted to point and can be shown on the map routes
     */
    private boolean onLocationChangedDemo(Location location) {
        Point pointOnRoad = mapDS.getOnMapPosition(
                new Location(location.getLatitude(), location.getLongitude()));
        if (pointOnRoad == null) {
            return false;
        }

        updateVisitorPosition(pointOnRoad);
        refreshGetToKnowMe();
        return true;
    }

    private void updateVisitorPosition(Point calibratedPointAndClosestPointFromPoints) {
        mapView.UpdateVisitorLocation(calibratedPointAndClosestPointFromPoints.getX(),
                calibratedPointAndClosestPointFromPoints.getY(),
                gpsState == GpsState.Focused);
    }

    private void refreshGetToKnowMe() {
        if (System.currentTimeMillis() - lastTimeUpdatedGetToKnowMe >
                MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES) {
            Animal.PersonalStories animalStory = mapDS.getNextAnimalStory();
            if (animalStory == null) {
                if (isGetToKnowMeShown)
                    hideGetToKnowMe();
                return;
            }
            if (animalStory != lastPersonalStoryShowed) {
                updateGetToKnowMe(animalStory);
                lastPersonalStoryShowed = animalStory;
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

    public void getToKnowMeClose(View view) {
        mapDS.removeAnimalStory(lastPersonalStoryShowed);
        hideGetToKnowMe();
    }

    public void getToKnowMeClick(View view) {
        clickedGetToKnowMe = true;
        Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp
                .class);
        intent.putExtra("animalId", getToKnowMeIndex);
        startActivity(intent);
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
                mapView.setAnimationInterrupted(true);
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

    /**
     * Move the doors for when entering/exiting the map activity.
     * @param openDoors True if the doors should open, false otherwise.
     */
    private void moveDoors(boolean openDoors) {
        LinearLayout.LayoutParams leftDoorParams = (LinearLayout.LayoutParams) leftDoor.getLayoutParams();
        LinearLayout.LayoutParams rightDoorParams = (LinearLayout.LayoutParams) rightDoor.getLayoutParams();

        Animation animationLogo = getLogoAnimation(openDoors);
        Animation animationDoors = getDoorsAnimation(openDoors, leftDoorParams, rightDoorParams,
                getResources().getDisplayMetrics().widthPixels / 2);
        startMoveDoorsAnimations(openDoors, animationLogo, animationDoors);
    }

    @NonNull
    private Animation getLogoAnimation(boolean openDoors) {
        Animation animationLogo = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                logo.setAlpha((openDoors ? (1 - interpolatedTime) : interpolatedTime));
            }
        };
        animationLogo.setDuration(OPEN_DOORS_ANIMATION_DURATION);
        return animationLogo;
    }

    @NonNull
    private Animation getDoorsAnimation(boolean openDoors, LinearLayout.LayoutParams
            leftDoorParams, LinearLayout.LayoutParams rightDoorParams, int halfScreenWidth) {
        Animation animationDoors = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                leftDoorParams.rightMargin = (int) ((openDoors ? interpolatedTime : (1 - interpolatedTime)) * halfScreenWidth);
                leftDoorParams.leftMargin = (int) ((openDoors ? -interpolatedTime : (interpolatedTime - 1)) * halfScreenWidth);
                leftDoor.setLayoutParams(leftDoorParams);
                rightDoorParams.rightMargin = (int) ((openDoors ? -interpolatedTime : (interpolatedTime - 1)) * halfScreenWidth);
                rightDoorParams.leftMargin = (int) ((openDoors ? interpolatedTime : (1 - interpolatedTime)) * halfScreenWidth);
                rightDoor.setLayoutParams(rightDoorParams);
            }
        };
        animationDoors.setDuration(OPEN_DOORS_ANIMATION_DURATION);
        return animationDoors;
    }

    private void startMoveDoorsAnimations(boolean openDoors, Animation animationLogo,
                                          Animation animationDoors) {
        mapActivityLayout.startAnimation(openDoors ? animationLogo : animationDoors);
        new Handler().postDelayed(() -> {
            mapActivityLayout.startAnimation(openDoors ? animationDoors : animationLogo);
            logo.setVisibility(openDoors ? View.INVISIBLE : View.VISIBLE);
        }, OPEN_DOORS_ANIMATION_DURATION);
    }

    public void onDemoClick(View view) {
        double demoLocationLatitude = demoLocation.getLatitude();
        double demoLocationLongitude = demoLocation.getLongitude();
        if(view.getId() != R.id.demo_explore_button || explorationState != ExplorationState.OFF) {
            explorationModeOff();
        }
        switch (view.getId()) {
            case R.id.demo_left_button:
                demoLocationLatitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                demoLocationLongitude -= DEMO_MOVEMENT;
                break;
            case R.id.demo_top_button:
                demoLocationLatitude += DEMO_MOVEMENT;
                demoLocationLongitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                break;
            case R.id.demo_right_button:
                demoLocationLatitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                demoLocationLongitude += DEMO_MOVEMENT;
                break;
            case R.id.demo_bottom_button:
                demoLocationLatitude -= DEMO_MOVEMENT;
                demoLocationLongitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                break;
            case R.id.demo_explore_button:
                if(explorationState == ExplorationState.OFF) {
                    explorationModeOn();
                }
                break;
        }
        Location nextDemoLocation = new Location(demoLocationLatitude, demoLocationLongitude);
        if(onLocationChangedDemo(nextDemoLocation)) {
            demoLocation = nextDemoLocation;
        }
    }

    private void explorationModeOn() {
        if(demoExplorationThread == null || !demoExplorationThread.isAlive()) {
            explorationState = ExplorationState.ON;
            demoExplorationThread = new Thread(demoExplorationRunnable);
            demoExplorationThread.start();
            ((ImageButton) findViewById(R.id.demo_explore_button)).setImageResource(R.mipmap.round_explore_black_24);
        }
    }

    private void explorationModeOff() {
        explorationState = ExplorationState.OFF;
        ((ImageButton) findViewById(R.id.demo_explore_button)).setImageResource(R.mipmap.round_explore_off_black_24);
    }
}