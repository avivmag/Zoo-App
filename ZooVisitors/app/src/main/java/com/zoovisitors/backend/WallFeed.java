package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gili on 13/01/2018.
 */

public class WallFeed implements Serializable {
    // Aviv Note: be aware this maybe java.sql.date, and not the current java.util.date...
    @SerializedName("created")
    private String created;
    @SerializedName("info")
    private String info;
    @SerializedName("title")
    private String title;

    public WallFeed(String info, String title) {
        this.info = info;
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public String getInfo() {
        return info;
    }

    public String getTitle() {
        return title;
    }
}
