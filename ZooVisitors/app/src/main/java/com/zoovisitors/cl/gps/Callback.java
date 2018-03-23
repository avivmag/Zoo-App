package com.zoovisitors.cl.gps;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by aviv on 17-Mar-18.
 */

public interface Callback {
    void onLocationChanged(Location location);
//    void onStatusChanged(String provider, int status, Bundle extras);
    void onProviderEnabled(String provider);
    void onProviderDisabled(String provider);
}
