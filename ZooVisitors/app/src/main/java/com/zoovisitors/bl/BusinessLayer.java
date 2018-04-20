package com.zoovisitors.bl;

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
    void sendDeviceId();

    void getImage(String url, GetObjectInterface goi);
}
