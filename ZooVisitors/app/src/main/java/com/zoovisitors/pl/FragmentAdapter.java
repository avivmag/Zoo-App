package com.zoovisitors.pl;

/**
 * Created by oripa on 12/28/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.zoovisitors.R;

public class FragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;

    // tab titles
    private String[] tabTitles = new String[]{"about_animal", "about_species"};

    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new about_animal_fragment();
        } else {
            return new about_species_fragment();
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
