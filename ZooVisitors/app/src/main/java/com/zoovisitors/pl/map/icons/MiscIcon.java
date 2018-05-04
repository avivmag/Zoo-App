package com.zoovisitors.pl.map.icons;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.zoovisitors.backend.Misc;
import com.zoovisitors.pl.map.MapView;

public class MiscIcon extends ImageIcon {
    private Misc misc;
    public MiscIcon(MapView mapView, Drawable resource, Misc misc, int left, int top) {
        super(mapView, new Object[]{resource}, left, top, true, true);
        this.misc = misc;
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        view.setImageDrawable((Drawable) additionalData[0]);
        this.view = view;
    }
}
