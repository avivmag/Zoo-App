package com.zoovisitors.cl.gps;

import android.Manifest;
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

/**
 * Created by aviv on 17-Mar-18.
 */

public class Provider {
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Provider(Context context)
    {
        this.context = context;
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     *
     * @return true only if the listener has been activated, otherwise returns false and may pop up
     * dialog asking for permission or asking for activating gps provider.
     */
    public boolean startLocationListener(Callback callback) {
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(context);
            final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            final String message = "Enable either GPS or any other location"
                    + " service to find current location.  Click OK to go to"
                    + " location services settings to let you do so.";

            builder.setMessage(message)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    context.startActivity(new Intent(action));
                                    d.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int id) {
                                    d.cancel();
                                }
                            });
            builder.create().show();
            return false;
        }
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                callback.onLocationChanged(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO: raise awareness to the user in case something goes wrong.
            }

            public void onProviderEnabled(String provider) {
                callback.onProviderEnabled(provider);
            }

            public void onProviderDisabled(String provider) {
                stopLocationListener();
                callback.onProviderDisabled(provider);
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(context);
                final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                final String message = "Enable either GPS or any other location"
                        + " service to find current location.  Click OK to go to"
                        + " location services settings to let you do so.";

                builder.setMessage(message)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        context.startActivity(new Intent(action));
                                        d.dismiss();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {
                                        d.cancel();
                                    }
                                });
                builder.create().show();
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        return true;
    }

    private void stopLocationListener()
    {
        // Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
    }
}