package com.zoovisitors.pl.map.icons;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.pl.map.MapView;

import static com.zoovisitors.bl.RecurringEventsHandler.getHours;
import static com.zoovisitors.bl.RecurringEventsHandler.getMinutes;
import static com.zoovisitors.bl.RecurringEventsHandler.getSeconds;
import static com.zoovisitors.pl.map.MapView.RECURRING_EVENT_TIMER_ELAPSE_TIME;
import static com.zoovisitors.pl.map.MapView.TEXT_VIEW_TEXT_SIZE;

public class TextIcon extends Icon {
    public static final int PADDING_SIZE = 5;
    public TextView textView;
    private enum RecurringEventTimerElapseTimeTextLengthEnum {HOURS, MINUTES, SECONDS};
    private static RecurringEventTimerElapseTimeTextLengthEnum
            RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT =
            RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.SECONDS :
                    RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.MINUTES :
                            RecurringEventTimerElapseTimeTextLengthEnum.HOURS;


    public TextIcon(MapView mapView, View.OnTouchListener onTouchListener, int left, int top) {
        super(new Object[] {onTouchListener}, mapView, left, top);
        // Note: be aware that the visibility is handled by the timer scheduling, not here.
    }

    @Override
    void setView() {
        textView = new TextView(mapView.getContext());
        textView.setTextSize(TEXT_VIEW_TEXT_SIZE);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(ContextCompat.getColor(mapView.getContext(), R.color.orangeNegev));
        gd.setCornerRadius(5);
        gd.setStroke(5, ContextCompat.getColor(mapView.getContext(), R.color.orangeNegev));
        textView.setOnTouchListener((View.OnTouchListener) additionalData[0]);

        switch(RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT) {
            case HOURS:
                textView.setText("00:00:00");
                break;
            case MINUTES:
                textView.setText("00:00");
                break;
            case SECONDS:
                textView.setText("00");
                break;
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        textView.setLayoutParams(layoutParams);
        textView.setVisibility(View.INVISIBLE);
        mapView.addView(textView);

        textView.post(() -> {
            textView.setBackground(gd);
            textView.setPadding(PADDING_SIZE,PADDING_SIZE,PADDING_SIZE,PADDING_SIZE);
            width =  textView.getMeasuredWidth();
            height = textView.getMeasuredHeight();
        });
    }

    /**
     *
     * @param time The time that needs to be shown (after all calculations except dividing to hours, minutes and seconds)
     */
    public void setTime(long time) {
        StringBuilder text = new StringBuilder();
        switch(RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT) {
            case HOURS:
                text.append(getHours(time));
                text.append(":");
            case MINUTES:
                text.append(getMinutes(time));
                text.append(":");
            case SECONDS:
                text.append(getSeconds(time));
                break;
        }
        textView.post(() -> {
            textView.setText(text.toString());
            textView.setVisibility(View.VISIBLE);
        });
    }
    public void hide()
    {
        textView.post(() -> {
            textView.setVisibility(View.INVISIBLE);
        });
    }
}
