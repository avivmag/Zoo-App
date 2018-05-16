package com.zoovisitors.bl.map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by aviv on 16-Mar-18.
 */

public class DataStructure {
    /**
     * All the lat-lng points of the map
     *
     * @Pre: Must be sorted array by their longitude value (should be done by the server).
     */
    private Point[] points;
    /**
     * all the routes between the points
     */
    private Map<Point, Set<Point>> routes;
    // maximum len on x is around 50, the 30 is estimation for the other road. I better change
    // one day so it will support different latlng/xy ratio
    private final int MAX_DISTANCE_OF_ROUTE_FLAT = 50 * 50;// + 30 * 30; // 0.0000000032
    private final int MAX_DISTANCE_OF_ROUTE = 50*50+30*30;
    private final int MAX_APPROXIMATE_DISTANCE_FROM_POINT = 2*MAX_DISTANCE_OF_ROUTE_FLAT;
    private final int MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT = 2*50;
//    // taking in account the measurement of gps errors (3 times further).
//    private final int MAX_DISTANCE_FROM_POINT = MAX_DISTANCE_OF_ROUTE * MAX_DISTANCE_OF_ROUTE *
//            3;//7 * 0.0111111;
    /**
     * max threashold for update, if last update was before that time, then we need to
     * reinitialize the current location
     * currently set to 2 minutes.
     */
    private final long MAX_UPDATE_THRESHOLD = 7 * 1000;

    private final Location zooEntranceLocation;
    private final Point zooEntrancePoint;
    private Point lastLocation;
    double xLongitudeRatio;
    double yLatitudeRatio;
    private double sinAlpha;
    private double cosAlpha;
    // the borders of the zoo
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    public DataStructure(Point[] points,
                         Location zooEntranceLocation,
                         Point zooEntrancePoint,
                         double xLongitudeRatio,
                         double yLatitudeRatio,
                         double sinAlpha,
                         double cosAlpha,
                         double minLatitude,
                         double maxLatitude,
                         double minLongitude,
                         double maxLongitude
    ) {
        this.points = points;
        this.zooEntranceLocation = zooEntranceLocation;
        this.zooEntrancePoint = zooEntrancePoint;
        this.sinAlpha = sinAlpha;
        this.cosAlpha = cosAlpha;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;

        this.xLongitudeRatio = xLongitudeRatio;
        this.yLatitudeRatio = yLatitudeRatio;
        routes = new HashMap<>();

        for (Point point :
                points) {
            routes.put(point, new HashSet<>());
//            minX = Math.min(minX, point.getX());
//            maxX = Math.max(maxX, point.getX());
//            minY = Math.min(minY, point.getY());
//            maxY = Math.max(maxY, point.getY());
        }

        // generates the routes based on distance that is lower than MAX_DISTANCE_OF_ROUTE
        for (int curr = 0; curr < points.length; curr++) {
            for (int off = curr + 1; off < points.length &&
                    (points[curr].getX() - points[off].getX())*
                            (points[curr].getX() - points[off].getX())
                            < MAX_DISTANCE_OF_ROUTE_FLAT; off++) {
                if (squaredDistance(points[curr], points[off]) < MAX_DISTANCE_OF_ROUTE) {
                    routes.get(points[curr]).add(points[off]);
                    routes.get(points[off]).add(points[curr]);
                }
            }
        }

//        // TODO: only for testing comment this
        Map.Entry<Point, Set<Point>> lowest = null, biggest = null;
        int lowest_counter = 0, biggest_counter = 0;
        int low = 10000,big = 0;
        for (Map.Entry<Point, Set<Point>> entry:
             routes.entrySet()) {
            if(entry.getValue().size() > big)
            {
                big = entry.getValue().size();
                biggest = entry;
                biggest_counter = 1;
            } else if(entry.getValue().size() == big) {
                biggest_counter++;
            }
            if(entry.getValue().size() < low)
            {
                low = entry.getValue().size();
                lowest = entry;
                lowest_counter = 1;
            } else if(entry.getValue().size() == low) {
                lowest_counter++;
            }
            if(entry.getValue().size() == 2) {
                Log.e("AVIV", "Point " + entry.getKey());
            }
        }
        Log.e("AVIV", "Biggest: " + biggest);
        Log.e("AVIV", "Smallest: " + lowest);
        Log.e("AVIV", "Biggest: " + biggest_counter);
        Log.e("AVIV", "Lowest_counter: " + lowest_counter);
    }

    private long lastUpdate = 0;

    public Point getOnMapPosition(Point point) {

        Point ans;
        // new update is needed
        if (System.currentTimeMillis() - lastUpdate > MAX_UPDATE_THRESHOLD) {
            ans = getOnMapPositionFirstTime(point);
        } else {
            ans = getOnMapPositionContinues(point);
        }
        if (ans == null)
            return null;
        lastUpdate = System.currentTimeMillis();
        return ans;
    }

    /**
     * Should be used when there is at least one known location that is known to be near.
     *
     * @param estimatedPoint
     * @return two points: the one that we are estimating it to be + the one that was the closest
     * to the given point (should be used to call getOnMapPositionContinues)
     */
    private Point getOnMapPositionContinues(Point estimatedPoint) {
        double distanceToNearest = Double.MAX_VALUE;
        Point nearestFromTheSet = null;
        for (Point p : routes.get(lastLocation)) {
            int curDistance = squaredDistance(estimatedPoint, p);
            if (curDistance < distanceToNearest) {
                distanceToNearest = curDistance;
                nearestFromTheSet = p;
            }
        }

        // in case the new point is closer than the older that was given
        if (distanceToNearest < squaredDistance(estimatedPoint, lastLocation)) {
            lastLocation = nearestFromTheSet;
            return getOnMapPositionContinues(estimatedPoint);
        }

        if (distanceToNearest > MAX_APPROXIMATE_DISTANCE_FROM_POINT)
            return null;

        // find the nearest point
        return getPointOnLineThatIsClosestToOutsidePoint(estimatedPoint, lastLocation,
                nearestFromTheSet);
    }

    /**
     * This algorithm may take some time, it should be used when trying to get the position in a
     * first time use (like opening the map).
     *
     * @param point
     * @return two points: the one that we are estimating it to be + the one that was the closest
     * to the given point (should be used to call getOnMapPositionContinues)
     * returns null if the the point is far from any of the points on the graph
     */
    private Point getOnMapPositionFirstTime(Point point) {
        // reduce number of checks
        int[] indexRange = getIndexRangeInPoints(point.getX());
        if (indexRange == null)
            return null;

        Point nearest = findNearestLocation(point, indexRange);
        if (nearest == null)
            return null;

        // find second Nearest point
        Point secNearest = findSecondNearestLocation(point, nearest);
        if (secNearest == null)
            return null;

        lastLocation = nearest;
        // find the nearest point
        return getPointOnLineThatIsClosestToOutsidePoint(point, nearest, secNearest);
    }

    private Point findNearestLocation(Point point, int[] indexRange) {
        Point nearest = null;
        double distanceToNearest = Double.MAX_VALUE;
        for (int i = indexRange[0]; i <= indexRange[1]; i++) {
            int curDistance = squaredDistance(point, points[i]);
            if (curDistance < distanceToNearest && curDistance <= MAX_APPROXIMATE_DISTANCE_FROM_POINT) {
                distanceToNearest = curDistance;
                nearest = points[i];
            }
        }
        return nearest;
    }

    private Point findSecondNearestLocation(Point point, Point nearest) {
        Point secNearest = null;
        double distance = Double.MAX_VALUE;
        for (Point p : routes.get(nearest)) {
            int curDistance = squaredDistance(point, p);
            if (curDistance < distance) {
                distance = curDistance;
                secNearest = p;
            }
        }
        return secNearest;
    }

    private int squaredDistance(Point a, Point b) {
        return (a.getX() - b.getX())*(a.getX() - b.getX())
                + (a.getY() - b.getY())*(a.getY() - b.getY());
    }

    /**
     * Returns the most left and right points that is in the range between two points,
     * Returns null if there is no one in the range
     *
     * @return
     */
    private int[] getIndexRangeInPoints(int x) {
        int[] range = new int[2];
        int bottom = 0, top = points.length - 1;
        while (top - bottom > 1) {
            range[0] = (bottom + top) / 2;
            if (x - MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT < points[range[0]].getX()) {
                top = range[0];
            } else {
                bottom = range[0];
            }
        }
        range[0] = points[bottom].getX() >= x - MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT ? bottom : top;

        bottom = 0;
        top = points.length - 1;
        while (top - bottom > 1) {
            range[1] = (bottom + top) / 2;
            if (x + MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT > points[range[1]].getX()) {
                bottom = range[1];
            } else {
                top = range[1];
            }
        }
        range[1] = points[top].getX() <= x + MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT ? top : bottom;

        return range;
    }

    private Point getPointOnLineThatIsClosestToOutsidePoint(Point a, Point b, Point c) {
        double xb_xcIyb_yc = (b.getX() - c.getX()) * (b.getY() - c.getY());
        double Sxb_xcS = Math.pow(b.getX() - c.getX(), 2);
        double Syb_ycS = Math.pow(b.getY() - c.getY(), 2);
        double Sxb_xcSPSyb_ycS = Sxb_xcS + Syb_ycS;

        Point ans = new Point((int)
                ((xb_xcIyb_yc * (a.getY() - b.getY()) + Sxb_xcS * a.getX() + Syb_ycS * b.getX())
                        / Sxb_xcSPSyb_ycS),
                (int) ((xb_xcIyb_yc * (a.getX() - b.getX()) + Syb_ycS * a.getY() + Sxb_xcS * b
                        .getY())
                        / Sxb_xcSPSyb_ycS));

        if ((ans.getX() < b.getX() && ans.getX() < c.getX()) || (ans.getY() < b.getY() && ans
                .getY() < c.getY())
                || (ans.getX() > b.getX() && ans.getX() > c.getX()) || (ans.getY() > b.getY() &&
                ans.getY() > c.getY())) {
            ans = squaredDistance(ans, b) < squaredDistance(ans, c) ? b : c;
        }
        return ans;
    }

    public Point locationToPoint(Location location) {
        Location locationCenteredToEntranceReversed =
                calibrateLocationToEntranceAndReverseLongitudeAxis(location);
        Location turnedLocation = rotateLocationAroundEntrance(locationCenteredToEntranceReversed);
        Location locationCenteredAndRatioed = scaleLocationToPoints(turnedLocation);

        return new Point((int) locationCenteredAndRatioed.getLongitude() + zooEntrancePoint.getX(),
                (int) locationCenteredAndRatioed.getLatitude() + zooEntrancePoint.getY());
    }

    @NonNull
    private Location calibrateLocationToEntranceAndReverseLongitudeAxis(Location location) {
        // calibrate the position of the location based on the entrance
        Location locationCenteredToEntrance = new Location(location.getLatitude() -
                zooEntranceLocation.getLatitude(),
                location.getLongitude() - zooEntranceLocation.getLongitude());

        // reverse the latitude axis, so both the before and after would have the same axis
        return new Location(
                -locationCenteredToEntrance.getLatitude(),
                locationCenteredToEntrance.getLongitude()
        );
    }

    @NonNull
    private Location scaleLocationToPoints(Location locationCenteredToEntranceReversed) {
        // after, it should be centered, with point equaled values
        return new Location(yLatitudeRatio * locationCenteredToEntranceReversed.getLatitude(),
                xLongitudeRatio * locationCenteredToEntranceReversed.getLongitude());
    }

    @NonNull
    private Location rotateLocationAroundEntrance(Location locationCenteredAndRatioed) {
        // turn it around
//        return locationCenteredAndRatioed;
        return new Location(
                locationCenteredAndRatioed.getLatitude() * cosAlpha
                        + locationCenteredAndRatioed.getLongitude() * sinAlpha,
                locationCenteredAndRatioed.getLongitude() * cosAlpha
                        - locationCenteredAndRatioed.getLatitude() * sinAlpha
        );
    }
}
