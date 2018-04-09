package com.zoovisitors.pl.map;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;
import com.zoovisitors.bl.map.DataStructure;
import com.zoovisitors.bl.map.Dummy;
import com.zoovisitors.cl.gps.ProviderBasedActivity;

public class MapActivity extends ProviderBasedActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mapView;
    private Enclosure[] enclosures;
    private BusinessLayer bl;
    private DataStructure mapDS;
    private static final int MAX_ALLOWED_ACCURACY = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_test_frame);
        bl = new BusinessLayerImpl(this);
        mapDS = new DataStructure(Dummy.getPoints(),
                Dummy.ZOO_ENTRANCE_LOCATION,
                Dummy.ZOO_ENTRANCE_POINT,
                Dummy.getXLongitudeRatio(),
                Dummy.getYLatitudeRatio(),
                Dummy.getSinAlpha(),
                Dummy.getCosAlpha()
        );

        mapView.AddVisitorIcon();
        setNetworkDataProvider();
    }

    private void setNetworkDataProvider() {
        mapView.addZooMapIcon(0, 0);
        bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosures = (Enclosure[]) response;

                // get image urls
                for (int i = 0; i < enclosures.length; i++) {
                    final int finalI = i;
                    bl.getImage(enclosures[i].getMarkerIconUrl(), new GetObjectInterface() {
                        @Override
                        public void onSuccess(Object response) {
                            mapView.addImageIcon(new BitmapDrawable(getResources(), (Bitmap) response),
                                    enclosures[finalI],
                                    enclosures[finalI].getMarkerLongtitude(),
                                    enclosures[finalI].getMarkerLatitude());
                        }

                        @Override
                        public void onFailure(Object response) {
                            Log.e(GlobalVariables.LOG_TAG, response.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Object response) {
                Log.e(GlobalVariables.LOG_TAG, "Callback failed");
            }
        });
    }

    private boolean needToShowIcon = true;

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (location.getAccuracy() <= MAX_ALLOWED_ACCURACY || GlobalVariables.DEBUG) {
            Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
            if (needToShowIcon) {
                needToShowIcon = false;
                mapView.ShowVisitorIcon();
            }

            Point p = Dummy.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
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
