package com.zoovisitors.backend;

/**
 * Created by Gili on 12/01/2018.
 */
import com.google.gson.annotations.SerializedName;

public class Animal {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Story")
    private String story;
    @SerializedName("EncId")
    private int encId;
    @SerializedName("Language")
    private int language;


    public Animal(String name, String story, int encId, int language, int id) {
        this.name = name;
        this.story = story;
        this.encId = encId;
        this.language = language;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getEncId() {
        return encId;
    }

    public void setEncId(int encId) {
        this.encId = encId;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", story='" + story + '\'' +
                ", encId=" + encId +
                ", language=" + language +
                ", id=" + id +
                '}';
    }
}
