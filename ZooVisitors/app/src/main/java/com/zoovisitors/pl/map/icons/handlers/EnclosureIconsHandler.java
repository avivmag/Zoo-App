package com.zoovisitors.pl.map.icons.handlers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.RecurringEventsHandler;
import com.zoovisitors.pl.enclosures.EnclosureActivity;
import com.zoovisitors.pl.map.MapView;
import com.zoovisitors.pl.map.icons.EnclosureIcon;
import com.zoovisitors.pl.map.icons.RecurringEventCountDownIcon;
import com.zoovisitors.pl.map.icons.RecurringEventIcon;
import com.zoovisitors.pl.map.icons.TextIcon;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.zoovisitors.bl.RecurringEventsHandler.SEVEN_DAYS;
import static com.zoovisitors.bl.RecurringEventsHandler.getTimeAdjustedToWeekTime;

public class EnclosureIconsHandler {
    public static final long RECURRING_EVENT_TIMER_ELAPSE_TIME = 30 * 60 * 1000;

    private EnclosureIcon enclosureIcon;
    private View.OnTouchListener onTouchListener;
    private RecurringEventCountDownIcon recurringEventCountDownIcon;
    private RecurringEventIcon recurringEventIcon;
    private RecurringEventsHandler recurringEventsHandler;
    private Timer timer;
    private List<Runnable> timerFastRunnables;
    private List<Runnable> timerSlowRunnables;

    public EnclosureIconsHandler(MapView mapView, Enclosure enclosure, Drawable enclosureIconResource,
                                 int left, int top, Timer timer, List<Runnable> timerFastRunnables,
                                 List<Runnable> timerSlowRunnables) {
        this.timerFastRunnables = timerFastRunnables;
        this.timerSlowRunnables = timerSlowRunnables;
        this.timer = timer;
        recurringEventsHandler = new RecurringEventsHandler(enclosure.getRecurringEvents());
        onTouchListener = createOnTouchListener(enclosure);
        enclosureIcon = new EnclosureIcon(mapView, enclosureIconResource, onTouchListener, left, top);

        recurringEventCountDownIcon = new RecurringEventCountDownIcon(mapView,
                onTouchListener,
                left,
                top);
        recurringEventIcon = new RecurringEventIcon(mapView,
                onTouchListener,
                left,
                top);

        // should be ran after the view was added to front and the sizes are known, cool trick..
        enclosureIcon.view.post(() -> {
            recurringEventCountDownIcon.top -= enclosureIcon.height / 2 + recurringEventCountDownIcon.textView.getLineHeight();
            recurringEventIcon.top = recurringEventCountDownIcon.top;
        });

        scheduleRecurringTasks();
    }

    public void updateIconPositionWithSize() {
        enclosureIcon.updateIconPosition();
        recurringEventCountDownIcon.updateIconPosition();
        recurringEventIcon.updateIconPosition();
    }

    @NonNull
    private View.OnTouchListener createOnTouchListener(Enclosure enclosure) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(GlobalVariables.appCompatActivity, EnclosureActivity.class);
                        Bundle clickedEnclosure = new Bundle();

                        clickedEnclosure.putSerializable("enc", enclosure);
                        intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                        GlobalVariables.appCompatActivity.startActivity(intent);

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        };
    }

    /**
     * Scheduling the times when recurring events are shown or hidden.
     */
    public void scheduleRecurringTasks() {
        long currentTime = getTimeAdjustedToWeekTime();
        if(recurringEventsHandler.isEmpty())
            return;
        Enclosure.RecurringEvent recurringEvent = recurringEventsHandler.getNextRecuringEvent();
        recurringEventIcon.setText(recurringEvent.getTitle());
        // means the text should not be shown yet
        if (recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME > currentTime) {
            scheduleTextIcon(recurringEvent.getStartTime(), recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME - currentTime);
        } else if (recurringEvent.getStartTime() > currentTime) { // means we should be at the middle of the text timer ticking
            scheduleTextIcon(recurringEvent.getStartTime(), 0);
        } else if (currentTime >= recurringEvent.getEndTime()) { // means we are after the event time, we delay the time until the week will pass and to the starting minus Wait interval
            scheduleTextIcon(recurringEvent.getStartTime(), recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME + SEVEN_DAYS - currentTime);
        }

        // means that we shouldn't show the image yet
        if (recurringEvent.getStartTime() > currentTime) {
            scheduleRecurringEventIcon(recurringEvent.getEndTime(), recurringEvent.getStartTime() - currentTime);
        } else if (recurringEvent.getEndTime() > currentTime && currentTime >= recurringEvent.getStartTime()) { // means the event is on
            scheduleRecurringEventIcon(recurringEvent.getEndTime(), 0);
        } else { // really low percent it will happen, only if between the time it was called and until it started, the event was finished.
            scheduleRecurringEventIcon(recurringEvent.getEndTime(), recurringEvent.getStartTime() + SEVEN_DAYS - currentTime);
        }
    }

    private void scheduleRecurringEventIcon(long endTime, long delayTime) {
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               timerFastRunnables.add(
                                       new Runnable() {
                                           @Override
                                           public void run() {
                                               long currentTime = getTimeAdjustedToWeekTime();
                                               if (currentTime < endTime) {
                                                   recurringEventIcon.updateOpacity();
                                               } else {
                                                   recurringEventIcon.hide();
                                                   timerFastRunnables.remove(this);
                                                   scheduleRecurringTasks();
                                               }
                                           }
                                       }
                               );
                           }
                       },
                delayTime);
    }

    private void scheduleTextIcon(long startTime, long delayTime) {
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               timerSlowRunnables.add(
                                       new Runnable() {
                                           @Override
                                           public void run() {
                                               long currentTime = getTimeAdjustedToWeekTime();
                                               if(currentTime < startTime){
                                                   recurringEventCountDownIcon.setTime(startTime - currentTime);
                                               } else {
                                                   recurringEventCountDownIcon.hide();
                                                   timerSlowRunnables.remove(this);
                                               }
                                           }
                                       }
                               );
                           }
                       },
                delayTime);
    }
}
