package com.zoovisitors.backend;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class Misc {
    @SerializedName("latitude")
    private int markerY;
    @SerializedName("longitude")
    private int markerX;
    @SerializedName("iconData")
    private String markerData;
    private Bitmap markerBitmap;

    public int getMarkerY() {
        return markerY;
    }

    public int getMarkerX() {
        return markerX;
    }

    public String getMarkerData() {
        return markerData;
    }

    public Bitmap getMarkerBitmap() {
        return markerBitmap;
    }

    public void setMarkerBitmap(Bitmap markerBitmap) {
        this.markerBitmap = markerBitmap;
    }
}
