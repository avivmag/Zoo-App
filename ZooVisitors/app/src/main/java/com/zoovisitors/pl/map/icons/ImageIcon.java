package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zoovisitors.pl.map.MapView;

public abstract class ImageIcon extends Icon {
    public View view;

    public ImageIcon(MapView mapView, Object[] additionalData, int left, int top, boolean shouldBeCentered, boolean isVisible) {
        super(additionalData, mapView, left, top);
        UpdateView(shouldBeCentered, isVisible);
    }

    public void UpdateView(boolean shouldBeCentered, boolean isVisible) {

        view.setBackgroundColor(Color.TRANSPARENT);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        view.setLayoutParams(layoutParams);

        view.post(new Runnable() {
            @Override
            public void run() {
                width =  view.getMeasuredWidth();
                height = view.getMeasuredHeight();
                if(shouldBeCentered)
                    mapView.updateIconPositionWithSize(ImageIcon.this);
            }
        });
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        mapView.addView(view);
    }
}
