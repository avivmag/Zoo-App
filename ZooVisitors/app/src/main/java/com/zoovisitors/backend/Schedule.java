package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 10/03/2018.
 */

public class Schedule {
    @SerializedName("id")
    private int id;
    @SerializedName("description")
    private String description;
    @SerializedName("startDate")
    private String startTime;
    @SerializedName("endDate")
    private String endTime;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
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

}
