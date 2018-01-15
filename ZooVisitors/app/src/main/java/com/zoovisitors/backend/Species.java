package com.zoovisitors.backend;

/**
 * Created by Gili on 13/01/2018.
 */

public class Species {
    private String name;
    private int extinctNumber; //from 1 to 10 - how much the species is close to extinct.
    private String about;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExtinctNumber() {
        return extinctNumber;
    }

    public void setExtinctNumber(int extinctNumber) {
        this.extinctNumber = extinctNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
