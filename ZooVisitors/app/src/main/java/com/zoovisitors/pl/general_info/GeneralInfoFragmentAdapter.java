package com.zoovisitors.pl.general_info;

/**
 * Created by oripa on 12/28/2017.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.zoovisitors.R;

public class GeneralInfoFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public GeneralInfoFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new aboutUsFragment();
        } else if (position == 1){
            return new pricesFragment();
        } else if (position == 2) {
            return new openingHoursFragment();
        } else {
            return new contactInfoFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.about_us);
            case 1:
                return mContext.getString(R.string.prices);
            case 2:
                return mContext.getString(R.string.opening_hours);
            case 3:
                return mContext.getString(R.string.contact_info);
            default:
                return null;
        }
    }
}
