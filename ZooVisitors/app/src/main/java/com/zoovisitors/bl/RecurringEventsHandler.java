package com.zoovisitors.bl;

import com.zoovisitors.backend.Enclosure;

/**
 * Created by Gili on 02/05/2018.
 */

public class RecurringEventsHandler {

    public Enclosure.RecurringEvent getNextRecuringEvent(Enclosure.RecurringEvent[] recurringEvents){
        //TODO: get next reccuring
        return new Enclosure.RecurringEvent("Feeding");
    }

    /**
     * gets time from server and transform it to our time
     * @param time from server
     * @return our time
     */
    public String transformTime(long time){
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
        return String.format("%02d",(int) Math.min(time*0.001/60/60, 99));
    }
}
