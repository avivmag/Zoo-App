package com.zoovisitors.bl.map;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;

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
    private final int MAX_DISTANCE_OF_ROUTE_FLAT = 50 * 50;
    private final int MAX_APPROXIMATE_DISTANCE_FROM_POINT = 2 * MAX_DISTANCE_OF_ROUTE_FLAT;
    private final int MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT = 2 * 50;
    //                                                                           10 meters
    private final double MAX_MARGIN_OF_MAP_ROADS = GlobalVariables.DEBUG ? 100 : 10 * 0.000009;
    // The number of points for the enclosure
    private final int NUMBER_OF_POINTS_SHOULD_BE_BFS = 10;
    /**
     * max threashold for update, if last update was before that time, then we need to
     * reinitialize the current location
     */
    private final long MAX_UPDATE_THRESHOLD = 7 * 1000;

    private long lastUpdate = 0;
    private Set<Animal.PersonalStories> unwantedStories = new HashSet<>();
    private final Location zooEntranceLocation;
    private final Point zooEntrancePoint;
    private Point lastPoint;
    private double xLongitudeRatio;
    private double yLatitudeRatio;
    private double sinAlpha;
    private double cosAlpha;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    public DataStructure(Point[] points,
                         Map<Point, Set<Point>> routes,
                         Location zooEntranceLocation,
                         Point zooEntrancePoint,
                         double xLongitudeRatio,
                         double yLatitudeRatio,
                         double sinAlpha,
                         double cosAlpha,
                         double minLatitude,
                         double maxLatitude,
                         double minLongitude,
                         double maxLongitude,
                         Pair<Integer, Enclosure>[] enclosures,
                         Animal.PersonalStories[] animalStories
    ) {
        this.points = points;
        this.routes = routes;
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

        addAnimalStoriesToPoints(enclosures, animalStories);
    }

    public boolean IsInPark(Location location) {
        return location.getLatitude() >= minLatitude - MAX_MARGIN_OF_MAP_ROADS &&
                location.getLatitude() <= maxLatitude + MAX_MARGIN_OF_MAP_ROADS &&
                location.getLongitude() >= minLongitude - MAX_MARGIN_OF_MAP_ROADS &&
                location.getLongitude() <= maxLongitude + MAX_MARGIN_OF_MAP_ROADS;
    }

    public Point getOnMapPosition(Location location) {
        Point ans;
        Point point = locationToPoint(location);
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
     * This algorithm may take some time, it should be used when trying to get the position in a
     * first time use (like opening the map).
     *
     * @param point
     * @return two points: the one that we are estimating it to be + the one that was the closest
     * to the given point (should be used to call getOnMapPositionContinuesAndClosestPoint)
     * returns null if the the point is far from any of the points on the graph
     */
    private Point getOnMapPositionFirstTime(Point point) {
        // reduce number of checks
        int[] indexRange = getIndexRangeInPoints(point.getX());
        if (indexRange == null)
            return null;

        Point nearest = findNearestPoint(point, indexRange);
        if (nearest == null)
            return null;

        // find second Nearest point
        Point secNearest = findSecondNearestPoint(point, nearest);
        if (secNearest == null)
            return null;

        lastPoint = nearest;
        // find the nearest point
        return getPointOnLineThatIsClosestToOutsidePoint(point, nearest, secNearest);
    }

    /**
     * Should be used when there is at least one point that is known to be near.
     *
     * @param estimatedPoint
     * @return two points: the one that we are estimating it to be + the one that was the closest
     * to the given point (should be used to call getOnMapPositionContinuesAndClosestPoint)
     */
    private Point getOnMapPositionContinues(Point estimatedPoint) {
        double distanceToNearest = Double.MAX_VALUE;
        Point nearestFromTheSet = null;
        for (Point p : routes.get(lastPoint)) {
            int curDistance = squaredDistance(estimatedPoint, p);
            if (curDistance < distanceToNearest) {
                distanceToNearest = curDistance;
                nearestFromTheSet = p;
            }
        }

        // in case the new point is closer than the older that was given
        if (distanceToNearest < squaredDistance(estimatedPoint, lastPoint)) {
            lastPoint = nearestFromTheSet;
            return getOnMapPositionContinues(estimatedPoint);
        }

        if (distanceToNearest > MAX_APPROXIMATE_DISTANCE_FROM_POINT)
            return null;

        // find the nearest point
        return getPointOnLineThatIsClosestToOutsidePoint(estimatedPoint,
                lastPoint, nearestFromTheSet);
    }

    private Point findNearestPoint(Point point, int[] indexRange) {
        Point nearest = null;
        double distanceToNearest = Double.MAX_VALUE;
        for (int i = indexRange[0]; i <= indexRange[1]; i++) {
            int curDistance = squaredDistance(point, points[i]);
            if (curDistance < distanceToNearest && curDistance <=
                    MAX_APPROXIMATE_DISTANCE_FROM_POINT) {
                distanceToNearest = curDistance;
                nearest = points[i];
            }
        }
        return nearest;
    }

    private Point findSecondNearestPoint(Point point, Point nearest) {
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
        return (a.getX() - b.getX()) * (a.getX() - b.getX())
                + (a.getY() - b.getY()) * (a.getY() - b.getY());
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
        range[0] = points[bottom].getX() >= x - MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT ? bottom
                : top;
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
        range[1] = points[top].getX() <= x + MAX_APPROXIMATE_DISTANCE_FROM_POINT_FLAT ? top :
                bottom;
        return range;
    }

    private Point getPointOnLineThatIsClosestToOutsidePoint(Point estimatedPoint, Point
            endPoint1, Point endPoint2) {
        double endPointsXsDifferenceDivideByEndPointsYsDifference = (endPoint1.getX() -
                endPoint2.getX()) * (endPoint1.getY() - endPoint2.getY());
        double squaredEndPointsXsDifference = Math.pow(endPoint1.getX() - endPoint2.getX(), 2);
        double squaredEndPointsYsDifference = Math.pow(endPoint1.getY() - endPoint2.getY(), 2);
        double squaredDistance = squaredEndPointsXsDifference + squaredEndPointsYsDifference;

        Point ans = new Point((int) ((endPointsXsDifferenceDivideByEndPointsYsDifference *
                (estimatedPoint.getY() - endPoint1.getY()) + squaredEndPointsXsDifference *
                estimatedPoint.getX() + squaredEndPointsYsDifference * endPoint1.getX()) /
                squaredDistance), (int) ((endPointsXsDifferenceDivideByEndPointsYsDifference *
                (estimatedPoint.getX() - endPoint1.getX()) + squaredEndPointsYsDifference *
                estimatedPoint.getY() + squaredEndPointsXsDifference * endPoint1.getY()) /
                squaredDistance));

        // deal with the case that the new point is on the line but not between them
        if ((ans.getX() < endPoint1.getX() && ans.getX() < endPoint2.getX()) || (ans.getY() <
                endPoint1.getY() && ans.getY() < endPoint2.getY()) || (ans.getX() >
                endPoint1.getX() && ans.getX() > endPoint2.getX()) || (ans.getY() >
                endPoint1.getY() && ans.getY() > endPoint2.getY())) {
            ans = squaredDistance(ans, endPoint1) < squaredDistance(ans, endPoint2) ? endPoint1 :
                    endPoint2;
        }
        return ans;
    }

    private Point locationToPoint(Location location) {
        Location locationCenteredToEntranceReversed =
                calibrateLocationToEntranceAndReverseLongitudeAxis(location);
        Location turnedLocation = rotateLocationAroundEntrance(locationCenteredToEntranceReversed);
        Point pointCenteredAndRatioed = scaleLocationToPoints(turnedLocation);

        return new Point((int) pointCenteredAndRatioed.getX() + zooEntrancePoint.getX(),
                (int) pointCenteredAndRatioed.getY() + zooEntrancePoint.getY());
    }

    @NonNull
    private Location calibrateLocationToEntranceAndReverseLongitudeAxis(Location location) {
        // calibrate the position of the location based on the entrance
        Location locationCenteredToEntrance = new Location(
                location.getLatitude() - zooEntranceLocation.getLatitude(),
                location.getLongitude() - zooEntranceLocation.getLongitude());

        // reverse the latitude axis, so both the before and after would have the same axis
        return new Location(
                -locationCenteredToEntrance.getLatitude(),
                locationCenteredToEntrance.getLongitude()
        );
    }

    @NonNull
    private Point scaleLocationToPoints(Location locationCenteredToEntranceReversed) {
        // after, it should be centered, with point equaled values
        return new Point((int) (xLongitudeRatio * locationCenteredToEntranceReversed.getLongitude
                ()),
                (int) (yLatitudeRatio * locationCenteredToEntranceReversed.getLatitude()));
    }

    @NonNull
    private Location rotateLocationAroundEntrance(Location locationCenteredAndRatioed) {
        // turn it around
        return new Location(
                locationCenteredAndRatioed.getLatitude() * cosAlpha
                        + locationCenteredAndRatioed.getLongitude() * sinAlpha,
                locationCenteredAndRatioed.getLongitude() * cosAlpha
                        - locationCenteredAndRatioed.getLatitude() * sinAlpha
        );
    }

    private void addAnimalStoriesToPoints(Pair<Integer, Enclosure>[] enclosures, Animal
            .PersonalStories[] animalStories) {
        for (Pair<Integer, Enclosure> enclosure :
                enclosures) {
            for (Animal.PersonalStories animalStory :
                    animalStories) {
                if (enclosure.second.getId() == animalStory.getEncId()) {
                    addAnimalStoryToPoints(enclosure.second, animalStory);
                }
            }
        }
    }

    public Animal.PersonalStories getNextAnimalStory() {
        Set<Animal.PersonalStories> animalStories = lastPoint.getClosestAnimalStories();
        animalStories.removeAll(unwantedStories);

        if (animalStories.isEmpty()) {
            return null;
        }
        return animalStories.toArray(new Animal.PersonalStories[animalStories.size()])
                [(int) (Math.random() * animalStories.size())];
    }

    public void removeAnimalStory(Animal.PersonalStories as) {
        unwantedStories.add(as);
    }

    private void addAnimalStoryToPoints(Enclosure enclosure, Animal.PersonalStories animalStory) {
        Point onMapPoint = getPointInstanceByXY(enclosure.getClosestPointX(), enclosure
                .getClosestPointY());
        if (onMapPoint != null) {
            addAnimalStoryToPointByBFS(onMapPoint, animalStory);
        }
    }

    /**
     * Returns an instance from the points array that has the x and y values
     * precondition: the point should be in points array, otherwise will return null.
     *
     * @param x
     * @param y
     * @return
     */
    private Point getPointInstanceByXY(int x, int y) {
        int mid, bottom = 0, top = points.length - 1;
        while (top - bottom > 1) {
            mid = (bottom + top) / 2;
            if (x < points[mid].getX()) {
                top = mid;
            } else {
                bottom = mid;
            }
        }
        // finding the point
        int curr = bottom;
        while (curr < points.length && points[curr].getX() == x) {
            if (points[curr].getY() == y)
                return points[curr];
            curr++;
        }
        curr = bottom - 1;
        while (curr >= 0 && points[curr].getX() == x) {
            if (points[curr].getY() == y)
                return points[curr];
            curr--;
        }
        // should never happen
        return null;
    }

    private void addAnimalStoryToPointByBFS(Point point, Animal.PersonalStories animalStory) {
        addAnimalStoryToPointByBFS(point, animalStory, new HashSet<>(),
                NUMBER_OF_POINTS_SHOULD_BE_BFS);
    }

    private void addAnimalStoryToPointByBFS(Point point, Animal.PersonalStories
            animalStory, Set<Point> checkedPoints, int depth) {
        point.addCloseAnimalStory(animalStory);
        checkedPoints.add(point);
        if (depth == 0)
            return;
        for (Point connectedPoint :
                routes.get(point)) {
            if (!checkedPoints.contains(connectedPoint)) {
                addAnimalStoryToPointByBFS(connectedPoint, animalStory, checkedPoints, depth - 1);
            }
        }
    }
}