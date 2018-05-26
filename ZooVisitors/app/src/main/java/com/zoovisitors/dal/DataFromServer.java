package com.zoovisitors.dal;

import com.google.gson.annotations.SerializedName;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfoResult;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.WallFeed;

public class DataFromServer {
    @SerializedName("enclosures")
    private Enclosure[] enclosures;
    @SerializedName("animalStories")
    private Animal.PersonalStories[] animalStories;
    @SerializedName("miscMarkers")
    private Misc[] miscMarkers;
    @SerializedName("mapResult")
    private MapResult mapResult;
    @SerializedName("wallFeeds")
    private WallFeed[] wallFeeds;
    @SerializedName("contactInfoResult")
    private ContactInfoResult contactInfoResult;
    @SerializedName("openingHoursResult")
    private OpeningHoursResult openingHoursResult;
    @SerializedName("prices")
    private Price[] prices;
    @SerializedName("aboutUs")
    private String aboutUs;
}
