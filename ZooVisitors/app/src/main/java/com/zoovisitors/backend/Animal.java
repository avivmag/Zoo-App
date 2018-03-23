package com.zoovisitors.backend;

/**
 * Created by Gili on 12/01/2018.
 */
import com.google.gson.annotations.SerializedName;

public class Animal implements java.io.Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("story")
    private String story;
    @SerializedName("encId")
    private int encId;
    @SerializedName("category")
    private String category;
    @SerializedName("series")
    private String series;
    @SerializedName("family")
    private String family;
    @SerializedName("ditribution")
    private String ditribution;
    @SerializedName("reproduction")
    private String reproduction;
    @SerializedName("food")
    private String food;
    @SerializedName("preservation")
    private int preservation;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("language")
    private int language;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStory() {
        return story;
    }

    public int getEncId() {
        return encId;
    }

    public String getCategory() {
        return category;
    }

    public String getSeries() {
        return series;
    }

    public String getFamily() {
        return family;
    }

    public String getDitribution() {
        return ditribution;
    }

    public String getReproduction() {
        return reproduction;
    }

    public String getFood() {
        return food;
    }

    public int getPreservation() {
        return preservation;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getLanguage() {
        return language;
    }
}
