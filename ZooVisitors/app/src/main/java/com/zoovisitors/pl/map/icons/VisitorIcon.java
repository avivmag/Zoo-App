package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VisitorIcon extends ImageIcon {
    private final String VISITOR_ICON = "visitor_icon_2";

    public VisitorIcon(MapView mapView) {
        super(mapView, null, 0, 0, false);
    }

    @Override
    void setView() {
        ImageView imageView = new ImageView(mapView.getContext());
        imageView.setBackgroundColor(Color.TRANSPARENT);
        int resourceId = mapView.getResources().getIdentifier(VISITOR_ICON, "mipmap", mapView.getContext().getPackageName());
        imageView.setImageResource(resourceId);
        this.view = imageView;
    }

    public void UpdateVisitorLocation(int left, int top) {
        this.left = left;
        this.top = top;

        updateIconPosition();
        view.setVisibility(VISIBLE);
        view.bringToFront();
    }

    public void Hide() {
        view.setVisibility(INVISIBLE);
    }
}
