package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 12/01/2018.
 */

public class Enclosure {
    @SerializedName("id")
    private int id;
    private int[] animalsInEnc;
    @SerializedName("name")
    private String name;
    private String closestEvent;
    private String youtubeVideoUrl;
    @SerializedName("markerIconUrl")
    private String markerIconUrl;
    @SerializedName("markerLatitude")
    private int markerLatitude;
    @SerializedName("markerLongtitude")
    private int markerLongtitude;

    public String getMarkerIconUrl() {
        return markerIconUrl;
    }

    public int getMarkerLatitude() {
        return markerLatitude;
    }

    public int getMarkerLongtitude() {
        return markerLongtitude;
    }

//    public void setImageURL(String imageURL) {
//        this.imageURL = imageURL;
//    }
//
//    public String getImageURL() {
//        return imageURL;
//    }

    //TODO:: Or I need the imageURL of the enclosure and images of the animals

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getAnimalsInEnc() {
        return animalsInEnc;
    }

    public void setAnimalsInEnc(int[] animalsInEnc) {
        this.animalsInEnc = animalsInEnc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClosestEvent() {
        return closestEvent;
    }

    public void setClosestEvent(String closestEvent) {
        this.closestEvent = closestEvent;
    }

    public String getYoutubeVideoUrl() {
        return youtubeVideoUrl;
    }

    public void setYoutubeVideoUrl(String youtubeVideoUrl) {
        this.youtubeVideoUrl = youtubeVideoUrl;
    }
}
