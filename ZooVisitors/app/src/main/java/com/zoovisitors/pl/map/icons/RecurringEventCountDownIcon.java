package com.zoovisitors.pl.map.icons;

import android.view.View;

import com.zoovisitors.pl.map.MapView;

import static com.zoovisitors.bl.RecurringEventsHandler.getHours;
import static com.zoovisitors.bl.RecurringEventsHandler.getMinutes;
import static com.zoovisitors.bl.RecurringEventsHandler.getSeconds;
import static com.zoovisitors.pl.map.icons.handlers.EnclosureIconsHandler
        .RECURRING_EVENT_TIMER_ELAPSE_TIME;

public class RecurringEventCountDownIcon extends TextIcon {
    private enum RecurringEventTimerElapseTimeTextLengthEnum {HOURS, MINUTES, SECONDS};
    private static RecurringEventTimerElapseTimeTextLengthEnum
            RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT =
            RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.SECONDS :
                    RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.MINUTES :
                            RecurringEventTimerElapseTimeTextLengthEnum.HOURS;


    public RecurringEventCountDownIcon(MapView mapView, View.OnTouchListener onTouchListener, int left, int top) {
        super(new Object[] {onTouchListener}, mapView, left, top);
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
        updateViewWidthHeight();
        updateIconPosition();
        // Note: be aware that the visibility is handled by the timer scheduling, not here.
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
        updateViewText(text.toString());
        updateIconPosition();
        show();
    }
}