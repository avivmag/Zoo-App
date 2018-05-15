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

    public Icon(Object[] additionalData, MapView mapView, int left, int top, boolean isMapView) {
        this.additionalData = additionalData;
        this.mapView = mapView;
        this.left = left;
        this.top = top;
        setView();
        // zooMapIcon is the first that is added to the main thread, so this will be ran after the we already know the map width and height
        if(!isMapView) {
            mapView.post(() -> {
                this.left = mapView.getIconsOffsetLeft(this.left);
                this.top = mapView.getIconsOffsetTop(this.top);
            });
        }
    }
}
