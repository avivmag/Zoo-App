package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Gili on 12/01/2018.
 */

public class Enclosure implements java.io.Serializable{
    public static final int SEVEN_DAYS = 7 *
            24 * 60 * 60 * 1000;
//    public static final int THREE_DAYS = 3 * 24 * 60 * 60 * 1000;

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("story")
    private String story;
    private String youtubeVideoUrl;
    @SerializedName("markerIconUrl")
    private String markerIconUrl;
    @SerializedName("markerLatitude")
    private int markerLatitude;
    @SerializedName("markerLongtitude")
    private int markerLongtitude;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("recurringEvents")
    private RecurringEvent[] recurringEvents;

//    public RecurringEvent[] getRecurringEvent() { return recurringEvents; }
    public Queue<RecurringEvent> getRecurringEvent() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        for (int j = 0; j < recurringEvents.length; j++) {
            recurringEvents[j].setStartTime(
                    (recurringEvents[j].getStartTime() + SEVEN_DAYS - currentTime// + THREE_DAYS
                    ) % SEVEN_DAYS);
            recurringEvents[j].setEndTime(
                    (recurringEvents[j].getEndTime() + SEVEN_DAYS - currentTime// + THREE_DAYS
                    ) % SEVEN_DAYS);
//                                (recurringEvents[j].getStartTime() + SEVEN_DAYS - ((currentTime - THREE_DAYS) % SEVEN_DAYS)) % SEVEN_DAYS);
        }
        Arrays.sort(recurringEvents, (rec1, rec2) ->
                (int) (rec1.getStartTime() - rec2.getStartTime()));
        return new LinkedList<>(Arrays.asList(recurringEvents));
    }

    // TODO: remove this when recurring events are completed on the server side
    public void setRecurringEvent(RecurringEvent[] recurringEvents) { this.recurringEvents = recurringEvents; }

    public String getMarkerIconUrl() {
        return markerIconUrl;
    }

    public int getMarkerLatitude() {
        return markerLatitude;
    }

    public int getMarkerLongtitude() {
        return markerLongtitude;
    }

    //TODO:: Or I need the imageURL of the enclosure and images of the animals

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYoutubeVideoUrl() {
        return youtubeVideoUrl;
    }

    public void setYoutubeVideoUrl(String youtubeVideoUrl) {
        this.youtubeVideoUrl = youtubeVideoUrl;
    }

    public String getStory() {
        return story;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }


    public static Enclosure createInstance(int id, String name, String story, String youtubeVideoUrl, String markerIconUrl, int markerLatitude, int markerLongtitude, String pictureUrl) {
        Enclosure enclosure = new Enclosure();
        enclosure.id = id;
        enclosure.name = name;
        enclosure.story = story;
        enclosure.youtubeVideoUrl = youtubeVideoUrl;
        enclosure.markerIconUrl = markerIconUrl;
        enclosure.markerLatitude = markerLatitude;
        enclosure.markerLongtitude = markerLongtitude;
        enclosure.pictureUrl = pictureUrl;
        return enclosure;
    }

    public static class RecurringEvent implements Serializable{
        @SerializedName("id")
        private int id;
        @SerializedName("description")
        private String description;
        @SerializedName("startTime")
        private long startTime;
        @SerializedName("endTime")
        private long endTime;
        @SerializedName("title")
        private String title;

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public String getTitle() {
            return title;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public static RecurringEvent createRecurringEvent(int id, String description, long startTime, long lastsTime, String title) {
            RecurringEvent recurringEvent = new RecurringEvent();
            recurringEvent.id = id;
            recurringEvent.description = description;
            recurringEvent.startTime = startTime;
            recurringEvent.endTime = lastsTime;
            recurringEvent.title = title;
            return recurringEvent;
        }
    }
}
