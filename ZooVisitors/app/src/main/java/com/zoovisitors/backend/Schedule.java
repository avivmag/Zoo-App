package com.zoovisitors.backend;

/**
 * Created by Gili on 10/03/2018.
 */

public class Schedule {
    private int id;
    private String name;
    private String time;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
