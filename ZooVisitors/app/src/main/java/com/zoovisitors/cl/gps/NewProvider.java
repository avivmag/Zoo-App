package com.zoovisitors.cl.gps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zoovisitors.pl.map.MapActivity;

/**
 * Created by aviv on 26-Mar-18.
 */

public class NewProvider {
    public static final int PERMISSION_REQUEST_GPS = 310;

    @SuppressLint("MissingPermission")
    public static void start(MapActivity activity, Callback callback)
    {
        Log.e("AVIV", "yes start");
        LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    Log.e("AVIV", "Loation changed");
                    callback.onLocationChanged(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO: raise awareness to the user in case something goes wrong.
                }

                public void onProviderEnabled(String provider) {
                    callback.onProviderEnabled(provider);
                }

                public void onProviderDisabled(String provider) {
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
                }
            };
        LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 0, locationListener);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }
}
