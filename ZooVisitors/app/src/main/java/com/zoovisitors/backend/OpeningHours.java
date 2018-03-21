package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 21/03/2018.
 */

public class OpeningHours {
    @SerializedName("id")
    private int id;
    @SerializedName("day")
    private String day;
    @SerializedName("startHour")
    private int startHour;
    @SerializedName("startMin")
    private int startMin;
    @SerializedName("endHour")
    private int endHour;
    @SerializedName("endMin")
    private int endMin;


    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMin() {
        return endMin;
    }
}
