package com.zoovisitors.backend;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.zoovisitors.backend.map.Point;

import java.util.Map;
import java.util.Set;

public class MapResult {
    @SerializedName("mapData")
    private String mapData;
    @SerializedName("mapInfo")
    private MapInfo mapInfo;
    private Bitmap mapBitmap;

    public Bitmap getMapBitmap() {
        return mapBitmap;
    }

    public MapInfo getMapInfo() {
        return mapInfo;
    }

    public void setMapBitmap(Bitmap mapBitmap) {
        this.mapBitmap = mapBitmap;
    }

    public String getMapData() {
        return mapData;
    }

    public class MapInfo {
        @SerializedName("zooLocationLongitude")
        private double zooLocationLongitude;
        @SerializedName("zooLocationLatitude")
        private double zooLocationLatitude;
        @SerializedName("zooPointX")
        private int zooPointX;
        @SerializedName("zooPointY")
        private int zooPointY;
        @SerializedName("xLongitudeRatio")
        private float xLongitudeRatio;
        @SerializedName("yLatitudeRatio")
        private float yLatitudeRatio;
        @SerializedName("sinAlpha")
        private float sinAlpha;
        @SerializedName("cosAlpha")
        private float cosAlpha;
        @SerializedName("minLatitude")
        private double minLatitude;
        @SerializedName("maxLatitude")
        private double maxLatitude;
        @SerializedName("minLongitude")
        private double minLongitude;
        @SerializedName("maxLongitude")
        private double maxLongitude;
        @SerializedName("routes")
        private Route[] routes;

        private Point[] points;
        private Map<Point, Set<Point>> routesMap;

        public double getZooLocationLongitude() {
            return zooLocationLongitude;
        }

        public double getZooLocationLatitude() {
            return zooLocationLatitude;
        }

        public int getZooPointX() {
            return zooPointX;
        }

        public int getZooPointY() {
            return zooPointY;
        }

        public float getxLongitudeRatio() {
            return xLongitudeRatio;
        }

        public float getyLatitudeRatio() {
            return yLatitudeRatio;
        }

        public float getSinAlpha() {
            return sinAlpha;
        }

        public float getCosAlpha() {
            return cosAlpha;
        }

        public double getMinLatitude() {
            return minLatitude;
        }

        public double getMaxLatitude() {
            return maxLatitude;
        }

        public double getMinLongitude() {
            return minLongitude;
        }

        public double getMaxLongitude() {
            return maxLongitude;
        }

        public Route[] getRoutes() {
            return routes;
        }

        public Point[] getPoints() {
            return points;
        }

        public void setPoints(Point[] points) {
            this.points = points;
        }

        public Map<Point, Set<Point>> getRoutesMap() {
            return routesMap;
        }

        public void setRoutesMap(Map<Point, Set<Point>> routesMap) {
            this.routesMap = routesMap;
        }

        public class Route {
            @SerializedName("primaryLeft")
            private int x1;
            @SerializedName("primaryRight")
            private int y1;
            @SerializedName("secLeft")
            private int x2;
            @SerializedName("secRight")
            private int y2;

            public int getX1() {
                return x1;
            }

            public int getY1() {
                return y1;
            }

            public int getX2() {
                return x2;
            }

            public int getY2() {
                return y2;
            }
        }
    }
}
