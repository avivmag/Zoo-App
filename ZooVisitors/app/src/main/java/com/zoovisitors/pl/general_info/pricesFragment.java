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
public class pricesFragment extends Fragment {


    public pricesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prices_res, container, false);

        return rootView;
    }

}
