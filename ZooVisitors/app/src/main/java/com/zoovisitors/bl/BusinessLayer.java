package com.zoovisitors.bl;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    public void getAnimals(int pos, final GetObjectInterface goi);
    public void getAllAnimals(final GetObjectInterface goi);
    public void getEnclosures(final GetObjectInterface goi);
    public void getNewsFeed(final GetObjectInterface goi);
    public void getSchedule(final GetObjectInterface goi);
    public void getPrices(final GetObjectInterface goi);
    public void getOpeningHours(final GetObjectInterface goi);
    public void getAboutUs(final GetObjectInterface goi);
    public void getContactInfo(final GetObjectInterface goi);

    //notification
    public void sendDeviceId();

    void getImage(String url, GetObjectInterface goi);
}
