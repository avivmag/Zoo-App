package com.zoovisitors.dal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Pair;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfoResult;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.map.Point;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Memory {

    private Enclosure[] enclosures;
    private Pair<Integer, Enclosure>[] indexEnclosureForMap;
    private Animal.PersonalStories[] animalStories;
    private Misc[] miscMarkers;
    private MapResult mapResult;
    private WallFeed[] wallFeeds;
    private ContactInfoResult contactInfoResult;
    private OpeningHoursResult openingHoursResult;
    private Price[] prices;
    private String aboutUs;
    private Map<Integer, List<CustomRelativeLayout>> enclosuresAnimalCardMap;
    private CustomRelativeLayout[] enclosureCards;

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
        this.enclosuresAnimalCardMap = new HashMap<>();

        initializeBitmaps();


        // just for the map
        List<Pair<Integer, Enclosure>> enclosureList = new ArrayList<>();
        for (int i = 0; i < enclosures.length; i++)
            if (enclosures[i].getMarkerBitmap() != null) {
                enclosureList.add(new Pair<>(i, enclosures[i]));
            }
        indexEnclosureForMap = new Pair[enclosureList.size()];
        enclosureList.toArray(indexEnclosureForMap);
        Arrays.sort(indexEnclosureForMap, (iep1, iep2) -> iep1.second.getMarkerY() - iep2.second.getMarkerY());
        // reorders them so they would be better looking on the map, sadly cannot be done on the enclosures..
        Arrays.sort(miscMarkers, (misc1, misc2) -> misc1.getMarkerY()-misc2.getMarkerY());


        List<Point> points = new ArrayList<>();
        Map<Point, Set<Point>> routes = new HashMap<>();
        points.add(new Point(mapResult.getMapInfo().getRoutes()[0].getX1(), mapResult.getMapInfo
                ().getRoutes()[0].getY1()));

        Point lastAdded = points.get(0);
        routes.put(lastAdded, new HashSet<>());

        for (MapResult.MapInfo.Route route :
                mapResult.getMapInfo().getRoutes()) {
            if(lastAdded.getX() != route.getX1() || lastAdded.getY() != route.getY1())
            {
                lastAdded = new Point(route.getX1(), route.getY1());
                points.add(lastAdded);
                if(!routes.containsKey(lastAdded))
                    routes.put(lastAdded, new HashSet<>());
            }
            Point p2 = new Point(route.getX2(), route.getY2());
            routes.get(lastAdded).add(p2);
            if(!routes.containsKey(p2))
                routes.put(p2, new HashSet<>());
            routes.get(p2).add(lastAdded);
        }

        mapResult.getMapInfo().setPoints(points.toArray(new Point[points.size()]));
        mapResult.getMapInfo().setRoutesMap(routes);
    }

    public Pair<Integer, Enclosure>[] getIndexEnclosureForMap() {
        return indexEnclosureForMap;
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

    public Map<Integer, List<CustomRelativeLayout>> getEnclosuresAnimalCardMap() {
        return enclosuresAnimalCardMap;
    }

    public void setEnclosuresAnimalCardMap(Map<Integer, List<CustomRelativeLayout>> enclosuresAnimalCardMap) {
        this.enclosuresAnimalCardMap = enclosuresAnimalCardMap;
    }

    public CustomRelativeLayout[] getEnclosureCards() {
        return enclosureCards;
    }

    public void setEnclosureCards(CustomRelativeLayout[] enclosureCards) {
        this.enclosureCards = enclosureCards;
    }
}

