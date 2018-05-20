package com.zoovisitors.backend;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class AnimalStory {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("pictureDrawable")
    private Drawable pictureDrawable;
    @SerializedName("enclosureId")
    private int enclosureId;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Drawable getPictureDrawable() {
        return pictureDrawable;
    }

    public int getEnclosureId() {
        return enclosureId;
    }
}
