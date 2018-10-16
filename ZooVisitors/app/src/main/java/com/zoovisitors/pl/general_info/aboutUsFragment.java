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
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.TextViewRegularText;

/**
 * A simple {@link Fragment} subclass.
 */
public class aboutUsFragment extends Fragment {

    private LinearLayout infoTableLayout;

    public aboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.info_table_layout, container, false);

        GlobalVariables.bl.getAboutUs(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                infoTableLayout = rootView.findViewById(R.id.info_table_layout);
                infoTableLayout.removeAllViews();

                TextViewRegularText aboutUs = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER);
                String aboutUsString = (String) response;
                if (aboutUsString == null){
                    addErrorMessage();
                }
                else {
                    aboutUs.setText(aboutUsString);
                }
                infoTableLayout.addView(aboutUs);
            }

            @Override
            public void onFailure(Object response) {
                addErrorMessage();
            }
        });

        return rootView;
    }

    private void addErrorMessage() {
        TextView error = new TextView(GlobalVariables.appCompatActivity);
        error.setVisibility(View.VISIBLE);
        error.setGravity(Gravity.CENTER_HORIZONTAL);
        error.setTextColor(getResources().getColor(R.color.black));
        error.setTextSize(20);
        error.setText(R.string.error_no_about_us);

        infoTableLayout.addView(error);
    }
}
