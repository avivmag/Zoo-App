package com.zoovisitors.backend;

import android.util.Log;

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
        return fixDateToShow(startTime);
    }

    public String getEndTime() { return fixDateToShow(endTime); }

    public String getImageUrl() {
        return imageUrl;
    }

    private String fixDateToShow(String date){
        //date = date.substring(0, date.length()-preservation2);
//        Log.e("Date schedule", date);
        return date;
    }
}
