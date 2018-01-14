package com.zoovisitors.bl;

import com.zoovisitors.backend.AboutZoo;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.backend.Species;
import com.zoovisitors.cl.network.ResponseInterface;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    public void getAnimals(int pos, final GetObjectInterface goi);
    public Enclosure[] getEnclosures();
    public AboutZoo getAboutZoo();
    public NewsFeed getNewsFeed();
    public Species getSpecies();
}
