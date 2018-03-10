package com.zoovisitors.bl;

import com.zoovisitors.backend.AboutZoo;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Species;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    public void getAnimals(int pos, final GetObjectInterface goi);
    public void getEnclosures(final GetObjectInterface goi);
    public void getAboutZoo();
    public void getNewsFeed(final GetObjectInterface goi);
    public void getSpecies();
}
