package com.zoovisitors.bl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfoResult;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.map.Location;
import com.zoovisitors.backend.map.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Memory {

    private Enclosure[] enclosures;
    private Animal.PersonalStories[] animalStories;
    private Misc[] miscMarkers;
    private MapResult mapResult;
    private WallFeed[] wallFeeds;
    private ContactInfoResult contactInfoResult;
    private OpeningHoursResult openingHoursResult;
    private Price[] prices;
    private String aboutUs;

    public Memory(Enclosure[] enclosures, Animal.PersonalStories[] animalStories,
                  Misc[] miscMarkers, MapResult mapResult, WallFeed[] wallFeeds,
                  ContactInfoResult contactInfoResult, OpeningHoursResult openingHoursResult,
                  Price[] prices, String aboutUs) {
        this.enclosures = enclosures;
        this.animalStories = animalStories;
        this.miscMarkers = miscMarkers;
        this.mapResult = mapResult;
        this.wallFeeds = wallFeeds;
        this.contactInfoResult = contactInfoResult;
        this.openingHoursResult = openingHoursResult;
        this.prices = prices;
        this.aboutUs = aboutUs;
        this.stringToBitmapMap = new HashMap<>();

        initializeBitmaps();
        List<Point> points = new ArrayList<>();
        points.add(new Point(mapResult.getMapInfo().getRoutes()[0].getX1(), mapResult.getMapInfo
                ().getRoutes()[0].getY1()));
        Point lastAdded = points.get(0);
        for (MapResult.MapInfo.Route route :
                mapResult.getMapInfo().getRoutes()) {
            if(lastAdded.getX() != route.getX1())
            {
                lastAdded = new Point(route.getX1(), route.getY1());
                points.add(lastAdded);
            }
        }

        Point[] pointArr = new Point[points.size()];
        mapResult.getMapInfo().setPoints(points.toArray(pointArr));
    }

    private void initializeBitmaps() {
        for (Animal.PersonalStories p : getAnimalStories()) {
            if(p.getPictureData() != null) {
                p.setPersonalPicture(stringToBitmap(p.getPictureData()));
            }
        }
        for (Enclosure e : getEnclosures()) {
            if(e.getMarkerData() != null) {
                e.setMarkerBitmap(stringToBitmap(e.getMarkerData()));
            }
        }
        getMapResult().setMapBitmap(stringToBitmap(getMapResult().getMapData()));
        for (Misc m : getMiscMarkers()) {
            if(m.getMarkerData() != null) {
                m.setMarkerBitmap(stringToBitmap(m.getMarkerData()));
            }
        }
    }

    private Bitmap stringToBitmap(String str) {
        byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    private Map<String, Bitmap> stringToBitmapMap;

    public void setStringAndBitmap(String s, Bitmap d) {
        stringToBitmapMap.put(s, d);
    }

    public Bitmap getBitmapByString(String s) {
        return stringToBitmapMap.get(s);
    }

    public Enclosure[] getEnclosures() {
        return enclosures;
    }

    public Animal.PersonalStories[] getAnimalStories() {
        return animalStories;
    }

    public Misc[] getMiscMarkers() {
        return miscMarkers;
    }

    public MapResult getMapResult() {
        return mapResult;
    }

    public WallFeed[] getWallFeeds() {
        return wallFeeds;
    }

    public ContactInfoResult getContactInfoResult() {
        return contactInfoResult;
    }

    public OpeningHoursResult getOpeningHoursResult() {
        return openingHoursResult;
    }

    public Price[] getPrices() {
        return prices;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setEnclosures(Enclosure[] enclosures) {
        this.enclosures = enclosures;
    }

    public void setAnimalStories(Animal.PersonalStories[] animalStories) {
        this.animalStories = animalStories;
    }

    public void setWallFeeds(WallFeed[] wallFeeds) {
        this.wallFeeds = wallFeeds;
    }

    public void setContactInfoResult(ContactInfoResult contactInfoResult) {
        this.contactInfoResult = contactInfoResult;
    }

    public void setOpeningHoursResult(OpeningHoursResult openingHoursResult) {
        this.openingHoursResult = openingHoursResult;
    }

    public void setPrices(Price[] prices) {
        this.prices = prices;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }
}

