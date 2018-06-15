package com.zoovisitors.pl.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final boolean IS_IN_DEMO_MODE = false;
    private Location demoLocation;
    private Runnable demoExplorationRunnable;
    private Thread demoExplorationThread;
    public static final int GET_TO_KNOW_ME_ANIMATION_DELTA_DP = 230;
    private MapView mapView;
    private MapResult mapData;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;
    private RelativeLayout mapActivityLayout;
    private RelativeLayout getToKnowMeLayout;
    private int initialGetToKnowMeLayoutTop;
    private int initialGetToKnowMeLayoutBottom;
    private int getToKnowMeIndex = -1;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 10 * 1000;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1500;
    private int getToKnowMeAnimationDeltaPx;
    private final AtomicBoolean movementInProgress = new AtomicBoolean(false);
    private enum GpsState {Off, On, Focused};
    private GpsState gpsState = GpsState.Off;
    private ImageButton gpsButton;
    private ImageView logo;
    private ImageView leftDoor;
    private ImageView rightDoor;
    private android.support.v7.app.ActionBar actionBar;
    public static final long OPEN_DOORS_ANIMATION_DURATION = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        actionBar = getSupportActionBar();
        actionBar.hide();

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

        Pair<Integer, Enclosure>[] enclosures = GlobalVariables.bl.getEnclosuresForMap();
        // Note: be aware that cancelFocus should be called only when touching the map view and
        // not other views in activity_map
        mapView.SetInitialValues(GlobalVariables.bl.getMapResult().getMapBitmap(), enclosures,
                GlobalVariables.bl.getMiscs(),
                getIntent().getIntExtra("enclosureID", -1),
                // called when someone touches the map
                () -> { cancelFocus(); },
                // called when someone clicks on enclosure
                () -> {
                    cancelFocus();
                    moveDoors(false);
                },
                2*OPEN_DOORS_ANIMATION_DURATION);

        mapDS.addAnimalStoriesToPoints(enclosures, GlobalVariables.bl.getPersonalStories());

        if(IS_IN_DEMO_MODE) {
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private boolean clickedGetToKnowMe = false;
    @Override
    protected void onResume() {
        super.onResume();

        if(!clickedGetToKnowMe) {
            moveDoors(true);
        }
        clickedGetToKnowMe = false;
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (!IS_IN_DEMO_MODE && location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
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

            Point p = mapDS.locationToPoint(new Location(location.getLatitude(), location
                    .getLongitude()));
            Point calibratedPointAndClosestPointFromPoints = mapDS.getOnMapPosition(p);
            if (calibratedPointAndClosestPointFromPoints == null) {
                movementInProgress.set(false);
                return;
            }

            updateVisitorPosition(calibratedPointAndClosestPointFromPoints);
            updateGetToKnowMe();

            movementInProgress.set(false);
        } else {
            // TODO: Think of a better way to show this or at least support all 4 languages
            Toast.makeText(this, "Bad gps signal", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param location
     * @return true if the given location was converted to point and shown on the map
     */
    private boolean onLocationChangedDemo(Location location) {
        Point p = mapDS.locationToPoint(new Location(location.getLatitude(), location
                .getLongitude()));
        Point calibratedPointAndClosestPointFromPoints = mapDS.getOnMapPosition(p);
        if (calibratedPointAndClosestPointFromPoints == null) {
            return false;
        }

        updateVisitorPosition(calibratedPointAndClosestPointFromPoints);
        updateGetToKnowMe();
        return true;
    }

    private void updateVisitorPosition(Point calibratedPointAndClosestPointFromPoints) {
        mapView.UpdateVisitorLocation(calibratedPointAndClosestPointFromPoints.getX(),
                calibratedPointAndClosestPointFromPoints.getY(),
                gpsState == GpsState.Focused);
    }

    private long lastTimeUpdatedGetToKnowMe = 0;
    private Animal.PersonalStories lastPersonalStoryShowed = null;

    private boolean isGetToKnowMeShown = false;
    private Set<Animal.PersonalStories> unwantedStories = new HashSet<>();
    private void updateGetToKnowMe() {
        if (System.currentTimeMillis() - lastTimeUpdatedGetToKnowMe >
                MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES) {
            Set<Animal.PersonalStories> animalPersonalStories = mapDS.getCloseAnimalStories();
            animalPersonalStories.removeAll(unwantedStories);
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

    public void getToKnowMeClick(View view) {
        clickedGetToKnowMe = true;
        Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp
                .class);
        intent.putExtra("animalId", getToKnowMeIndex);
        startActivity(intent);
    }

    public void getToKnowMeClose(View view) {
        unwantedStories.add(lastPersonalStoryShowed);
        hideGetToKnowMe();
    }

    @Override
    public void onBackPressed() {
        cancelFocus();
        moveDoors(false);
        mapView.exitMap();
        new Handler().postDelayed(super::onBackPressed, OPEN_DOORS_ANIMATION_DURATION * 2);
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

    private void moveDoors(boolean outSide) {
        LinearLayout.LayoutParams leftDoorParams = (LinearLayout.LayoutParams) leftDoor.getLayoutParams();
        LinearLayout.LayoutParams rightDoorParams = (LinearLayout.LayoutParams) rightDoor.getLayoutParams();

        int halfScreenWidth = getResources().getDisplayMetrics().widthPixels / 2;
        Animation animationLogo = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                logo.setAlpha((outSide ? (1 - interpolatedTime) : interpolatedTime));
            }
        };
        Animation animationDoors = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                leftDoorParams.rightMargin = (int) ((outSide ? interpolatedTime : (1 - interpolatedTime)) * halfScreenWidth);
                leftDoorParams.leftMargin = (int) ((outSide ? -interpolatedTime : (interpolatedTime - 1)) * halfScreenWidth);
                leftDoor.setLayoutParams(leftDoorParams);
                rightDoorParams.rightMargin = (int) ((outSide ? -interpolatedTime : (interpolatedTime - 1)) * halfScreenWidth);
                rightDoorParams.leftMargin = (int) ((outSide ? interpolatedTime : (1 - interpolatedTime)) * halfScreenWidth);
                rightDoor.setLayoutParams(rightDoorParams);
            }
        };
        animationDoors.setDuration(OPEN_DOORS_ANIMATION_DURATION);
        animationLogo.setDuration(OPEN_DOORS_ANIMATION_DURATION);
        mapActivityLayout.startAnimation(outSide ? animationLogo : animationDoors);
        new Handler().postDelayed(() -> {
            mapActivityLayout.startAnimation(outSide ? animationDoors : animationLogo);
            logo.setVisibility(outSide ? View.INVISIBLE : View.VISIBLE);
        }, OPEN_DOORS_ANIMATION_DURATION);
    }

    private double DEMO_MOVEMENT = 0.00007;
    private enum ExplorationState {ON, OFF};
    private ExplorationState explorationState = ExplorationState.OFF;
    public void onDemoClick(View view) {
        double demoLocationLatitude = demoLocation.getLatitude();
        double demoLocationLongitude = demoLocation.getLongitude();
        switch (view.getId()) {
            case R.id.demo_left_button:
                explorationModeOff();
                demoLocationLatitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                demoLocationLongitude -= DEMO_MOVEMENT;
                break;
            case R.id.demo_top_button:
                explorationModeOff();
                demoLocationLatitude += DEMO_MOVEMENT;
                demoLocationLongitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                break;
            case R.id.demo_right_button:
                explorationModeOff();
                demoLocationLatitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                demoLocationLongitude += DEMO_MOVEMENT;
                break;
            case R.id.demo_bottom_button:
                explorationModeOff();
                demoLocationLatitude -= DEMO_MOVEMENT;
                demoLocationLongitude += (Math.random() * 2 - 1) * DEMO_MOVEMENT;
                break;
            case R.id.demo_explore_button:
                if(explorationState == ExplorationState.OFF) {
                    explorationModeOn();
                } else {
                    explorationModeOff();
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