package com.mapgenerator.handlers;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Should make sure all services needed by this application are up and running.
 * Created by aviv on 07-Dec-17.
 */
public class ServicesHandler {
    public static final int PERMISSION_REQUEST = 1;
    public static final int GPS_LOCATION_PROVIDER_ASKED = 2;
    private AppCompatActivity parentActivity;
    private static final String[] permissionsString = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };

    public ServicesHandler(AppCompatActivity parentActivity)
    {
        this.parentActivity = parentActivity;
    }

    /**
     * validate if all permissions that are needed are granted, else ask for it and returns the
     * outcome to activity.
     * @return true if permission was asked
     */
    public void validatePermissions(Runnable runnable)
    {
        // check if all permissions are granted, if not - request them.
        for (String str :
                permissionsString) {
            if(ActivityCompat.checkSelfPermission(parentActivity, str) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(parentActivity, permissionsString, PERMISSION_REQUEST);
                return;
            }
        }
        permissionGranted(runnable);
    }

    /**
     * The next stage after all permissions are granted.
     * If needed asks the user to enable gps location.
     * @param runnable
     */
    public void permissionGranted(Runnable runnable) {
        LocationManager locationManager = (LocationManager) parentActivity.getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try { gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); } catch(Exception ex) {Log.e(GlobalVariables.APPLICATION_TAG, ex.toString());}
        try { network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); } catch(Exception ex) {Log.e(GlobalVariables.APPLICATION_TAG, ex.toString());}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(parentActivity);
            dialog.setMessage("please enable GPS");
            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    // Note: the result for this activity is delivered to @parentActivity
                    parentActivity.startActivityForResult(myIntent, GPS_LOCATION_PROVIDER_ASKED);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Toast.makeText(parentActivity, "GPS must be on", Toast.LENGTH_LONG);
                }
            });
            dialog.show();
        }
        else
            runnable.run();
    }
}
