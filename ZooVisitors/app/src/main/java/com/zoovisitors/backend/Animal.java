package com.zoovisitors.backend;

/**
 * Created by Gili on 12/01/2018.
 */

public class Animal {

    private String name;
    private String story;
    private int encId;
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

    private int id;

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

}
