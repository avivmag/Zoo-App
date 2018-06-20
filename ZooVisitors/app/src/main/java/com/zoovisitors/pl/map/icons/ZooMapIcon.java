package com.zoovisitors.pl.map.icons;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class ZooMapIcon extends ImageIcon {
    private final String ZOO_MAP = "zoo_map";

    public ZooMapIcon(MapView mapView, Object[] additionalData) {
        super(mapView, additionalData, 0, 0, true);
    }

    @Override
    protected void doOnPost(boolean isVisible) {
        this.left = this.left + width / 2;
        this.top = this.top + height / 2;
        mapView.initiateVariablesWithSize(width, height);
        super.doOnPost(isVisible);
    }

    @Override
    void setView() {
        ImageView imageView = new ImageView(mapView.getContext());
        imageView.setImageBitmap((Bitmap) additionalData[0]);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        this.view = imageView;
    }
}
