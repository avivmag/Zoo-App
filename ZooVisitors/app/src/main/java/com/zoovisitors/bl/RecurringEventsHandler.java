package com.zoovisitors.bl;

import android.util.Log;

import com.zoovisitors.backend.Enclosure;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Gili on 02/05/2018.
 */

public class RecurringEventsHandler {
    public static final int SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;
    public static final int THREE_DAYS = 3 * 24 * 60 * 60 * 1000;
    private int lastRecurringEventIndex = -1;

    private Enclosure.RecurringEvent[] recurringEvents;

    public RecurringEventsHandler(Enclosure.RecurringEvent[] recurringEvents) {
        this.recurringEvents = recurringEvents;
        Arrays.sort(this.recurringEvents, (r1, r2) -> (int)(r1.getEndTime() - r2.getEndTime()));
    }

    public boolean isEmpty()
    {
        return recurringEvents.length == 0;
    }

    public Enclosure.RecurringEvent getNextRecuringEvent(){
        long currentTime = getTimeAdjustedToWeekTime();
        if(lastRecurringEventIndex == -1) {
            if(recurringEvents[recurringEvents.length - 1].getEndTime() <= currentTime ||
                    currentTime < recurringEvents[0].getEndTime()) {
                lastRecurringEventIndex = 0;
                return recurringEvents[0];
            }
            // fast search
            int top = recurringEvents.length, bottom = 0, mid;
            lastRecurringEventIndex = (top + bottom) / 2;
            while(top - bottom > 1){
                mid = (top+bottom)/2;
                if(recurringEvents[mid].getEndTime() <= currentTime) {
                    bottom = mid;
                } else {
                    top = mid;
                }
            }
            lastRecurringEventIndex = top;
            return recurringEvents[lastRecurringEventIndex];
        } else {
            lastRecurringEventIndex = (++lastRecurringEventIndex) % recurringEvents.length;
            return recurringEvents[lastRecurringEventIndex];
        }
    }
    //Added preservation2 hours to get our local time
    public static long getTimeAdjustedToWeekTime() {
        return ((Calendar.getInstance().getTimeInMillis() + SEVEN_DAYS - THREE_DAYS) % SEVEN_DAYS) + 60 * 60 * 1000 * 3;
    }

    /**
     * gets time from server and transform it to our time
     * @param time from server
     * @return our time
     */
    public static String getTime(long time){
        String timeText = "";
        timeText += getHours(time) + ":";
        timeText += getMinutes(time) + ":";
        timeText += getSeconds(time);
        return timeText;
    }


    public static String getSeconds(long time) {
        return String.format("%02d",(int) (time*0.001%60));
    }
    public static String getMinutes(long time) {
        return String.format("%02d",(int) (time*0.001/60%60));
    }
    public static String getHours(long time) {
        return String.format("%02d",(int) (time*0.001/60/60));
    }
}
