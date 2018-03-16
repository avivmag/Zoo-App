package com.zoovisitors.bl;

import com.zoovisitors.backend.AboutZoo;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Species;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    void getAnimals(int pos, final GetObjectInterface goi);
    void getEnclosures(int pos, final GetObjectInterface goi);
    void getAboutZoo();
    void getNewsFeed(final GetObjectInterface goi);
    void getSpecies();
}
