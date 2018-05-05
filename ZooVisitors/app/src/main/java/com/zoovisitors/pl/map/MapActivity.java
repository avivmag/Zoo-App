package com.zoovisitors.pl.map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
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

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mapView;
    private BusinessLayer bl;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;
    private final double MAX_MARGIN = 10 * 0.0111111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_view_layout);
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
        mapView.SetZooMapIcon();
        mapView.SetVisitorIcon();
        setNetworkDataProvider();
    }

    private void setNetworkDataProvider() {
        bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                getEnclosureIconsAndSetImagesOnMap((Enclosure[]) response);
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
                Log.e(GlobalVariables.LOG_TAG, "Callback failed");
            }
        });
    }

    private void getMiscIconsAndSetImagesOnMap(Misc[] miscs) {
        for (int i = 0; i < miscs.length; i++) {
            final int finalI = i;
            bl.getImage(miscs[i].getMarkerIconUrl(), 0, 0, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    mapView.addMiscIcon(new BitmapDrawable(getResources(), (Bitmap) response),
                            miscs[finalI],
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

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (location.getAccuracy() <= MAX_ALLOWED_ACCURACY || GlobalVariables.DEBUG) {
            Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG).show();

            if (location.getLatitude() < Memory.minLatitude - MAX_MARGIN ||
                    location.getLatitude() > Memory.maxLatitude + MAX_MARGIN ||
                    location.getLongitude() < Memory.minLongitude - MAX_MARGIN ||
                    location.getLongitude() > Memory.maxLongitude + MAX_MARGIN)
                return;

            if (needToShowIcon) {
                needToShowIcon = false;
                mapView.ShowVisitorIcon();
            }

            Point p = mapDS.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
            Point calibratedPoint = mapDS.getOnMapPosition(p);
            if (calibratedPoint == null)
                return;
            mapView.UpdateVisitorLocation(calibratedPoint.getX(), calibratedPoint.getY());
        }
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
