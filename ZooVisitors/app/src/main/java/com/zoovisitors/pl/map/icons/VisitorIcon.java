package com.zoovisitors.pl.map.icons;

import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VisitorIcon extends ImageIcon {
    private final String VISITOR_ICON = "visitor_icon";

    public VisitorIcon(MapView mapView, Object[] additionalData, int left, int top, boolean
            shouldBeCentered, boolean isVisible) {
        super(mapView, additionalData, left, top, shouldBeCentered, isVisible);
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        int resourceId = mapView.getResources().getIdentifier(VISITOR_ICON, "mipmap", mapView.getContext().getPackageName());
        view.setImageResource(resourceId);
        this.view = view;
    }

    public void UpdateVisitorLocation(int left, int top) {
        this.left = left;
        this.top = top;
        mapView.updateIconPositionWithSize(this);
    }

    public void Show() {
        view.setVisibility(VISIBLE);
    }

    public void Hide() {
        view.setVisibility(INVISIBLE);
    }
}
