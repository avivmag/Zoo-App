package com.zoovisitors.pl.map.icons;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class EnclosureIcon extends ImageIcon {
    public EnclosureIcon(MapView mapView, Drawable resource, View.OnTouchListener onTouchListener, int left, int top) {
        super(mapView, new Object[] {resource}, left, top, true);
        view.setOnTouchListener(onTouchListener);
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        view.setImageDrawable((Drawable) additionalData[0]);
        this.view = view;
    }
}
