package com.zoovisitors.cl.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.zoovisitors.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public abstract class ProviderBasedActivity extends Activity {
    public static final int PERMISSION_REQUEST_GPS = 310;
    private LocationManager lm;
    private LocationListener locationListener;

    public void startProviderActivity() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getMinDistance();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ProviderBasedActivity.this.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                ProviderBasedActivity.this.onProviderEnabled();
            }

            @Override
            public void onProviderDisabled(String provider) {
                ProviderBasedActivity.this.onProviderDisabled();
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.gps_no_permission_dialog_title)
                        .setMessage(R.string.gps_no_permission_dialog_message)
                        .setPositiveButton(R.string.gps_no_permission_dialog_approve, (dialog, id) -> {
                            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
                        })
                        .setNegativeButton(R.string.gps_no_permission_dialog_disapprove, (dialog, id) -> {
                        })
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS);
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

        if(!isFinishing())
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
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startProviderActivity();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();

        if (!handlePermissions() && !handleActivation()) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, getMinTime(), getMinDistance(), locationListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        lm.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_GPS:
//                provider.start(this, getLocationListener());
                break;
        }
    }

    public abstract void onLocationChanged(Location location);
    //    void onStatusChanged(String provider, int status, Bundle extras);
    public abstract void onProviderEnabled();
    public abstract void onProviderDisabled();
    public abstract int getMinTime();
    public abstract int getMinDistance();

}