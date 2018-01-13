package com.zoovisitors.pl.animals;

/**
 * Created by oripa on 12/28/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zoovisitors.R;

public class AnimalFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    private AppCompatActivity appCompatActivity;
    private String story;
    private String species;

    // tab titles
    private String[] tabTitles = new String[]{"about_animal", "about_species"};

    public AnimalFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            aboutAnimalFragment aboutAnimalFragment =  new aboutAnimalFragment();
            aboutAnimalFragment.setAppCompatActivity(appCompatActivity);
            aboutAnimalFragment.setStory(story);
            return aboutAnimalFragment;
        } else {
            return new aboutSpeciesFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.about_animal);
            case 1:
                return mContext.getString(R.string.about_species);
            default:
                return null;
        }
    }
}
