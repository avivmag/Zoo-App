package com.zoovisitors.pl.general_info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoovisitors.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class contactInfoFragment extends Fragment {


    public contactInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_info_res, container, false);

        return rootView;
    }

}
