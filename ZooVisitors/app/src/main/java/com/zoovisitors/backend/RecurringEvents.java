package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gili on 12/01/2018.
 */

public class RecurringEvents implements Serializable{
    @SerializedName("enclosureId")
    private int enclosureId;
    @SerializedName("description")
    private String description;
    @SerializedName("day")
    private int day;
    @SerializedName("startHour")
    private long startHour;
    @SerializedName("endHour")
    private long endHour;
}
