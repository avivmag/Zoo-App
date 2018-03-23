package com.zoovisitors.bl;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    void getAnimals(int pos, final GetObjectInterface goi);
    void getAllAnimals(final GetObjectInterface goi);
    void getEnclosures(final GetObjectInterface goi);
    void getImage(String url, GetObjectInterface goi);
    void getNewsFeed(final GetObjectInterface goi);
    void getSpecies();
    void getSchedule(final GetObjectInterface goi);
    void getPrices(final GetObjectInterface goi);
    void getOpeningHours(final GetObjectInterface goi);
    void getAboutUs(final GetObjectInterface goi);
    void getContactInfo(final GetObjectInterface goi);
}
