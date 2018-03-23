package com.zoovisitors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.zoovisitors.backend.map.Location;
import com.zoovisitors.bl.map.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by aviv on 16-Mar-18.
 */

public class BlMapTests {
//    static double MAX_DISTANCE_FROM_POINT = 7 * 0.0111111;
//    static DataStructure mapDS;
//    static Method getIndexRangeInLocations;
//    static Method getLocationOnLineThatIsClosestToOutsideLocation;
//
//    @BeforeClass
//    public static void beforeClass()
//    {
//        try {
//            getIndexRangeInLocations = DataStructure.class.getDeclaredMethod("getIndexRangeInLocations", double.class);
//            getLocationOnLineThatIsClosestToOutsideLocation =
//                    DataStructure.class.getDeclaredMethod(
//                            "getLocationOnLineThatIsClosestToOutsideLocation",
//                            Location.class,
//                            Location.class,
//                            Location.class);
//        } catch (NoSuchMethodException e) { }
//        getIndexRangeInLocations.setAccessible(true);
//        getLocationOnLineThatIsClosestToOutsideLocation.setAccessible(true);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations0() {
//        Location[] locations = {
//                new Location(31.25937952,34.74386091),
//                new Location(31.25940547,34.74388087),
//                new Location(31.25943142,34.74390085),
//                new Location(31.25945737,34.74392087),
//                new Location(31.25948332,34.74394107),
//                new Location(31.25950927,34.74396166),
//                new Location(31.25953522,34.74398301),
//                new Location(31.25956117,34.74400561),
//                new Location(31.25958712,34.74403012),
//                new Location(31.25961307,34.74405736),
//                new Location(31.25963902,34.74408834),
//                new Location(31.25966497,34.74412427),
//                new Location(31.25969092,34.74416655),
//                new Location(31.25971687,34.74421684),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, 31.25956117);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{0, locations.length - 1}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations1() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, MAX_DISTANCE_FROM_POINT * 3);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{2, 4}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations2() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0/2,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, MAX_DISTANCE_FROM_POINT * 3);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{4, 6}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations3() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0/2,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, MAX_DISTANCE_FROM_POINT * 3);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{4, 6}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations5() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0/2,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, -MAX_DISTANCE_FROM_POINT );
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{0,0}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations6() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 7.0/2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 8.0/2,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, MAX_DISTANCE_FROM_POINT * 4.5/2);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{3,6}, range);
//    }
//
//    @Test
//    public void testGetIndexRangeInLocations7() {
//        Location[] locations = {
//                new Location(MAX_DISTANCE_FROM_POINT * 0.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 1.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 2.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 3.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 4.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 5.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 6.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 7.0*2,0.0),
//                new Location(MAX_DISTANCE_FROM_POINT * 8.0*2,0.0),
//        };
//        DataStructure mapDS = new DataStructure(locations, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        int[] range = null;
//        try {
//            range = (int[]) getIndexRangeInLocations.invoke(mapDS, MAX_DISTANCE_FROM_POINT * 2*2);
//        } catch (Exception e) {}
//        assertArrayEquals(new int[]{2,2}, range);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation0() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(0.0,2.5),
//                    new Location(2.0,1.0),
//                    new Location(0.0,0.0));
//        } catch (Exception e) { }
//        assertEquals(new Location(1.0,0.5), o);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation1() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(0.0,2.5),
//                    new Location(2.0,1.0),
//                    new Location(0.0,0.0));
//        } catch (Exception e) { }
//        assertEquals(new Location(1.0,0.5), o);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation2() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(9.0,3.0),
//                    new Location(7.0,5.0),
//                    new Location(2.0,1.0));
//        } catch (Exception e) { }
//        assertEquals(297.0/41, o.getLatitude(), 0.0000001);
//        assertEquals(213.0/41, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation3() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(1.0,3.0),
//                    new Location(2.0,6.0),
//                    new Location(3.0,9.0));
//        } catch (Exception e) { }
//        assertEquals(1.0, o.getLatitude(), 0.0000001);
//        assertEquals(3.0, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation4() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(3.0,2.0),
//                    new Location(1.0,1.0),
//                    new Location(2.0,1.0));
//        } catch (Exception e) { }
//        assertEquals(3.0, o.getLatitude(), 0.0000001);
//        assertEquals(1.0, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation5() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(2.0,3.0),
//                    new Location(1.0,1.0),
//                    new Location(1.0,2.0));
//        } catch (Exception e) { }
//        assertEquals(1.0, o.getLatitude(), 0.0000001);
//        assertEquals(3.0, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation6() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(0.0,0.0),
//                    new Location(1.0,1.0),
//                    new Location(2.0,1.0));
//        } catch (Exception e) { }
//        assertEquals(0.0, o.getLatitude(), 0.0000001);
//        assertEquals(1.0, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation7() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(1.0,0.0),
//                    new Location(0.0,0.0),
//                    new Location(2.0,2.0));
//        } catch (Exception e) { }
//        assertEquals(0.5, o.getLatitude(), 0.0000001);
//        assertEquals(0.5, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation8() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(1.0,0.0),
//                    new Location(2.0,2.0),
//                    new Location(0.0,0.0));
//        } catch (Exception e) { }
//        assertEquals(0.5, o.getLatitude(), 0.0000001);
//        assertEquals(0.5, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation9() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);;
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(1.0,0.0),
//                    new Location(1.0,1.0),
//                    new Location(1.0,1.0));
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        assertEquals(Double.NaN, o.getLatitude(), 0.0000001);
//        assertEquals(Double.NaN, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getLocationOnLineThatIsClosestToOutsideLocation10() {
//        DataStructure mapDS = new DataStructure(new Location[]{}, Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location o = null;
//        try {
//            o = (Location) getLocationOnLineThatIsClosestToOutsideLocation.invoke(mapDS,
//                    new Location(1.0,0.0),
//                    new Location(1.0,0.0),
//                    new Location(1.0,1.0));
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//
//        assertEquals(1.0, o.getLatitude(), 0.0000001);
//        assertEquals(0.0, o.getLongitude(), 0.0000001);
//    }
//
//    @Test
//    public void getOnMapPositionFirstTime() {
//        DataStructure mapDS = new DataStructure(Dummy.getLocations(), Dummy.ZOO_ENTRANCE_LOCATION, Dummy.ZOO_EXIT_LOCATION, Dummy.ZOO_ENTRANCE_POINT, Dummy.ZOO_EXIT_POINT);
//        Location p = mapDS.getOnMapPosition(
//                new Location(31.25873889, 34.74429022));
//        assertEquals(31.2587235, p.getLatitude(), 0.0000001);
//        assertEquals(34.74429978, p.getLongitude(), 0.0000001);
//    }

    private final int MAX_DISTANCE_OF_ROUTE = 35;
    private final int MAX_DISTANCE_FROM_POINT = MAX_DISTANCE_OF_ROUTE * 3;
    static DataStructure mapDS;
    static Method getIndexRangeInLocations;
    static Method getLocationOnLineThatIsClosestToOutsideLocation;
    static Method calibrateLocationToEntranceAndReverseLongitudeAxis;
    static Method scaleLocationToPoints;
    static Method rotateLocationAroundEntrance;

    @BeforeClass
    public static void beforeClass()
    {
        try {
            rotateLocationAroundEntrance =
                    DataStructure.class.getDeclaredMethod(
                            "rotateLocationAroundEntrance",
                            Location.class);
            calibrateLocationToEntranceAndReverseLongitudeAxis =
                    DataStructure.class.getDeclaredMethod(
                            "calibrateLocationToEntranceAndReverseLongitudeAxis",
                            Location.class);
            scaleLocationToPoints =
                    DataStructure.class.getDeclaredMethod(
                            "scaleLocationToPoints",
                            Location.class);
        } catch (NoSuchMethodException e) { }
        calibrateLocationToEntranceAndReverseLongitudeAxis.setAccessible(true);
        rotateLocationAroundEntrance.setAccessible(true);
        scaleLocationToPoints.setAccessible(true);
        mapDS = new DataStructure(Dummy.getPoints(), Dummy.ZOO_ENTRANCE_LOCATION, Dummy.getXLongitudeRatio(),Dummy.getYLatitudeRatio(),
                Dummy.getSinAlpha(), Dummy.getCosAlpha());
    }

    @Test
    public void calibrateLocationToEntranceAndReverseLongitudeAxis0() {
        Location o = null;
        try {
            o = (Location) calibrateLocationToEntranceAndReverseLongitudeAxis.invoke(mapDS,
                    Dummy.ZOO_ENTRANCE_LOCATION);
        } catch (Exception e) { }
        assertEquals(0, o.getLatitude(), 0.0000001);
        assertEquals(0, o.getLongitude(), 0.0000001);
    }

    @Test
    public void calibrateLocationToEntranceAndReverseLongitudeAxis1() {
        Location o = null;
        try {
            o = (Location) calibrateLocationToEntranceAndReverseLongitudeAxis.invoke(mapDS,
                    new Location(Dummy.ZOO_ENTRANCE_LOCATION.getLatitude() + 2,
                            Dummy.ZOO_ENTRANCE_LOCATION.getLongitude() + 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(-2, o.getLatitude(), 0.0000001);
        assertEquals(1, o.getLongitude(), 0.0000001);
    }

    @Test
    public void scaleLocationToPoints0() {
        // 3 4 5
        // 6 8 10
        DataStructure mapDS = new DataStructure(Dummy.getPoints(),
                new Location(0.0, 0.0),
                2,
                2,
                -1, -1);

        Location o = null;
        try {
            o = (Location) scaleLocationToPoints.invoke(mapDS,
                    new Location(3,4));
        } catch (Exception e) { }
        assertEquals(6, o.getLatitude(), 0.0000001);
        assertEquals(8, o.getLongitude(), 0.0000001);
    }

    @Test
    public void rotateLocationAroundEntrance0() {
        DataStructure mapDS = new DataStructure(Dummy.getPoints(),
                new Location(0.0, 0.0),
                2,
                2,
                0.86602540378, 0.5);

        Location o = null;
        try {
            o = (Location) rotateLocationAroundEntrance.invoke(mapDS,
                    new Location(Math.sqrt(12),2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(o);
        assertEquals(Math.sqrt(12), o.getLatitude(), 0.0000001);
        assertEquals(-2, o.getLongitude(), 0.0000001);
    }

//    @Test
//    public void scaleLocationToPoints1() {
//        // entrance point: (3, 1)
//        // exit point (4, 5)
//        // exit Location(25.0, 50.0)
//        // root(17) to root(325)
//        DataStructure mapDS = new DataStructure(Dummy.getPoints(),
//                new Location(10.0, 40.0),
//                Math.sqrt(17/325),
//                -1, -1);
//
//        Location o = null;
//        try {
//            o = (Location) scaleLocationToPoints.invoke(mapDS,
//                    new Location());
//        } catch (Exception e) { }
//        assertEquals(0, o.getLatitude(), 0.0000001);
//        assertEquals(0, o.getLongitude(), 0.0000001);
//    }
}