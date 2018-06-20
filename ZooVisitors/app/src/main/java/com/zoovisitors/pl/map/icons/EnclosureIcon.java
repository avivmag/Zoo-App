package com.zoovisitors.pl.map.icons;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class EnclosureIcon extends ImageIcon {
    public EnclosureIcon(MapView mapView, Bitmap resource, View.OnTouchListener onTouchListener, int left, int top) {
        super(mapView, new Object[] {resource}, left, top, true);
        view.setOnTouchListener(onTouchListener);
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        view.setImageBitmap((Bitmap) additionalData[0]);
        view.setBackgroundColor(Color.TRANSPARENT);
        this.view = view;
    }
}
