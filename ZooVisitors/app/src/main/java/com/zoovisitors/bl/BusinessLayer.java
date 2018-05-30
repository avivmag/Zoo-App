package com.zoovisitors.bl;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.backend.callbacks.UpdateInterface;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {

    void getAnimals(int pos, final GetObjectInterface goi);
    void getAllAnimals(final GetObjectInterface goi);
    /**
     * Returns the enclosures that has been loaded in the load screen
     */
    Enclosure[] getEnclosures();
    void getEnclosures(final GetObjectInterface goi);
    /**
     * Returns the miscs that has been loaded in the load screen
     */
    Misc[] getMiscs();
    //    void getRecurringEvents(final GetObjectInterface goi);
    void getNewsFeed(final GetObjectInterface goi);
    void getSchedule(final GetObjectInterface goi);
    void getPrices(final GetObjectInterface goi);
    void getOpeningHours(final GetObjectInterface goi);
    void getAboutUs(final GetObjectInterface goi);
    void getContactInfo(final GetObjectInterface goi);
    /**
     * Returns the personal stories that has been loaded in the load screen
     */
    Animal.PersonalStories[] getPersonalStories();
    void getPersonalStories(final GetObjectInterface goi);

    //notification
    void updateIfInPark(boolean isInPark, final GetObjectInterface goi);
    void unsubscribeToNotification(final GetObjectInterface goi);

    //Images
    void getImage(String url, int width, int height, GetObjectInterface goi);
    void getImageFullUrl(String url, int width, int height, GetObjectInterface goi);

    //Audio
    void getAudio(String url, final GetObjectInterface goi);

    //All data
    void getAllDataInit(final UpdateInterface updateInterface);

    void insertStringandBitmap(String s, Bitmap d);
    Bitmap getBitmapByString(String s);
    MapResult getMapResult();
}
