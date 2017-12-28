package com.mapgenerator.handlers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Handles the current location of the smartphone.
 * Created by aviv on 06-Dec-17.
 */
public class LocationHandler {
    private static int MIN_TIME = 0;
    private static int MIN_DISTANCE = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Activity parentActivity;
    public LocationHandler(Activity parentActivity, FunctionPasser functionPasser) {
        // Acquire a reference to the system LocationHandler Controller
        this.parentActivity = parentActivity;
        this.locationManager = (LocationManager) parentActivity.getSystemService(LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                functionPasser.updateLocation(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    /**
     * Start gathering the location.
     */
    @SuppressLint("MissingPermission")
    public void start() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Stop gathering the location.
     */
    public void stop(){
        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
    }

    /**
     * There only to pass this function (minor usage)
     */
    public interface FunctionPasser {
        void updateLocation(android.location.Location location);
    }
}