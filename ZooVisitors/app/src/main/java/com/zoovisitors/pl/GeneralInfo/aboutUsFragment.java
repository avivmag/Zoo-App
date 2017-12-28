package com.zoovisitors.pl.GeneralInfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.FragmentPagerAdapter;

import com.zoovisitors.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class aboutUsFragment extends Fragment {


    public aboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_us_res, container, false);

        return rootView;
    }

}
