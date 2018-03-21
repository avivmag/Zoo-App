package com.zoovisitors.pl.general_info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.ContactInfo;
import com.zoovisitors.bl.GetObjectInterface;

import javax.microedition.khronos.opengles.GL;

/**
 * A simple {@link Fragment} subclass.
 */
public class contactInfoFragment extends Fragment {


    public contactInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.info_table_layout, container, false);

        GlobalVariables.bl.getContactInfo(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                ContactInfo[] prices = (ContactInfo[]) response;
                TableLayout pricesTable = (TableLayout) rootView.findViewById(R.id.info_table_table);

                int textSize = 16;
                for (ContactInfo ci : prices) {
                    TableRow tbr = new TableRow(getContext());
                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,0,0,40);
                    tbr.setLayoutParams(lp);
                    TextView viaColumn = new TextView(getContext()); //column of the via
                    TextView addressColumn = new TextView(getContext()); //column of the address
                    viaColumn.setText(ci.getVia());
                    addressColumn.setText("" + ci.getAddress());
                    viaColumn.setWidth(400);
                    addressColumn.setWidth(400);
                    addressColumn.setGravity(Gravity.LEFT);
                    viaColumn.setGravity(Gravity.LEFT);
                    viaColumn.setTextSize(textSize);
                    addressColumn.setTextSize(textSize);
                    tbr.addView(viaColumn);
                    tbr.addView(addressColumn);
                    pricesTable.addView(tbr);
                }
            }

            @Override
            public void onFailure(Object response) {

            }
        });

        return rootView;
    }

}
