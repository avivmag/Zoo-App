package com.zoovisitors.pl.map.icons;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zoovisitors.pl.map.MapView;

public abstract class Icon {
    protected Object[] additionalData;
    protected MapView mapView;
    public int left;
    public int top;
    public int width;
    public int height;
    public View view;

    abstract void setView();
    abstract void updateIconPosition();

    public Icon(Object[] additionalData, MapView mapView, int left, int top, boolean isVisible) {
        this.additionalData = additionalData;
        this.mapView = mapView;
        this.left = left;
        this.top = top;
        setView();

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        view.setLayoutParams(layoutParams);

        view.setVisibility(View.INVISIBLE);
        mapView.addView(view);
        view.post(() -> {
            width = view.getMeasuredWidth();
            height = view.getMeasuredHeight();
            doOnPost(isVisible);
        });
    }

    protected void doOnPost(boolean isVisible) {
        updateIconPosition();
        if (isVisible)
            view.setVisibility(View.VISIBLE);
    }

    public void show() {
        view.post(() -> {
            view.setVisibility(View.VISIBLE);
        });
    }
    public void hide()
    {
        view.post(() -> {
            view.setVisibility(View.INVISIBLE);
        });
    }
}
