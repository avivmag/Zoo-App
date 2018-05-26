package com.zoovisitors.cl.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
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

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public abstract class GpsService extends Service {
    private static final int PERMISSION_REQUEST_GPS = 3100;
    private final long MIN_TIME_MILISECONDS = 3000;
    private final float MIN_DISTANCE_METERS = 0;
    private LocationManager lm;
    private LocationListener locationListener;

    public void startProviderActivity() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GetObjectInterface goi = new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                Log.e("Notification Success", (String) response);
            }

            @Override
            public void onFailure(Object response) {
                Log.e("Notification Failed", (String) response);
            }
        };

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GlobalVariables.bl.updateIfInPark(isInPark(location), goi);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) {
                handleActivation();
            }
        };
    }

    /**
     *
     * @return true if it needs to handle the permission and the general flow should not continue.
     */
    private boolean handlePermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?

            try_again:
            try {
                if (ActivityCompat.shouldShowRequestPermissionRationale(GlobalVariables.foregroundActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.gps_no_permission_dialog_title)
                            .setMessage(R.string.gps_no_permission_dialog_message)
                            .setPositiveButton(R.string.gps_no_permission_dialog_approve, (dialog, id) -> {
                                ActivityCompat.requestPermissions(GlobalVariables.foregroundActivity, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
                            })
                            .setNegativeButton(R.string.gps_no_permission_dialog_disapprove, (dialog, id) -> {
                            })
                            .show();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(GlobalVariables.foregroundActivity, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
                }
            } catch (Exception e) {
                // in case the GlobalVariables.foregroundActivity is currently not available
                try {Thread.sleep(500);} catch (InterruptedException e1) {}
                break try_again;
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @return true if it needs to handle the activation and the general flow should not continue.
     */
    public boolean handleActivation() {
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;

        try_again:
        try {
            if(!GlobalVariables.foregroundActivity.isFinishing())
                new AlertDialog.Builder(this)
                        .setTitle(R.string.gps_no_activated_dialog_title)
                        .setMessage(this.getResources().getString(R.string.gps_no_activated_dialog_activate_needed))
                        .setPositiveButton(this.getResources().getString(R.string.gps_no_activated_dialog_open_settings), (paramDialogInterface, paramInt) -> {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            this.startActivity(myIntent);
                        })
                        .setNegativeButton(this.getString(R.string.gps_no_activated_dialog_do_not_open_settings), (paramDialogInterface, paramInt) -> {
                        })
                        .show();
        } catch (Exception e) {
            // in case the GlobalVariables.foregroundActivity is currently not available
            try {Thread.sleep(500);} catch (InterruptedException e1) {}
            break try_again;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        startProviderActivity();
        if (!handlePermissions() && !handleActivation()) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_MILISECONDS, MIN_DISTANCE_METERS, locationListener);
        }
    }

    public boolean isInPark(Location location) {
        // TODO:
        return true;
//        return Memory.getMinX()
    }
}