package com.zoovisitors.pl.animals;

/**
 * Created by oripa on 12/28/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;

public class AnimalFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private Animal animal;

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
            aboutAnimalFragment.setAnimal(animal);
            return aboutAnimalFragment;
        } else {
            aboutSpeciesFragment aboutSpeciesFragment = new aboutSpeciesFragment();
            aboutSpeciesFragment.setAnimal(animal);

            return aboutSpeciesFragment;
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

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
