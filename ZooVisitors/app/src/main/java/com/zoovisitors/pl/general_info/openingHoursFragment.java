package com.zoovisitors.pl.general_info;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.OpeningHours;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class openingHoursFragment extends Fragment {


    public openingHoursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.info_table_layout, container, false);
        GlobalVariables.bl.getOpeningHours(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                OpeningHours[] openingHours = (OpeningHours[]) response;
                TableLayout openingHoursTable = (TableLayout) rootView.findViewById(R.id.info_table_table);
                int textSize = 14;
                ((ImageView) rootView.findViewById(R.id.info_table_image)).setImageResource(R.mipmap.aligator);

                TextView tv1 = (TextView) rootView.findViewById(
                        R.id.firstColInfoTbl);

                TextView tv2 = (TextView) rootView.findViewById(
                        R.id.secColInfoTbl);

                if (GlobalVariables.language == 1) {
                    tv1.setText(getResources().getString(R.string.hours));
                    tv2.setText(getResources().getString(R.string.days));
                }
                else
                {
                    tv1.setText(getResources().getString(R.string.days));
                    tv2.setText(getResources().getString(R.string.hours));
                }

                //Build the table for the opening hours
                for (OpeningHours oh : openingHours) {
                    TableRow tbr = new TableRow(getContext());
                    TextView dayColumn = new TextView(getContext()); //column of the day
                    TextView hoursTimeColumn = new TextView(getContext()); //column of the start time
                    dayColumn.setText(oh.getDay());
                    hoursTimeColumn.setText(oh.getStartTime() + " - " + oh.getEndTime());

                    if (GlobalVariables.language == 1) {
                        tbr.addView(hoursTimeColumn);
                        tbr.addView(dayColumn);
                    }
                    else
                    {
                        tbr.addView(dayColumn);
                        tbr.addView(hoursTimeColumn);
                    }

                    openingHoursTable.addView(tbr);

                }
                //Build the table for the opening hours
//                for (OpeningHours oh : openingHours){
//                    TableRow tbr = new TableRow(getContext());
//                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
//                    //lp.setMargins(0,0,0,40);
//                    //tbr.setLayoutParams(lp);
//                    TextView dayColumn = new TextView(getContext()); //column of the day
//                    TextView startTimeColumn = new TextView(getContext()); //column of the start time
//                    TextView endTimeColumn = new TextView(getContext()); //column of the end time
//                    dayColumn.setText(oh.getDay());
//                    startTimeColumn.setText(oh.getStartTime());
//                    endTimeColumn.setText(oh.getEndTime());
//
//                    //TableRow.LayoutParams clp = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
//                    lp.setMargins(0,0,0,10);
//                    //dayColumn.setLayoutParams(clp);
//                    //startTimeColumn.setLayoutParams(clp);
//                    //endTimeColumn.setLayoutParams(clp);
//                    //dayColumn.setWidth(150);
//                    //startTimeColumn.setWidth(2);
//                    //endTimeColumn.setWidth(70);
//                    //startTimeColumn.setGravity(Gravity.CENTER);
//                    //endTimeColumn.setGravity(Gravity.LEFT);
//                    //dayColumn.setGravity(Gravity.CENTER);
//                    dayColumn.setTextSize(textSize);
//                    startTimeColumn.setTextSize(textSize);
//                    endTimeColumn.setTextSize(textSize);
//                    tbr.addView(dayColumn);
//                    tbr.addView(startTimeColumn);
//                    tbr.addView(endTimeColumn);
//                    openingHoursTable.addView(tbr);
//                }
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
