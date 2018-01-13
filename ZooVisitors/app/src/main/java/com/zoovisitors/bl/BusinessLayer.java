package com.zoovisitors.bl;

import com.zoovisitors.backend.AboutZoo;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.backend.Species;

/**
 * Created by Gili on 13/01/2018.
 */

public interface BusinessLayer {


    public Animal[] getAnimals();
    public Enclosure[] getEnclosures();
    public AboutZoo getAboutZoo();
    public NewsFeed getNewsFeed();
    public Species getSpecies();
}
