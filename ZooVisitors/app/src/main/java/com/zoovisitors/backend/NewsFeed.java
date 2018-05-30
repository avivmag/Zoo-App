package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Gili on 13/01/2018.
 */

public class NewsFeed implements java.io.Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("info")
    private String story;

    public NewsFeed(String title, String story){
        this.title = title;
        this.story = story;
    }

    public String getTitle() {return title;}
    public String getStory() {return story;}
}
