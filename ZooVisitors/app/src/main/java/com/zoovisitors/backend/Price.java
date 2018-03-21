package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gili on 10/03/2018.
 */

public class Price {
    @SerializedName("Id")
    private int id;
    @SerializedName("population")
    private String population;
    @SerializedName("pricePop")
    private Double pricePop;

    public int getId() {
        return id;
    }

    public String getPopulation() {
        return population;
    }

    public Double getPricePop() {
        return pricePop;
    }
}