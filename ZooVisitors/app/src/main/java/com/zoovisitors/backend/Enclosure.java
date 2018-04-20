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
    private String youtubeVideoUrl;
    @SerializedName("markerIconUrl")
    private String markerIconUrl;
    @SerializedName("markerLatitude")
    private int markerLatitude;
    @SerializedName("markerLongtitude")
    private int markerLongtitude;
    @SerializedName("pictureUrl")
    private String pictureUrl;

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
}
