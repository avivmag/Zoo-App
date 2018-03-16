package com.zoovisitors.bl.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aviv on 16-Mar-18.
 */

public class DataStructure {
    /**
     * @Pre: Must be sorted array (should be done by the server).
     */
    private Point[] points;
    private Map<Point, Set<Point>> pointToPoints;

    public DataStructure(String pointsStr) {
        // TODO: implement a pointsStr to points when we know how it is saved in the server

        points = Dummy.points;
        pointToPoints = new HashMap<>();



    }
}
