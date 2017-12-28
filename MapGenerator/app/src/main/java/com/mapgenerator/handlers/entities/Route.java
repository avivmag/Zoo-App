package com.mapgenerator.handlers.entities;

/**
 * Created by aviv on 08-Dec-17.
 */

public class Route {
    Coordinate coordinate1;
    Coordinate coordinate2;
    public Route(Coordinate coordinate1, Coordinate coordinate2) {
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
    }
}
