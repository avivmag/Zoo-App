package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Gili on 13/01/2018.
 */

public class WallFeed {
    // Aviv Note: be aware this maybe java.sql.date, and not the current java.util.date...
    @SerializedName("created")
    private Date created;
    @SerializedName("info")
    private String info;
    @SerializedName("title")
    private String title;

    public Date getCreated() {
        return created;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }
}
