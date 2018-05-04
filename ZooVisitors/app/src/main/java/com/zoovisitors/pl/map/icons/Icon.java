package com.zoovisitors.pl.map.icons;

import com.zoovisitors.pl.map.MapView;

public abstract class Icon {
    protected Object[] additionalData;
    protected MapView mapView;
    public int left;
    public int top;
    public int width;
    public int height;

    abstract void setView();
//        abstract void UpdateView(boolean shouldBeCentered, boolean isVisible);

    public Icon(Object[] additionalData, MapView mapView, int left, int top) {
        this.additionalData = additionalData;
        this.mapView = mapView;
        this.left = left;
        this.top = top;
        setView();
    }
}
