package com.zoovisitors.backend;

import android.graphics.drawable.Drawable;

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
    @SerializedName("videos")
    private VideoEnc[] videos;
    @SerializedName("pictures")
    private PictureEnc[] pictures;
    @SerializedName("recEvents")
    private RecurringEvent[] recurringEvents;
    @SerializedName("audioUrl")
    private String audioUrl;
    @SerializedName("markerData")
    private Byte[] markerData;

    private Drawable markerDrawable;

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

    public void setStory(String story) {
        this.story = story;
    }

    public int getMarkerX() {
        return markerX;
    }

    public void setMarkerX(int markerX) {
        this.markerX = markerX;
    }

    public int getMarkerY() {
        return markerY;
    }

    public void setMarkerY(int markerY) {
        this.markerY = markerY;
    }

    public int getClosestPointX() {
        return closestPointX;
    }

    public void setClosestPointX(int closestPointX) {
        this.closestPointX = closestPointX;
    }

    public int getClosestPointY() {
        return closestPointY;
    }

    public void setClosestPointY(int closestPointY) {
        this.closestPointY = closestPointY;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public VideoEnc[] getVideos() {
        return videos;
    }

    public void setVideos(VideoEnc[] videos) {
        this.videos = videos;
    }

    public PictureEnc[] getPictures() {
        return pictures;
    }

    public void setPictures(PictureEnc[] pictures) {
        this.pictures = pictures;
    }

    public RecurringEvent[] getRecurringEvents() {
        return recurringEvents;
    }

    public void setRecurringEvents(RecurringEvent[] recurringEvents) {
        this.recurringEvents = recurringEvents;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Byte[] getMarkerData() {
        return markerData;
    }

    public void setMarkerData(Byte[] markerData) {
        this.markerData = markerData;
    }

    public static class RecurringEvent implements Serializable{
        @SerializedName("title")
        private String title;
        @SerializedName("description")
        private String description;
        @SerializedName("startTime")
        private long startTime;
        @SerializedName("endTime")
        private long endTime;

        public RecurringEvent() {}

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
    }

    public class PictureEnc implements java.io.Serializable{
        @SerializedName("pictureUrl")
        private String pictureUrl;

        public String getPictureUrl() {
            return pictureUrl;
        }
    }

    public class VideoEnc implements java.io.Serializable{
        @SerializedName("videoUrl")
        private String videoUrl;

        public String getVideoUrl() {
            return videoUrl;
        }
    }
}
