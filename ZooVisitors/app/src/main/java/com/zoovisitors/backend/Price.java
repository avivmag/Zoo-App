package com.zoovisitors.backend;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 10/03/2018.
 */

public class Price {
    @SerializedName("population")
    private String population;
    @SerializedName("pricePop")
    private Double pricePop;

    public String getPopulation() {
        return population;
    }

    public String getPricePop() {
        if (pricePop == 0)
            return GlobalVariables.appCompatActivity.getResources().getString(R.string.free);
        return fixPriceToShow("" + pricePop);
    }

    private String fixPriceToShow(String price){
        Log.e("Date price", price);
        return price + "0₪";
    }
}
