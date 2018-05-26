package com.zoovisitors.pl.general_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.AboutUs;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.TextViewRegularText;

/**
 * A simple {@link Fragment} subclass.
 */
public class aboutUsFragment extends Fragment {

    public aboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_table_layout, container, false);

        GlobalVariables.bl.getAboutUs(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                LinearLayout infoTableLayout = rootView.findViewById(R.id.info_table_layout);
                infoTableLayout.removeAllViews();

                TextViewRegularText aboutUs = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER);
                aboutUs.setPadding(0,150,0,0);
                aboutUs.setText(((AboutUs[]) response)[0].getAboutUs());

                infoTableLayout.addView(aboutUs);
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
