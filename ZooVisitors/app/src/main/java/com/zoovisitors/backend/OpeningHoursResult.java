package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

public class OpeningHoursResult {
    @SerializedName("openingHours")
    private OpeningHours[] openingHours;
    @SerializedName("openingHoursNote")
    private String openingHoursNote;

    public OpeningHours[] getOpeningHours() {
        return openingHours;
    }

    public String getOpeningHoursNote() {
        return openingHoursNote;
    }

    public class OpeningHours {
        @SerializedName("day")
        private String day;
        @SerializedName("startTime")
        private String startTime;
        @SerializedName("endTime")
        private String endTime;

        public String getDay() {
            return day;
        }

        public String getStartTime() {
            return fixHoursToShow(startTime);
        }

        public String getEndTime() {
            return fixHoursToShow(endTime);
        }

        private String fixHoursToShow(String date){
            date = date.substring(0, date.length()-3);
            return date;
        }
    }
}
