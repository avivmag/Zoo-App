package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 12/01/2018.
 */

public class Enclosure {
    @SerializedName("Id")
    private int id;
    private int[] animalsInEnc;
    @SerializedName("Name")
    private String name;
    private String closestEvent;
    private String youtubeVideoUrl;
    @SerializedName("latitude")
    private int latitude;
    @SerializedName("longitude")
    private int longitude;
    @SerializedName("Story")
    private String story;


    //TODO:: Or I need the image of the enclosure and images of the animals

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
