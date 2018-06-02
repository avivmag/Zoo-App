package com.zoovisitors.cl.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

public class GpsService extends Service
{
    private static final double MAX_MARGIN = 10 * 0.0111111;
    private LocationListener mLocationListener = new LocationListener();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 30 * 1000;
    private static final float LOCATION_DISTANCE = 0;

    private class LocationListener implements android.location.LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
            if(!GlobalVariables.notifications)
                return;

            boolean isInPark = (location.getLatitude() >= GlobalVariables.bl.getMapResult().getMapInfo().getMinLatitude() - MAX_MARGIN &&
                    location.getLatitude() <= GlobalVariables.bl.getMapResult().getMapInfo().getMaxLatitude() + MAX_MARGIN &&
                    location.getLongitude() >= GlobalVariables.bl.getMapResult().getMapInfo().getMinLongitude() - MAX_MARGIN &&
                    location.getLongitude() <= GlobalVariables.bl.getMapResult().getMapInfo().getMaxLongitude() + MAX_MARGIN);
            GlobalVariables.bl.updateIfInPark(isInPark);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListener);
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception ex) {
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}