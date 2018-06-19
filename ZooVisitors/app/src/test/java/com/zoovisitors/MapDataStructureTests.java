package com.zoovisitors;

import android.util.Pair;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.bl.map.DataStructure;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapDataStructureTests {
    private static final double MAX_MARGIN_OF_MAP_ROADS = 10 * 0.0111111;

    // rectangle shaped roads
    private static Point[] points;
    private static Location zooEntranceLocation;
    private static Point zooEntrancePoint;
    private static double xLongitudeRatio;
    private static double yLatitudeRatio;
    private static double sinAlpha;
    private static double cosAlpha;
    private static DataStructure ds;

    @BeforeClass
    public static void beforeClass() {
        int len = 49;
        List<Point> pointList = new ArrayList<>();
        // create some kind of rectangle
        for (int i = 1; i < 9; i++) {
            pointList.add(new Point(i * len, 0));
            pointList.add(new Point(0, i * len));
            pointList.add(new Point(i * len, 9 * len));
            pointList.add(new Point(9 * len, i * len));
        }
        pointList.add(new Point(0, 0));
        pointList.add(new Point(0, 9 * len));
        pointList.add(new Point(9 * len, 0));
        pointList.add(new Point(9 * len, 9 * len));

        // another interesting point
        pointList.add(new Point(25, 25));

        points = new Point[pointList.size()];
        pointList.toArray(points);
        Arrays.sort(points, (p1, p2) -> p1.getX() - p2.getX());
        zooEntranceLocation = new Location(32, 34);
        zooEntrancePoint = new Point(0,0);
        xLongitudeRatio = 50/0.0001;
        yLatitudeRatio = 50/0.0001;
        // alpha = 45 degrees
        sinAlpha = Math.sqrt(2);
        cosAlpha = Math.sqrt(2);
        Arrays.toString(points);


        Map<Point, Set<Point>> routes = new HashMap<>();

//        for (Point point :
//                points) {
//            routes.put(point, new HashSet<>());
//        }
//
//        // generates the routes based on distance that is lower than MAX_DISTANCE_OF_ROUTE
//        for (int curr = 0; curr < points.length; curr++) {
//            for (int off = curr + 1; off < points.length &&
//                    (points[curr].getX() - points[off].getX()) *
//                            (points[curr].getX() - points[off].getX())
//                            < MAX_DISTANCE_OF_ROUTE_FLAT; off++) {
//                if (squaredDistance(points[curr], points[off]) < MAX_DISTANCE_OF_ROUTE) {
//                    routes.get(points[curr]).add(points[off]);
//                    routes.get(points[off]).add(points[curr]);
//                }
//            }
//        }


        ds = new DataStructure(
                points,
                null, // TODO: Create routes
                zooEntranceLocation,
                zooEntrancePoint,
                xLongitudeRatio,
                yLatitudeRatio,
                sinAlpha,
                cosAlpha,
                31.5,
                32.5,
                33.5,
                34.5,
                new Pair[0],
                new Animal.PersonalStories[0]
                );
    }

    @Test
    public void IsInPark() {
        assertTrue(ds.IsInPark(new Location(31.5, 33.5)));
        assertTrue(ds.IsInPark(new Location(32.5, 33.5)));
        assertTrue(ds.IsInPark(new Location(31.5, 34.5)));
        assertTrue(ds.IsInPark(new Location(32.5, 34.5)));
        assertTrue(ds.IsInPark(new Location(31.5 - MAX_MARGIN_OF_MAP_ROADS, 33.5 - MAX_MARGIN_OF_MAP_ROADS)));
        assertTrue(ds.IsInPark(new Location(32.5 + MAX_MARGIN_OF_MAP_ROADS, 34.5 + MAX_MARGIN_OF_MAP_ROADS)));
        assertFalse(ds.IsInPark(new Location(32.5, 34.5 + MAX_MARGIN_OF_MAP_ROADS * 1.01)));
        assertFalse(ds.IsInPark(new Location(31.5 - MAX_MARGIN_OF_MAP_ROADS * 1.01, 34.5)));
    }

    @Test
    public void getOnMapPosition() {
        assertEquals(new Point(0,0), ds.getOnMapPosition(new Location(32, 34)));
        assertEquals(new Point(25,25), ds.getOnMapPosition(new Location(32, 34.000070710678118654752440084436)));
        assertEquals(new Point(0,99), ds.getOnMapPosition(new Location(31.999929289321881345247559915564, 34.000070710678118654752440084436)));
        assertEquals(new Point(99,0), ds.getOnMapPosition(new Location(32.000070710678118654752440084436, 34.000070710678118654752440084436)));
    }
}