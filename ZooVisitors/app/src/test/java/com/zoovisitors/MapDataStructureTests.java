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
import java.util.HashSet;
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
        int len = 50;
        List<Point> pointList = new ArrayList<>();
        Map<Point, Set<Point>> routes = new HashMap<>();
        // create some kind of rectangle
        pointList.add(new Point(0, 0));
        pointList.add(new Point(0, 9 * len));
        pointList.add(new Point(9 * len, 0));
        pointList.add(new Point(9 * len, 9 * len));
        routes.put(new Point(0, 0), new HashSet<>());
        routes.put(new Point(0, 9 * len), new HashSet<>());
        routes.put(new Point(9 * len, 0), new HashSet<>());
        routes.put(new Point(9 * len,9 * len), new HashSet<>());
        for (int i = 1; i < 9; i++) {
            pointList.add(new Point(i * len, 0));
            pointList.add(new Point(0, i * len));
            pointList.add(new Point(i * len, 9 * len));
            pointList.add(new Point(9 * len, i * len));

            routes.put(new Point(i * len, 0), new HashSet<>());
            routes.put(new Point(0, i * len), new HashSet<>());
            routes.put(new Point(i * len, 9 * len), new HashSet<>());
            routes.put(new Point(9 * len,i * len), new HashSet<>());

            routes.get(new Point(i * len, 0)).add(new Point((i-1) * len, 0));
            routes.get(new Point(0, i * len)).add(new Point(0, (i-1) * len));
            routes.get(new Point(i * len, 9 * len)).add(new Point((i-1) * len, 9 * len));
            routes.get(new Point(9 * len, i * len)).add(new Point(9 * len, (i-1) * len));

            routes.get(new Point((i-1) * len, 0)).add(new Point(i * len, 0));
            routes.get(new Point(0, (i-1) * len)).add(new Point(0, i * len));
            routes.get(new Point((i-1) * len, 9 * len)).add(new Point(i * len, 9 * len));
            routes.get(new Point(9 * len, (i-1) * len)).add(new Point(9 * len, i * len));
        }
        // another interesting point
        pointList.add(new Point(25,25));
        routes.put(new Point(25,25), new HashSet<>());
        routes.get(new Point(0,0)).add(new Point(25,25));
        routes.get(new Point(25,25)).add(new Point(0,0));
        routes.get(new Point(50,0)).add(new Point(25,25));
        routes.get(new Point(25,25)).add(new Point(50,0));
        routes.get(new Point(0,50)).add(new Point(25,25));
        routes.get(new Point(25,25)).add(new Point(0,50));

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

        ds = new DataStructure(
                points,
                routes,
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