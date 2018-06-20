package com.zoovisitors.pl.map.icons;

import android.widget.RelativeLayout;

import com.zoovisitors.pl.map.MapView;

public abstract class ImageIcon extends Icon {
    ImageIcon(MapView mapView, Object[] additionalData, int left, int top, boolean isVisible) {
        super(additionalData, mapView, left, top, isVisible);
    }

    public void updateIconPosition() {
        if (width != 0 && height != 0) {
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams((int) (width * mapView.getmScaleFactor()), (int) (height *
                            mapView.getmScaleFactor()));

            params.setMargins(
                    (int) ((left - width/2) * mapView.getmScaleFactor() + mapView.getmPosX()),
                    (int) ((top - height/2) * mapView.getmScaleFactor() + mapView.getmPosY()),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            view.setLayoutParams(params);
        }
    }
}
