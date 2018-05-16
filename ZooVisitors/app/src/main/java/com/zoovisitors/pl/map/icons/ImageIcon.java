package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zoovisitors.pl.map.MapView;

public abstract class ImageIcon extends Icon {
    public View view;

    public ImageIcon(MapView mapView, Object[] additionalData, int left, int top, boolean isVisible) {
        super(additionalData, mapView, left, top);
        UpdateView(isVisible);
    }

    public void UpdateView(boolean isVisible) {

        view.setBackgroundColor(Color.TRANSPARENT);

        // TODO: Check if this block is needed, we are overriding this in the post beneath
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        view.setLayoutParams(layoutParams);

        view.setVisibility(View.INVISIBLE);
        mapView.addView(view);
        view.post(() -> {postRun(isVisible);});
    }

    protected void postRun(boolean isVisible) {
        setSize();
        setImageOnScreen(isVisible);
    }

    protected void setSize() {
        width =  view.getMeasuredWidth();
        height = view.getMeasuredHeight();
    }

    protected void setImageOnScreen(boolean isVisible) {
        mapView.updateIconPositionWithSize(ImageIcon.this);
        if(isVisible)
            view.setVisibility(View.VISIBLE);
    }
}
