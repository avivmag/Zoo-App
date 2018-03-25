package com.zoovisitors.pl.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.zoovisitors.cl.gps.Callback;
import com.zoovisitors.cl.gps.NewProvider;

//import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private MapView mapView;
    private Enclosure[] enclosures;
    private NewProvider gpsProvider;
    private BusinessLayer bl;
    private DataStructure mapDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_test_frame);
//        gpsProvider = new NewProvider(this);
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
        // TODO: get it from the server also.
        mapView.addZooMapIcon(0, 0);
        bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosures = (Enclosure[]) response;


//                for(int i = 0; i<5;i++)
//                {
//                    mapView.addImageIcon("animal_" + i, "",150 + 150*i, 200 + (int)(Math.random()*100));
//                }

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
                            Log.e("AVIV", response.toString());
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

    private final int MAX_ALLOWED_ACCURACY = 7;
//    private void setLocationProvider() {













//        if(!gpsProvider.checkPermission())
//        {
//            gpsProvider.requestPermission();
//        }
//        else {
//            Log.e("AVIV", "GOT HERE!");
//            gpsProvider.startLocationListener(this, new Callback() {
//                boolean needToShowIcon = true;
//
//                @Override
//                public void onLocationChanged(android.location.Location location) {
//                    if (GlobalVariables.DEBUG || location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
//                        Log.e("AVIV", "Location Update: " + location.getLatitude() + "," + location.getLongitude());
//                        if (needToShowIcon) {
//                            needToShowIcon = false;
//                            mapView.ShowVisitorIcon();
//                        }
//
//                        Point p = Dummy.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
//                        Log.e("AVIV", "MapActivity: " + p);
//                        Point calibratedPoint = mapDS.getOnMapPosition(p);
//                        if (calibratedPoint == null)
//                            return;
//                        Log.e("AVIV", "On Map Location: " + calibratedPoint.toString());
//                        mapView.UpdateVisitorLocation(calibratedPoint.getX(), calibratedPoint.getY());
//                    }
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//                    needToShowIcon = true;
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//                    mapView.HideVisitorIcon();
//                }
//            });
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case Provider.MY_PERMISSIONS_REQUEST_GPS:
//                if (grantResults.length > 0) {
//
//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
////                    if (locationAccepted)
////                        Snackbar.make(view, "Permission Granted, Now you can access location data and start.", Snackbar.LENGTH_LONG).show();
////                    else {
////
////                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
////
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
////                                showMessageOKCancel("You need to allow access to both the permissions",
////                                        new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
////                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
////                                                            PERMISSION_REQUEST_CODE);
////                                                }
////                                            }
////                                        });
////                                return;
////                            }
////                        }
////
////                    }
//                }
//
//
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == NewProvider.PERMISSION_REQUEST_GPS) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("AVIV", "yes permission");
                NewProvider.start(this, new Callback() {
                    boolean needToShowIcon = true;

                    @Override
                    public void onLocationChanged(android.location.Location location) {
                        if (GlobalVariables.DEBUG || location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
                            Log.e("AVIV", "acc: " + location.getAccuracy());
                            Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                            //                            Log.e("AVIV", "Location Update: " + location.getLatitude() + "," + location.getLongitude() + " acc: " + location.getAccuracy());
                            if (needToShowIcon) {
                                needToShowIcon = false;
                                mapView.ShowVisitorIcon();
                            }

                            Point p = Dummy.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
                            Log.e("AVIV", "MapActivity: " + p);
                            Point calibratedPoint = mapDS.getOnMapPosition(p);
                            if (calibratedPoint == null)
                                return;
                            Log.e("AVIV", "On Map Location: " + calibratedPoint.toString());
                            mapView.UpdateVisitorLocation(calibratedPoint.getX(), calibratedPoint.getY());
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
            } else {
                Log.e("AVIV", "no permission");
                Toast.makeText(this,"Permission not granted", Toast.LENGTH_LONG).show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void setLocationProvider(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            NewProvider.start(this, new Callback() {
                boolean needToShowIcon = true;

                @Override
                public void onLocationChanged(android.location.Location location) {
                    if (GlobalVariables.DEBUG || location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {

//                        Log.e("AVIV", "acc: " + location.getAccuracy());
                        Toast.makeText(MapActivity.this, "acc: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                        if (needToShowIcon) {
                            needToShowIcon = false;
                            mapView.ShowVisitorIcon();
                        }

                        Point p = Dummy.locationToPoint(new Location(location.getLatitude(), location.getLongitude()));
                        Log.e("AVIV", "MapActivity: " + p);
                        Point calibratedPoint = mapDS.getOnMapPosition(p);
                        if (calibratedPoint == null)
                            return;
                        Log.e("AVIV", "On Map Location: " + calibratedPoint.toString());
                        mapView.UpdateVisitorLocation(calibratedPoint.getX(), calibratedPoint.getY());
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
        } else {
            // Permission is missing and must be requested.
            requestPermission();
        }
    }

    private void requestPermission() {
        Log.e("AVIV", "yes requestPermission");
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            NewProvider.PERMISSION_REQUEST_GPS);

        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, NewProvider.PERMISSION_REQUEST_GPS);
        }
    }
}
