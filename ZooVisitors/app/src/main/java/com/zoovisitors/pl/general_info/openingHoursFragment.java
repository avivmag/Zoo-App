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
import com.zoovisitors.backend.OpeningHours;
import com.zoovisitors.bl.GetObjectInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class openingHoursFragment extends Fragment {


    public openingHoursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup                  container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.info_table_layout, container, false);
        GlobalVariables.bl.getOpeningHours(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                OpeningHours[] openingHours = (OpeningHours[]) response;
                TableLayout openingHoursTable = (TableLayout) rootView.findViewById(R.id.info_table_table);
                int textSize = 14;
                for (OpeningHours oh : openingHours){
                    TableRow tbr = new TableRow(getContext());

//                    ViewGroup.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT);
//                    tbr.setLayoutParams(lp);
                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,0,0,40);
                    tbr.setLayoutParams(lp);
                    TextView dayColumn = new TextView(getContext()); //column of the day
                    TextView startTimeColumn = new TextView(getContext()); //column of the start time
                    TextView endTimeColumn = new TextView(getContext()); //column of the end time
                    dayColumn.setText(oh.getDay());
                    startTimeColumn.setText("" + fixTime(oh.getStartHour()) + ":" + fixTime(oh.getStartMin()));
                    endTimeColumn.setText("" + fixTime(oh.getEndHour()) + ":" + fixTime(oh.getEndMin()));

                    TableRow.LayoutParams clp = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0,0,0,10);
                    dayColumn.setLayoutParams(clp);
                    startTimeColumn.setLayoutParams(clp);
                    endTimeColumn.setLayoutParams(clp);
                    dayColumn.setWidth(150);
                    startTimeColumn.setWidth(2);
                    endTimeColumn.setWidth(70);
                    startTimeColumn.setGravity(Gravity.CENTER);
                    endTimeColumn.setGravity(Gravity.LEFT);
                    dayColumn.setGravity(Gravity.CENTER);
                    dayColumn.setTextSize(textSize);
                    startTimeColumn.setTextSize(textSize);
                    endTimeColumn.setTextSize(textSize);
                    tbr.addView(dayColumn);
                    tbr.addView(startTimeColumn);
                    tbr.addView(endTimeColumn);
                    openingHoursTable.addView(tbr);
                }
            }

            @Override
            public void onFailure(Object response) {

            }
        });

        return rootView;
    }

    private String fixTime(int time){
        return time < 10 ? "0" + time : "" + time;
    }
}
