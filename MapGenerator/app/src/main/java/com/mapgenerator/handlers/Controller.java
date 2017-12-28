package com.mapgenerator.handlers;

import android.app.Activity;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapgenerator.handlers.entities.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A controller which gets coordinates, stores them and updates the caller  when a new Coordinate
 * is added.
 * Created by aviv on 06-Dec-17.
 */
public class Controller implements LocationHandler.FunctionPasser {
    private static final String PRODUCTION_TAG = "production";
    private static final String INTEGRATION_TAG = "integration";
    private static final int MAX_ALLOWED_ACCURACY = 4;
    private static final double METERS_LOCATION_RATIO = 1e7;
    private static final int MAX_FOR_ROAD_CREATION = 5;
    //private static final int MIN_DISTANCE_BETWEEN_TWO_LOCATIONS = 2;

    private double diffLatitude;// = metersToLatitude((int)(MAX_ALLOWED_ACCURACY*1.5));
    private double diffLongitude;// = metersToLongitude((int)(MAX_ALLOWED_ACCURACY*1.5), (int)(location.getLatitude()*1e7));
    private int firstLatitude;
    private int firstLongitude;

    private Activity parentActivity;
    private LocationHandler locationHandler;
    private FunctionPasser functionPasser;
//    private List<Route> routes;
    /**
     * The data that will be forwarded to a production environment
     */
    private DatabaseHandler productionDatabaseHandler;
    /**
     * The data that will be used to show on this application
     */
    private DatabaseHandler integrationDatabaseHandler;
    private ServicesHandler servicesHandler;
    private List<Coordinate> onActivityCoordinates;

    public Controller(AppCompatActivity parentActivity, FunctionPasser functionPasser)
    {
        this.parentActivity = parentActivity;
        this.onActivityCoordinates = new ArrayList<Coordinate>();
        this.functionPasser = functionPasser;
//        this.routes = new ArrayList<Route>();
        servicesHandler = new ServicesHandler(parentActivity);
        locationHandler = new LocationHandler(parentActivity, this::updateLocation);
        productionDatabaseHandler = new DatabaseHandler(parentActivity, PRODUCTION_TAG);
        integrationDatabaseHandler = new DatabaseHandler(parentActivity, INTEGRATION_TAG);
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
        productionDatabaseHandler.stop();
        integrationDatabaseHandler.stop();
    }

    private boolean firstRun = true;
    private double accLatitude;
    private double accLongitude;
    private Queue<Location> locationsQueue = new LinkedList<>();// PriorityQueue<Location>();
    private int round = 0;
//    private int accLatitudeCounter = 0;
//    private int accLongitudeCounter = 0;
//    private final int MAX_ROUNDS_FOR_LOCAITON_VALIDITY = 15;
    private final int MIN_NUMBER_OF_SAMPLES = 15;
    private final double ACCURACY_EPSILON = 0.0000001;
    private boolean gatherData = false;
    public void gatherLocation()
    {
        gatherData = true;
    }
    public synchronized void updateLocation(android.location.Location location)
    {
        functionPasser.updateRound(round, location.getAccuracy());
//        Toast.makeText(parentActivity, "accuracy: " + location.getAccuracy(), Toast.LENGTH_SHORT).show();
        if(gatherData && location.getAccuracy() <= MAX_ALLOWED_ACCURACY) {
            // not enough samples yet
            if(round < MIN_NUMBER_OF_SAMPLES) {
                round++;
                accLatitude += location.getLatitude();
                accLongitude += location.getLongitude();
                locationsQueue.add(location);
            } else { // we are on the moving window mode
                // if the current sample correlates with the last ones, then we are good to go
                if (Math.abs(accLatitude / MIN_NUMBER_OF_SAMPLES - location.getLatitude()) < ACCURACY_EPSILON &&
                        Math.abs(accLongitude / MIN_NUMBER_OF_SAMPLES - location.getLongitude()) < ACCURACY_EPSILON) {
                    // zero the helping data
                    round = 0;
                    accLatitude = 0;
                    accLongitude = 0;
                    locationsQueue = new LinkedList<>();
                    AddLocation(location);
                } else {
                    Location oldLocation = locationsQueue.poll();
                    accLatitude += location.getLatitude() - oldLocation.getLatitude();
                    accLongitude += location.getLongitude() - oldLocation.getLongitude();
                    locationsQueue.add(location);
                }
            }
            // TODO: My newest idea is to get all the points until converge, then calculate distance of the middle
            // TODO: from each of them, parse all those distances to integers, count what is the highest one distanced
            // TODO: from them, remove all the groups with higher distance and half the size of the main group
            // TODO: after removing all the (probably) wrong points, calculate the new average and add it as final location.


//            // if it still not ready
//            if(accLatitudeCounter < MAX_ROUNDS_FOR_LOCAITON_VALIDITY || accLongitudeCounter < MAX_ROUNDS_FOR_LOCAITON_VALIDITY) {
//                // if we are bigger than epsilon, we must zero the counter.
//                accLatitudeCounter = (location.getLatitude() - accLatitude) / (round + 1) > ACCURACY_EPSILON ?
//                        0 : accLatitudeCounter + 1;
//                accLongitudeCounter = (location.getLongitude() - accLongitude) / (round + 1) > ACCURACY_EPSILON ?
//                    0: accLongitudeCounter + 1;
//
//                accLatitude = accLatitude + (location.getLatitude() - accLatitude) / (round + 1);
//                accLongitude = accLongitude + (location.getLongitude() - accLongitude) / (round + 1);
//                round++;
//
//                Log.e(GlobalVariables.APPLICATION_TAG, "(" + accLatitude + "," + accLongitude + ") accuracy: " + location.getAccuracy() + " round: " + round);
//            }
//            else {// store this location
//                // zero the helping data
//                accLatitudeCounter = 0;
//                accLongitudeCounter = 0;
//                round = 0;
//
//                AddLocation(location);
//            }


//
//            // check if location is needed to be added to integration
//            List<Coordinate> nearCoordinates =
//                    integrationDatabaseHandler.getNearCoordinates(
//                            (int) ((location.getLatitude() - diffLatitude)*METERS_LOCATION_RATIO),
//                            (int) ((location.getLatitude() + diffLatitude)*METERS_LOCATION_RATIO),
//                            (int) ((location.getLongitude() - diffLongitude)*METERS_LOCATION_RATIO),
//                            (int) ((location.getLongitude() + diffLongitude)*METERS_LOCATION_RATIO)
//                    );
//            //Check if the point should be added to the map
//            if(isNewLocationShouldBeAdded(nearCoordinates, location.getLatitude(),location.getLongitude())) {
//                Log.e(GlobalVariables.APPLICATION_TAG, "added: (" + (int)(location.getLatitude() * METERS_LOCATION_RATIO - firstLatitude) + "," + (int)(location.getLongitude() * METERS_LOCATION_RATIO - firstLongitude) + ") accuracy: " + location.getAccuracy());
//                integrationDatabaseHandler.addCoordinates((int) (location.getLatitude() * METERS_LOCATION_RATIO), (int) (location.getLongitude() * METERS_LOCATION_RATIO));
//                // Add the new routes
//                addRoutes(nearCoordinates, location.getLatitude(),location.getLongitude());
//            }

//            if(GlobalVariables.DEBUG) {
//                Log.e(GlobalVariables.APPLICATION_TAG, "Round: " + ++i + ",\nAccuracy: " + location.getAccuracy() + "\nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + ",\nBearing: " + location.getBearing());
//            }
        }
    }

    private void AddLocation(Location location) {
        if (firstRun) {
            firstLatitude = (int) (location.getLatitude() * METERS_LOCATION_RATIO);
            firstLongitude = (int) (location.getLongitude() * METERS_LOCATION_RATIO);
            diffLatitude = metersToLatitude(MAX_FOR_ROAD_CREATION);
            diffLongitude = metersToLongitude(MAX_FOR_ROAD_CREATION, location.getLatitude());
            firstRun = false;
        }
        // add location to production database
        productionDatabaseHandler.addCoordinates((int) (location.getLatitude() * METERS_LOCATION_RATIO), (int) (location.getLongitude() * METERS_LOCATION_RATIO));

        List<Coordinate> nearCoordinates =
                productionDatabaseHandler.getNearCoordinates(
                        (int) ((location.getLatitude() - diffLatitude)*METERS_LOCATION_RATIO),
                        (int) ((location.getLatitude() + diffLatitude)*METERS_LOCATION_RATIO),
                        (int) ((location.getLongitude() - diffLongitude)*METERS_LOCATION_RATIO),
                        (int) ((location.getLongitude() + diffLongitude)*METERS_LOCATION_RATIO));

        addRoutes(nearCoordinates, location.getLatitude(),location.getLongitude());
        gatherData = false;
        functionPasser.newLocationAdded((int)(location.getLatitude() * METERS_LOCATION_RATIO - firstLatitude) ,(int)(location.getLongitude() * METERS_LOCATION_RATIO - firstLongitude));
    }

    private double metersToLatitude(int meters)
    {
        return meters*0.0111111;
    }

    private double metersToLongitude(int meters, double longitude)
    {
        return meters*0.0111111*Math.cos(longitude);
    }

//    private boolean isNewLocationShouldBeAdded(List<Coordinate> coordinatesList,
//                                               double latitude, double longitude){
//        float[] result = new float[1];
//        for (Coordinate nearCoordinate:
//                coordinatesList) {
//            Location.distanceBetween(
//                    (double) (nearCoordinate.latitude) / METERS_LOCATION_RATIO,
//                    (double) (nearCoordinate.longitude) / METERS_LOCATION_RATIO,
//                    latitude,
//                    longitude,
//                    result
//                    );
//            if(result[0] < MIN_DISTANCE_BETWEEN_TWO_LOCATIONS)
//                return false;
//        }
//        return true;
//    }

    private void addRoutes(List<Coordinate> coordinatesList,
                           double latitude, double longitude){
        float[] result = new float[1];
        for (Coordinate nearCoordinate:
                coordinatesList) {
            Location.distanceBetween(
                    (double) (nearCoordinate.latitude) / METERS_LOCATION_RATIO,
                    (double) (nearCoordinate.longitude) / METERS_LOCATION_RATIO,
                    latitude,
                    longitude,
                    result
            );
            if(result[0] < MAX_FOR_ROAD_CREATION) {
//                routes.add(new Route(nearCoordinate,
//                        new Coordinate((int) (latitude * METERS_LOCATION_RATIO),
//                                (int) (longitude * METERS_LOCATION_RATIO))));

                // send the new route to the gui
                functionPasser.addRoute((int) (latitude * METERS_LOCATION_RATIO) - firstLatitude,
                        (int) (longitude * METERS_LOCATION_RATIO) - firstLongitude,
                        nearCoordinate.latitude - firstLatitude,
                        nearCoordinate.longitude - firstLongitude);
            }
        }
    }

    public interface FunctionPasser{
        /**
         * Called when a new route is needed to be added to ghe gui
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         */
        void addRoute(int x1, int y1, int x2, int y2);

        /**
         * Notifies that a new location is added and it will now stop listening for new coordinates
         * until otherwise mentioned.
         */
        void newLocationAdded(int x, int y);
        void updateRound(int round, float acc);
//        void updateRound(int accLatitudeCounter, int accLongitudeCounter, float acc);
    }
}


