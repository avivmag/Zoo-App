package com.zoovisitors.pl.map.icons;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.pl.map.MapView;

public abstract class TextIcon extends Icon {
    private static final int TEXT_VIEW_TEXT_SIZE = 10;
    private static final int PADDING_SIZE = 5;
    TextView textView;
    private GradientDrawable gd;

    TextIcon(Object[] additionalData, MapView mapView, int left, int top) {
        super(additionalData, mapView, left, top, false);
        // Note: be aware that the visibility is handled by the timer scheduling, not here.
    }

    @Override
    void setView() {
        textView = new TextView(mapView.getContext());
        this.view = textView;
        textView.setTextSize(TEXT_VIEW_TEXT_SIZE);
        textView.setTextColor(mapView.getResources().getColor(R.color.mapGetToKnowMeText));
        gd = new GradientDrawable();
        gd.setColor(ContextCompat.getColor(mapView.getContext(), R.color.mapGetToKnowMeBackground));
        gd.setCornerRadius(5);
        textView.setOnTouchListener((View.OnTouchListener) additionalData[0]);

        textView.setBackground(gd);
        textView.setPadding(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE);
    }
    void updateViewText(String text) {
        textView.post(() -> {
            textView.setText(text);
        });
    }

    void updateViewWidthHeight() {
        textView.post(() -> {
            textView.measure(0, 0);
            width = (int) (textView.getMeasuredWidth() / mapView.getmScaleFactor());
            height = (int) (textView.getMeasuredHeight() / mapView.getmScaleFactor());
            ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).setMargins(
                    (int) ((left - width / 2) * mapView.getmScaleFactor() + mapView.getmPosX()),
                    (int) ((top - height / 2) * mapView.getmScaleFactor() + mapView.getmPosY()),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            textView.setTextSize(TEXT_VIEW_TEXT_SIZE * mapView.getmScaleFactor());
        });
    }
    public void updateIconPosition() {
        if (width != 0 && height != 0) {
            ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).setMargins(
                    (int) ((left - width / 2) * mapView.getmScaleFactor() + mapView.getmPosX()),
                    (int) ((top - height / 2) * mapView.getmScaleFactor() + mapView.getmPosY()),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            textView.post(() -> {
                textView.setTextSize(TEXT_VIEW_TEXT_SIZE * mapView.getmScaleFactor());
            });
        }
    }
}
