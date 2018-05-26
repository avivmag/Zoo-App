package com.zoovisitors.backend;

import com.google.gson.annotations.SerializedName;

public class ContactInfoResult {

    @SerializedName("contactInfo;")
    private ContactInfo[] contactInfo;
    @SerializedName("contactInfoNote")
    private String contactInfoNote;

    private class ContactInfo {
        @SerializedName("via")
        private String via;
        @SerializedName("address")
        private String address;

        public String getVia() {
            return via;
        }
        public String getAddress() {
            return address;
        }
    }
}
