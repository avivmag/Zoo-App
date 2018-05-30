package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

public class MapResult {
    @SerializedName("mapData")
    private String mapData;
    @SerializedName("mapInfo")
    private MapInfo mapInfo;

    private class MapInfo {
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

        private class Route {
            @SerializedName("primaryLeft")
            private int x1;
            @SerializedName("primaryRight")
            private int y1;
            @SerializedName("secondaryLeft")
            private int x2;
            @SerializedName("secondaryRight")
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
