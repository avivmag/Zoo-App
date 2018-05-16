package com.zoovisitors.pl.map.icons;

import android.view.View;
import android.widget.ImageView;

import com.zoovisitors.pl.map.MapView;

public class RecurringEventIcon extends ImageIcon {
    private final String RECURRING_EVENT_STARTED_ICON = "recurring_event_started_icon";

    public RecurringEventIcon(MapView mapView, View.OnTouchListener onTouchListener, int left, int top) {
        super(mapView, new Object[] {onTouchListener}, left, top, false);
    }

    @Override
    void setView() {
        ImageView view = new ImageView(mapView.getContext());
        int resourceId = mapView.getResources().getIdentifier(RECURRING_EVENT_STARTED_ICON, "mipmap", mapView.getContext().getPackageName());
        view.setImageResource(resourceId);
        view.setOnTouchListener((View.OnTouchListener) additionalData[0]);
        view.setVisibility(View.INVISIBLE);
        this.view = view;
    }

    float lastAlpha = MapView.MAXIMAL_IMAGE_ALPHA;
    boolean directionIsUp = false;
    public void updateOpacity()
    {
        if(lastAlpha <= MapView.MINIMAL_IMAGE_ALPHA)
            directionIsUp = true;
        else if(lastAlpha >= MapView.MAXIMAL_IMAGE_ALPHA)
            directionIsUp = false;

        lastAlpha += directionIsUp ? MapView.ALPHA_CHANGE : -MapView.ALPHA_CHANGE;
        view.post(() -> {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(lastAlpha);
        });
    }

    public void hide() {
        view.post(() -> {
            view.setVisibility(View.INVISIBLE);
        });
    }
}
