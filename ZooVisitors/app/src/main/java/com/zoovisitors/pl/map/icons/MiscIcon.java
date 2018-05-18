package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.zoovisitors.backend.Misc;
import com.zoovisitors.pl.map.MapView;

public class MiscIcon extends ImageIcon {
    public MiscIcon(MapView mapView, Drawable resource, int left, int top) {
        super(mapView, new Object[]{resource}, left, top, true);

    }

    @Override
    void setView() {
        ImageView imageView = new ImageView(mapView.getContext());
        imageView.setImageDrawable((Drawable) additionalData[0]);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        this.view = imageView;
    }
}
