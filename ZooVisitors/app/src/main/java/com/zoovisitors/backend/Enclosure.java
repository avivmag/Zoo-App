package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Gili on 12/01/2018.
 */

public class Enclosure implements java.io.Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("story")
    private String story;
    @SerializedName("markerIconUrl")
    private String markerIconUrl;
    @SerializedName("markerX")
    private int markerX;
    @SerializedName("markerY")
    private int markerY;
    @SerializedName("markerClosestPointX")
    private int closestPointX;
    @SerializedName("markerClosestPointY")
    private int closestPointY;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("recEvents")
    private RecurringEvent[] recurringEvents;
    @SerializedName("pictures")
    private PictureEnc[] pictures;
    @SerializedName("videos")
    private VideoEnc[] videos;

    public PictureEnc[] getPictures() {
        return pictures;
    }

    public VideoEnc[] getVideos() {
        return videos;
    }

    public RecurringEvent[] getRecurringEvents() { return recurringEvents; }

    // TODO: remove this when recurring events are completed on the server side
    public void setRecurringEvent(RecurringEvent[] recurringEvents) { this.recurringEvents = recurringEvents; }

    public String getMarkerIconUrl() {
        return markerIconUrl;
    }

    public int getMarkerX() { return markerX; }
    public int getMarkerY() {
        return markerY;
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

    public String getStory() {
        return story;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getClosestPointX() {
        return closestPointX;
    }

    public int getClosestPointY() {
        return closestPointY;
    }

    // TODO: delete this dummies
    public void setClosestPointX(int closestPointX) {
        this.closestPointX = closestPointX;
    }

    public void setClosestPointY(int closestPointY) {
        this.closestPointY = closestPointY;
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

        public RecurringEvent() {}

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

//        public void setStartTime(long startTime) {
//            this.startTime = startTime;
//        }

//        public void setEndTime(long endTime) {
//            this.endTime = endTime;
//        }

        // TODO: remove this, it is only for testing.
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

    public class PictureEnc implements java.io.Serializable{
        @SerializedName("id")
        private int id;
        @SerializedName("enclosureId")
        private int enclosureId;
        @SerializedName("pictureUrl")
        private String pictureUrl;

        public int getId() {
            return id;
        }

//        public int getEnclosureId() {
//            return enclosureId;
//        }

        public String getPictureUrl() {
            return pictureUrl;
        }
    }

    public class VideoEnc implements java.io.Serializable{
        @SerializedName("id")
        private int id;
        @SerializedName("enclosureId")
        private int enclosureId;
        @SerializedName("videoUrl")
        private String videoUrl;


        public int getId() {
            return id;
        }

        public int getEnclosureId() {
            return enclosureId;
        }

        public String getVideoUrl() {
            return videoUrl;
        }
    }
}
