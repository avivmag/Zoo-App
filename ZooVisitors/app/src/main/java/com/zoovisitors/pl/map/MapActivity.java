package com.zoovisitors.pl.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
//import com.zoovisitors.backend.RecurringEvent;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.cl.gps.ProviderBasedActivity;
import com.zoovisitors.dal.Memory;
import com.zoovisitors.pl.personalStories.PersonalStoriesRecyclerAdapter;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mapView;
    private BusinessLayer bl;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;
    private Enclosure[] enclosures;
    private Animal.PersonalStories[] animalStories;
    private TextView getToKnowMeTv;
    private ImageView getToKnowMeIb;
    private final long MAX_TIME_BETWEEN_GET_TO_KNOW_ME_UPDATES = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_view_layout);
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
                enclosures[0].setClosestPointX(866);
                enclosures[0].setClosestPointY(352);
//                enclosures[1].setClosestPointX(441);
//                enclosures[1].setClosestPointY(336);
                enclosures[1].setClosestPointX(1210);
                enclosures[1].setClosestPointY(935);
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
            Log.e("AVIV", "animalPersonalStories " + animalPersonalStories.size());
            if(animalPersonalStories.isEmpty())
                return;
            Animal.PersonalStories nextPersonalStory =
                    animalPersonalStories.toArray(
                            new Animal.PersonalStories[animalPersonalStories.size()])
                            [(int) (Math.random() * animalPersonalStories.size())];
            if(nextPersonalStory == lastPersonalStoryShowed)
                return;
            updateGetToKnowMe(nextPersonalStory);
        }
    }
    private void updateGetToKnowMe(Animal.PersonalStories nextPersonalStory) {
        Log.e("AVIV", "nextPersonalStory " + nextPersonalStory.toString());
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