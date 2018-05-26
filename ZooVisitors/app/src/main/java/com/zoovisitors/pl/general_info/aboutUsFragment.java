package com.zoovisitors.pl.general_info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.AboutUs;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class aboutUsFragment extends Fragment {


    public aboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.info_table_layout, container, false);
        GlobalVariables.bl.getAboutUs(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                TextView aboutUs = (TextView) rootView.findViewById(R.id.error_info_text);
                aboutUs.setText(((AboutUs[]) response)[0].getAboutUs());
            }

            @Override
            public void onFailure(Object response) {
                TextView errorText = (TextView) rootView.findViewById(R.id.error_info_text);
                errorText.setText((String) response);
            }
        });

        return rootView;
    }

}
