package com.mapgenerator.handlers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * A controller which gets coordinates, stores them and updates the caller  when a new coordinate
 * is added.
 * Created by aviv on 06-Dec-17.
 */
public class Controller implements LocationHandler.FunctionPasser {
    private Activity parentActivity;
    private LocationHandler locationHandler;
    private DatabaseHandler databaseHandler;
    private ServicesHandler servicesHandler;

    public Controller(AppCompatActivity parentActivity)
    {
        this.parentActivity = parentActivity;
        servicesHandler = new ServicesHandler(parentActivity);
        locationHandler = new LocationHandler(parentActivity, this::updateLocation);
        databaseHandler = new DatabaseHandler();
    }

    /**
     * Starts the coordinates gathering process.
     */
    public void start()
    {
        // validate permissions
        servicesHandler.validatePermissions(locationHandler::start);
    }

    /**
     * When all permissions needed are granted, this method should be called.
     */
    public void permissionGranted()
    {
        servicesHandler.permissionGranted(locationHandler::start);
    }

    /**
     * This method should be called when gathering process should stop.
     */
    public void stop()
    {
        locationHandler.stop();
    }

    private int i;
    public void updateLocation(android.location.Location location)
    {
        Log.e("Aviv", "Round: " + ++i + ",\nAccuracy: " + location.getAccuracy() + "\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + ",\nBearing: " + location.getBearing());
    }

}


