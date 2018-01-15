package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 13/01/2018.
 */

public class NewsFeed {
    @SerializedName("Stories")
    private String story;

    public String getStory() {
        return story;
    }
}
