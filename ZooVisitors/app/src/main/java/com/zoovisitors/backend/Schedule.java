package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 10/03/2018.
 */

public class Schedule {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("startDate")
    private String startTime;
    @SerializedName("endDate")
    private String endTime;
    @SerializedName("imageUrl")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() { return endTime; }

    public String getImageUrl() {
        return imageUrl;
    }
}
