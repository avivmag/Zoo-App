package com.zoovisitors.cl.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.zoovisitors.R;
import com.zoovisitors.pl.map.MapActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CleanProvider {
    public static final int PERMISSION_REQUEST_GPS = 310;

    private final MapActivity activity;
    private final Callback callback;
    private LocationManager lm;

    public CleanProvider(MapActivity activity, Callback callback) {
        this.activity = activity;
        this.callback = callback;

    }

    @SuppressLint("MissingPermission")
    public void start() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                callback.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO: raise awareness to the user in case something goes wrong.
            }

            @Override
            public void onProviderEnabled(String provider) {
                callback.onProviderEnabled();
            }

            @Override
            public void onProviderDisabled(String provider) {
                callback.onProviderDisabled();
                handleActivation();
            }
        };
        lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!handlePermissions() && !handleActivation()) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     *
     * @return true if it needs to handle the permission and the general flow should not continue.
     */
    private boolean handlePermissions() {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.gps_no_permission_dialog_title)
                        .setMessage(R.string.gps_no_permission_dialog_message)
                        .setPositiveButton(R.string.gps_no_permission_dialog_approve, (dialog, id) -> {
                            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
                        })
                        .setNegativeButton(R.string.gps_no_permission_dialog_disapprove, (dialog, id) -> {
                        })
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
            }
            return true;
        }
        return false;
    }

    public void stop() {

    }

    public void onRequestPermissionsResult(int grantResults) {
        // if permission is not granted, for now I want the users to enable location - no matter what.
//        if (grantResults != PackageManager.PERMISSION_GRANTED) {
//
//        }
        start();
    }

    /**
     *
     * @return true if it needs to handle the activation and the general flow should not continue.
     */
    public boolean handleActivation() {
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;

        if(!activity.isFinishing())
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.gps_no_activated_dialog_title)
                    .setMessage(activity.getResources().getString(R.string.gps_no_activated_dialog_activate_needed))
                    .setPositiveButton(activity.getResources().getString(R.string.gps_no_activated_dialog_open_settings), (paramDialogInterface, paramInt) -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(myIntent);
                    })
                    .setNegativeButton(activity.getString(R.string.gps_no_activated_dialog_do_not_open_settings), (paramDialogInterface, paramInt) -> {
                    })
                    .show();
        return true;
    }
}
