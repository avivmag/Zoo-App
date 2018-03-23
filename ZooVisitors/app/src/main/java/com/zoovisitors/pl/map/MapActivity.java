package com.zoovisitors.pl.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.zoovisitors.cl.gps.Callback;
import com.zoovisitors.cl.gps.Provider;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private Enclosure[] enclosures;
    private Provider gpsProvider;
    private BusinessLayer bl;
    private DataStructure mapDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_test_frame);
        gpsProvider = new Provider(this);
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
        setLocationProvider();
    }

    private void setNetworkDataProvider() {
//        bl.getEnclosures(new GetObjectInterface() {
//            @Override
//            public void onSuccess(Object response) {
//                enclosures = (Enclosure[]) response;

                mapView.addZooMapIcon(0, 0);
                for(int i = 0; i<5;i++)
                {
                    mapView.addImageIcon("animal_" + i, "",150 + 150*i, 200 + (int)(Math.random()*100));
                }
//            }
//            @Override
//            public void onFailure(Object response) {
//                Log.e(GlobalVariables.LOG_TAG, "Callback failed");
//            }
//        });
    }

    private final int MAX_ALLOWED_ACCURACY = 7;
    private void setLocationProvider() {
        gpsProvider.startLocationListener(new Callback() {
            boolean needToShowIcon = true;
            @Override
            public void onLocationChanged(android.location.Location location) {
                if(GlobalVariables.DEBUG || location.getAccuracy() <= MAX_ALLOWED_ACCURACY)
                {
                    Log.e("AVIV", "Location Update: " + location.getLatitude() + "," + location.getLongitude());
                    if(needToShowIcon) {
                        needToShowIcon = false;
                        mapView.ShowVisitorIcon();
                    }

                    Point p = Dummy.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
                    Log.e("AVIV", "MapActivity: " + p);
                    Point calibratedPoint = mapDS.getOnMapPosition(p);
                    if(calibratedPoint == null)
                        return;
                    Log.e("AVIV", "On Map Location: " + calibratedPoint.toString());
//                    Point currentPoint =
//                            mapDS.LatLngPointToImagePoint(calibratedPoint);
//                    Log.e("AVIV", "Image location: " + currentPoint.toString());
                    mapView.UpdateVisitorLocation(calibratedPoint.getX(),calibratedPoint.getY());
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                needToShowIcon = true;
            }

            @Override
            public void onProviderDisabled(String provider) {
                mapView.HideVisitorIcon();
            }
        });
    }
}
