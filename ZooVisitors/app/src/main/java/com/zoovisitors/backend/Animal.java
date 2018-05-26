package com.zoovisitors.backend;

/**
 * Created by Gili on 12/01/2018.
 */
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class Animal implements java.io.Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("interesting")
    private String interesting;
    @SerializedName("encId")
    private int encId;
    @SerializedName("category")
    private String category;
    @SerializedName("series")
    private String series;
    @SerializedName("family")
    private String family;
    @SerializedName("distribution")
    private String distribution;
    @SerializedName("reproduction")
    private String reproduction;
    @SerializedName("food")
    private String food;
    @SerializedName("preservation")
    private int preservation;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("language")
    private int language;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInteresting() {
        return interesting;
    }

    public int getEncId() {
        return encId;
    }

    public String getCategory() {
        return category;
    }

    public String getSeries() {
        return series;
    }

    public String getFamily() {
        return family;
    }

    public String getDistribution() {
        return distribution;
    }

    public String getReproduction() {
        return reproduction;
    }

    public String getFood() {
        return food;
    }

    public int getPreservation() {
        return preservation;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getLanguage() {
        return language;
    }


    public class PersonalStories implements java.io.Serializable{
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("story")
        private String story;
        @SerializedName("encId")
        private int encId;
        @SerializedName("pictureUrl")
        private String pictureUrl;
        private Drawable drawable;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStory() {
            return story;
        }

        public int getEncId() {
            return encId;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}
