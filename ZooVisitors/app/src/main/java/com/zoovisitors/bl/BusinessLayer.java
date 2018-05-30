package com.zoovisitors.bl;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.backend.callbacks.UpdateInterface;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {

    void getAnimals(int pos, final GetObjectInterface goi);
    void getAllAnimals(final GetObjectInterface goi);
    void getEnclosures(final GetObjectInterface goi);
    void getMisc(final GetObjectInterface goi);
//    void getRecurringEvents(final GetObjectInterface goi);
    void getNewsFeed(final GetObjectInterface goi);
    void getSchedule(final GetObjectInterface goi);
    void getPrices(final GetObjectInterface goi);
    void getOpeningHours(final GetObjectInterface goi);
    void getAboutUs(final GetObjectInterface goi);
    void getContactInfo(final GetObjectInterface goi);
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
}
