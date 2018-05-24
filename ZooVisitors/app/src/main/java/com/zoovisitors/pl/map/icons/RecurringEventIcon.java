package com.zoovisitors.pl.map.icons;

import android.view.View;

import com.zoovisitors.pl.map.MapView;

public class RecurringEventIcon extends TextIcon {
    private static final float ALPHA_CHANGE = 0.05f;
    private static final float MINIMAL_IMAGE_ALPHA = 0.2f;
    private static final float MAXIMAL_IMAGE_ALPHA = 1f;

    public RecurringEventIcon(MapView mapView, View.OnTouchListener onTouchListener, int left, int top) {
        super(new Object[] {onTouchListener}, mapView, left, top);
        // Note: be aware that the visibility is handled by the timer scheduling, not here.
    }

    public void setText(String text) {
        updateViewText(text);
        updateViewWidthHeight();
        updateIconPosition();
    }

    float lastAlpha = MAXIMAL_IMAGE_ALPHA;
    boolean directionIsUp = false;
    public void updateOpacity()
    {
        if(lastAlpha <= MINIMAL_IMAGE_ALPHA)
            directionIsUp = true;
        else if(lastAlpha >= MAXIMAL_IMAGE_ALPHA)
            directionIsUp = false;

        lastAlpha += directionIsUp ? ALPHA_CHANGE : -ALPHA_CHANGE;
        textView.post(() -> {
            textView.setAlpha(lastAlpha);
        });
        show();
    }
}
