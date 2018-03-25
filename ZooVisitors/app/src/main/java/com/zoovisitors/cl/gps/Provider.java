//package com.zoovisitors.cl.gps;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//
///**
// * Created by aviv on 17-Mar-18.
// */
//
//public class Provider {
//    private Activity activity;
//    private LocationManager locationManager;
//    private LocationListener locationListener;
//    public static final int MY_PERMISSIONS_REQUEST_GPS = 1;
//
//    public Provider(Activity activity)
//    {
//        this.activity = activity;
//        // Acquire a reference to the system Location Manager
//        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//    }
//
//    /**
//     *
//     * @return true only if the listener has been activated, otherwise returns false and may pop up
//     * dialog asking for permission or asking for activating gps provider.
//     */
//    @SuppressLint("MissingPermission")
//    public boolean startLocationListener(Activity activity, Callback callback) {
////
////        // Here, thisActivity is the current activity
////        if (ContextCompat.checkSelfPermission(activity,
////                Manifest.permission.ACCESS_FINE_LOCATION)
////                != PackageManager.PERMISSION_GRANTED) {
////
////            // Permission is not granted
////            // Should we show an explanation?
////            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
////                    Manifest.permission.ACCESS_FINE_LOCATION)) {
////
////                // Show an explanation to the user *asynchronously* -- don't block
////                // this thread waiting for the user's response! After the user
////                // sees the explanation, try again to request the permission.
////
////            } else {
////
////                // No explanation needed; request the permission
////                ActivityCompat.requestPermissions(activity,
////                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
////                        MY_PERMISSIONS_REQUEST_GPS);
////
////                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
////                // app-defined int constant. The callback method gets the
////                // result of the request.
////            }
////        }
////        else {
////            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
////        }
////
//////
//////
////        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
////        boolean gps_enabled = false;
////
////        try {
////            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
////        } catch(Exception ex) {}
////
////        // Register the listener with the Location Manager to receive location updates
////        if (!gps_enabled) {
////            final AlertDialog.Builder builder =
////                    new AlertDialog.Builder(activity);
////            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
////            final String message = "Enable either GPS or any other location"
////                    + " service to find current location.  Click OK to go to"
////                    + " location services settings to let you do so.";
////
////            builder.setMessage(message)
////                    .setPositiveButton("OK",
////                            new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface d, int id) {
////                                    activity.startActivity(new Intent(action));
////                                    d.dismiss();
////                                }
////                            })
////                    .setNegativeButton("Cancel",
////                            new DialogInterface.OnClickListener() {
////                                public void onClick(DialogInterface d, int id) {
////                                    d.cancel();
////                                }
////                            });
////            builder.create().show();
////            return false;
////        }
////        // Define a listener that responds to location updates
////        else {
//            locationListener = new LocationListener() {
//                public void onLocationChanged(Location location) {
//                    Log.e("AVIV", "Loation changed");
//                    callback.onLocationChanged(location);
//                }
//
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                    // TODO: raise awareness to the user in case something goes wrong.
//                }
//
//                public void onProviderEnabled(String provider) {
//                    callback.onProviderEnabled(provider);
//                }
//
//                public void onProviderDisabled(String provider) {
//                    stopLocationListener();
//                    callback.onProviderDisabled(provider);
//                    final AlertDialog.Builder builder =
//                            new AlertDialog.Builder(activity);
//                    final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
//                    final String message = "Enable either GPS or any other location"
//                            + " service to find current location.  Click OK to go to"
//                            + " location services settings to let you do so.";
//
//                    builder.setMessage(message)
//                            .setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface d, int id) {
//                                            activity.startActivity(new Intent(action));
//                                            d.dismiss();
//                                        }
//                                    })
//                            .setNegativeButton("Cancel",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface d, int id) {
//                                            d.cancel();
//                                        }
//                                    });
//                    builder.create().show();
//                }
//            };
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
////            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            return true;
////        }
//
////        return false;
//
////        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
////        boolean gps_enabled = false;
////
////        try {
////            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
////        } catch(Exception ex) {}
////
////        if(!gps_enabled) {
////            // notify user
////            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
////            dialog.setMessage("gps is not enabled");
////            dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
////                    // TODO Auto-generated method stub
////                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                    activity.startActivity(myIntent);
////                    //get gps
////                }
////            });
////            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
////                    // TODO Auto-generated method stub
////
////                }
////            });
////            dialog.show();
////            return false;
////        }
////        return true;
//    }
////    @SuppressLint("MissingPermission")
////    public void startLocation()
////    {
////
////    }
//
//    private void stopLocationListener()
//    {
//        // Remove the listener you previously added
//        locationManager.removeUpdates(locationListener);
//    }
//
//    public boolean checkPermission() {
//        int result = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION);
//
//        return result == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public void requestPermission() {
//        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_GPS );
//    }
//
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(activity)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//}