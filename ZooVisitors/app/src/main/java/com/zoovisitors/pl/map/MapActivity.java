package com.zoovisitors.pl.map;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.cl.gps.ProviderBasedActivity;
import com.zoovisitors.dal.Memory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ConstraintLayout activityMapLayout;
    private MapView mapView;
    private BusinessLayer bl;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;
    private Enclosure[] enclosures;
    private Animal.PersonalStories[] animalStories;
    private TranslateAnimation translateAnimationYOut, translateAnimationYIn,
        translateAnimationYOutIn;
//    private ConstraintSet constraintSetIn, constraintSetOut;
    private LinearLayout getToKnowMeLayout;
    private TextView getToKnowMeTv;
    private String textViewTextToSet;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 4 * 1000; //10 * 1000 ;
    private final long GET_TO_KNOW_ME_ANIMATION_TIME = 1000;
    private final float GET_TO_KNOW_ME_ANIMATION_START_Y = -400f;
    private final float GET_TO_KNOW_ME_ANIMATION_END_Y = 400f;
//    private final int GET_TO_KNOW_ME_ANIMATION_TRANSLATE = 100;
//    private AutoTransition autoTransition;

//    private ConstraintSet normal = new ConstraintSet();
//    private ConstraintSet movedGTKM = new ConstraintSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mapView = findViewById(R.id.map_view_layout);
        getToKnowMeLayout = findViewById(R.id.map_get_to_know_me_layout);
//        activityMapLayout = (ConstraintLayout) findViewById(R.id.activity_map_layout);

//        normal.clone(activityMapLayout);
//        normal.connect(R.id.map_get_to_know_me_layout, ConstraintSet.BOTTOM,
//                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
//        movedGTKM.clone(activityMapLayout);
//        normal.connect(R.id.map_get_to_know_me_layout, ConstraintSet.TOP,
//                ConstraintSet.PARENT_ID, ConstraintSet.TOP, GET_TO_KNOW_ME_ANIMATION_TRANSLATE);
//        //        movedGTKM.setMargin(R.id.map_get_to_know_me_layout, ConstraintSet.TOP, GET_TO_KNOW_ME_ANIMATION_TRANSLATE);
//        AutoTransition at = new AutoTransition();
//        at.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
//        TransitionManager.beginDelayedTransition(activityMapLayout, at);
//        movedGTKM.applyTo(activityMapLayout);


//        normal.clone(activityMapLayout);
//        movedGTKM.load(this,R.layout.activity_map_moved_gtkm);
//        AutoTransition at = new AutoTransition();
//        at.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
//        TransitionManager.beginDelayedTransition(activityMapLayout, at);


        translateAnimationYIn = new TranslateAnimation(0, 0, 0,
                GET_TO_KNOW_ME_ANIMATION_END_Y);
        translateAnimationYIn.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        translateAnimationYIn.setFillAfter(true);
        translateAnimationYIn.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
                getToKnowMeTv.setText(textViewTextToSet);
            }
            @Override public void onAnimationEnd(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
        });

        translateAnimationYOut = new TranslateAnimation(0, 0, 0,
                GET_TO_KNOW_ME_ANIMATION_START_Y);
        translateAnimationYOut.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        translateAnimationYOut.setFillAfter(true);

        translateAnimationYOutIn = new TranslateAnimation(0, 0, 0,
                GET_TO_KNOW_ME_ANIMATION_START_Y);
        translateAnimationYOutIn.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
        translateAnimationYOutIn.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationEnd(Animation animation) {
                getToKnowMeTv.setText(textViewTextToSet);
                getToKnowMeLayout.startAnimation(translateAnimationYIn);}
            @Override public void onAnimationRepeat(Animation animation) {
            }
        });
        translateAnimationYOutIn.setFillAfter(true);
//        getToKnowMeLayout.startAnimation(translateAnimationYOut);

//        constraintSetIn = new ConstraintSet();
//        constraintSetOut = new ConstraintSet();
//        constraintSetIn.clone(this, R.layout.activity_map);
//        constraintSetOut.clone(this, R.layout.activity_map);
//        constraintSetIn.setTransformPivotY(R.id.map_get_to_know_me_layout,
//                GET_TO_KNOW_ME_ANIMATION_TRANSLATE);
//        autoTransition = new AutoTransition();
//        autoTransition.setDuration(GET_TO_KNOW_ME_ANIMATION_TIME);
//        autoTransition.setInterpolator(new AccelerateDecelerateInterpolator());
//        TransitionManager.beginDelayedTransition(getToKnowMeLayout, autoTransition);

        getToKnowMeTv = findViewById(R.id.map_get_to_know_me_textview);
        getToKnowMeIb = findViewById(R.id.map_get_to_know_me_imagebutton);
        getToKnowMeIb.setBackgroundColor(Color.TRANSPARENT);

        bl = new BusinessLayerImpl(this);
        mapDS = new DataStructure(Memory.getPoints(),
                Memory.ZOO_ENTRANCE_LOCATION,
                Memory.ZOO_ENTRANCE_POINT,
                Memory.getXLongitudeRatio(),
                Memory.getYLatitudeRatio(),
                Memory.getSinAlpha(),
                Memory.getCosAlpha(),
                Memory.minLatitude,
                Memory.maxLatitude,
                Memory.minLongitude,
                Memory.maxLongitude
        );
        mapView.SetInitialValues();
        setNetworkDataProvider();
    }

    private CountDownLatch cdl;

    private void setNetworkDataProvider() {
        cdl = new CountDownLatch(2);
        bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosures = (Enclosure[]) response;
                // TODO: remove dummies
                enclosures[0].setClosestPointX(425);
                enclosures[0].setClosestPointY(356);
                enclosures[1].setClosestPointX(164);
                enclosures[1].setClosestPointY(601);
                enclosures[2].setClosestPointX(1210);
                enclosures[2].setClosestPointY(935);
                getEnclosureIconsAndSetImagesOnMap(enclosures);
                cdl.countDown();
            }

            @Override
            public void onFailure(Object response) {
                Log.e(GlobalVariables.LOG_TAG, response.toString());
            }
        });

        bl.getMisc(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                getMiscIconsAndSetImagesOnMap((Misc[]) response);
            }

            @Override
            public void onFailure(Object response) {
                Log.e(GlobalVariables.LOG_TAG, response.toString());
            }
        });

        GlobalVariables.bl.getPersonalStories(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                animalStories = (Animal.PersonalStories[]) response;
                Log.e("AVIV", Arrays.toString((Animal.PersonalStories[]) response));
                cdl.countDown();
            }

            @Override
            public void onFailure(Object response) {
                Log.e(GlobalVariables.LOG_TAG, response.toString());
            }
        });

        new Thread() {
            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mapDS.addAnimalStoriesToPoints(enclosures, animalStories);
            }
        }.start();
    }

    private void getMiscIconsAndSetImagesOnMap(Misc[] miscs) {
        for (int i = 0; i < miscs.length; i++) {
            final int finalI = i;
            bl.getImage(miscs[i].getMarkerIconUrl(), 0, 0, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    mapView.addMiscIcon(new BitmapDrawable(getResources(), (Bitmap) response),
                            miscs[finalI].getMarkerLongtitude(),
                            miscs[finalI].getMarkerLatitude());
                }

                @Override
                public void onFailure(Object response) {
                    Log.e(GlobalVariables.LOG_TAG, response.toString());
                }
            });
        }
    }

    private void getEnclosureIconsAndSetImagesOnMap(Enclosure[] enclosures) {
        for (int i = 0; i < enclosures.length; i++) {
            final int finalI = i;
            bl.getImage(enclosures[i].getMarkerIconUrl(), 0, 0, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    mapView.addEnclosure(enclosures[finalI],
                            new BitmapDrawable(getResources(), (Bitmap) response),
                            enclosures[finalI].getMarkerX(),
                            enclosures[finalI].getMarkerY());
                }

                @Override
                public void onFailure(Object response) {
                    Log.e(GlobalVariables.LOG_TAG, response.toString());
                }
            });
        }
    }

    private boolean needToShowIcon = true;
    private AtomicBoolean movementInProgress = new AtomicBoolean(false);

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (location.getAccuracy() <= MAX_ALLOWED_ACCURACY || GlobalVariables.DEBUG) {
            synchronized (movementInProgress) {
                if (movementInProgress.get())
                    return;
                movementInProgress.set(true);
            }
            Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG)
                    .show();

            if (location.getLatitude() < Memory.minLatitude - MAX_MARGIN ||
                    location.getLatitude() > Memory.maxLatitude + MAX_MARGIN ||
                    location.getLongitude() < Memory.minLongitude - MAX_MARGIN ||
                    location.getLongitude() > Memory.maxLongitude + MAX_MARGIN) {
                movementInProgress.set(false);
                return;
            }

            // TODO: remove this
            location.setLatitude(31.2591);
            location.setLongitude(34.7438);

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

    private void updateGetToKnowMe() {
        if (System.currentTimeMillis() - lastTimeUpdatedGetToKnowMe >
                MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES) {
            Set<Animal.PersonalStories> animalPersonalStories = mapDS.getCloseAnimalStories();
            if (animalPersonalStories.isEmpty()) {
                hideGetToKnowMe();
                return;
            }
            Animal.PersonalStories nextPersonalStory =
                    animalPersonalStories.toArray(
                            new Animal.PersonalStories[animalPersonalStories.size()])
                            [(int) (Math.random() * animalPersonalStories.size())];
            if (nextPersonalStory != lastPersonalStoryShowed)
                updateGetToKnowMe(nextPersonalStory);
            lastTimeUpdatedGetToKnowMe = System.currentTimeMillis();
        }
    }

    int c = 0;
    private boolean isGetToKnowMeShown = false;
    private void updateGetToKnowMe(Animal.PersonalStories nextPersonalStory) {
        textViewTextToSet = nextPersonalStory.getName() + ++c;
        if (isGetToKnowMeShown) {
            changeGetToKnowMe();
        } else {
            showGetToKnowMe();
        }

//        getToKnowMeIb.setImageDrawable(nextPersonalStory.getDrawable());
    }

    private void showGetToKnowMe() {
//        getToKnowMeTv.setText(text);
//        movedGTKM.applyTo(activityMapLayout);
//        constraintSetIn.applyTo(activityMapLayout);
        getToKnowMeLayout.startAnimation(translateAnimationYIn);
        isGetToKnowMeShown = true;
    }

    private void changeGetToKnowMe() {
//        constraintSetOut.applyTo(activityMapLayout);
//        normal.applyTo(activityMapLayout);
//        getToKnowMeTv.setText(text);
//        movedGTKM.applyTo(activityMapLayout);
        getToKnowMeLayout.startAnimation(translateAnimationYOutIn);
//        constraintSetIn.applyTo(activityMapLayout);

    }

    private void hideGetToKnowMe() {
//        normal.applyTo(activityMapLayout);
//        constraintSetOut.applyTo(activityMapLayout);
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

//    private boolean isMessageShown = false;
//    private Set<AnimalStory> lastAnimalStorySet;
//    private void updateGetToKnowMe(Point point) {
//        if(isMessageShown)
//            return;
//        isMessageShown = true;
//
//    }

//    private Set<AnimalStory> getClosestAnimalStories(Point point) {
//        Set<Enclosure> nearEnclosures = new HashSet<>();
//        for (Enclosure enc :
//                enclosures) {
//            if(mapDS.squaredDistance(new Point(enc.getMarkerX(), enc.getMarkerY()), point) <
// MAX_DISTANCE_FROM_ENCLOSURE) {
//                nearEnclosures.add(enc);
//            }
//        }
////        Set<Enclosure> nearEnclosures = Arrays.stream(enclosures).filter(enclosure -> mapDS
/// .squaredDistance(new Point(enclosure.getMarkerX(), enclosure.getMarkerY()), point) <
/// MAX_DISTANCE_FROM_ENCLOSURE).collect(Collectors.toSet());
//    }

//    @Override
//    public void setAnimalStory(AnimalStory as) {
//        getToKnowMeTv.setText(as.getName());
//        getToKnowMeIb.setImageDrawable(as.getPictureDrawable());
//    }
}