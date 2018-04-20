//package com.zoovisitors.backend;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.io.Serializable;
//
///**
// * Created by Aviv on 14/04/2018.
// */
//
//public class RecurringEvent implements Serializable{
//    @SerializedName("id")
//    private int id;
//    @SerializedName("enclosureId")
//    private int enclosureId;
//    @SerializedName("description")
//    private String description;
//    @SerializedName("weekDay")
//    private int weekDay;
//    @SerializedName("startTime")
//    private long startTime;
//    @SerializedName("lastsTime")
//    private long lastsTime;
//    @SerializedName("title")
//    private String title;
//
//    public static RecurringEvent createRecurringEvent(int id, int enclosureId, String description, int weekDay, long startTime, long lastsTime, String title) {
//        RecurringEvent recurringEvent = new RecurringEvent();
//        recurringEvent.id = id;
//        recurringEvent.enclosureId = enclosureId;
//        recurringEvent.description = description;
//        recurringEvent.weekDay = weekDay;
//        recurringEvent.startTime = startTime;
//        recurringEvent.lastsTime = lastsTime;
//        recurringEvent.title = title;
//        return recurringEvent;
//    }
//}