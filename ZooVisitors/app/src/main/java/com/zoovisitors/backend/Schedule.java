package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 10/03/2018.
 */

public class Schedule {
    @SerializedName("Id")
    private int id;
    @SerializedName("Description")
    private String description;
    @SerializedName("StartDate")
    private String startTime;
    @SerializedName("EndDate")
    private String endTime;
    @SerializedName("Stories")
    private String image;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getImage() {
        return image;
    }

    public String getEndTime() { return endTime; }

    public void setId(int id) { this.id = id; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) { this.endTime = endTime; }

    public void setImage(String image) {
        this.image = image;
    }
}
