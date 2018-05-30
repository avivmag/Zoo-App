package com.zoovisitors.backend;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class Misc implements java.io.Serializable{
    @SerializedName("latitude")
    private int markerY;
    @SerializedName("longtitude")
    private int markerX;
    @SerializedName("iconData")
    private String iconData;

    private Drawable markerDrawable;


    public int getMarkerY() {
        return markerY;
    }

    public int getMarkerX() {
        return markerX;
    }

    public String getIconData() {
        return iconData;
    }

    public Drawable getMarkerDrawable() {
        return markerDrawable;
    }
}
