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
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class pricesFragment extends Fragment {

    public pricesFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.info_table_layout, container, false);

        GlobalVariables.bl.getPrices(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                Price[] prices = (Price[]) response;
                TableLayout pricesTable = (TableLayout) rootView.findViewById(R.id.info_table_table);
                int textSize = 24;
                //Build the table for the prices
                for (Price p : prices) {
                    TableRow tbr = new TableRow(getContext());
                    ViewGroup.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT);
                    tbr.setLayoutParams(lp);
                    TextView popColumn = new TextView(getContext()); //column of the population
                    TextView priceColumn = new TextView(getContext()); //column of the prices
                    popColumn.setText(p.getPopulation());
                    priceColumn.setText("" + p.getPricePop() + "₪");
                    popColumn.setWidth(700);
                    priceColumn.setWidth(90);
                    priceColumn.setGravity(Gravity.CENTER);
                    popColumn.setGravity(Gravity.LEFT);
                    popColumn.setTextSize(textSize);
                    priceColumn.setTextSize(textSize);
                    tbr.addView(popColumn);
                    tbr.addView(priceColumn);
                    pricesTable.addView(tbr);
                }
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
