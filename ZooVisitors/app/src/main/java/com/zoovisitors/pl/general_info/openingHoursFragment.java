package com.zoovisitors.pl.general_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;

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

        //get all the opening hours
        GlobalVariables.bl.getOpeningHours(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                OpeningHoursResult openingHoursResult = (OpeningHoursResult) response;


                TableLayout openingHoursTable = (TableLayout) rootView.findViewById(R.id.info_table_table);

                ((ImageView) rootView.findViewById(R.id.info_table_image)).setImageResource(R.mipmap.aligator);

                //calculate the screen width.
                int screenSize = GlobalVariables.appCompatActivity.getResources().getDisplayMetrics().widthPixels;
                int spaces = 20;
                int cellWidth = screenSize/2 - spaces;

                //add the titles to the table
                LinearLayout titleLayout = new LinearLayout(getContext());
                titleLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                titleLayout.setLayoutParams(params);
                if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                    titleLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
                else{
                    titleLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                }

                //day title
                TextViewTitle dayTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);
                dayTitle.setText(getContext().getResources().getString(R.string.days));
                dayTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                dayTitle.setWidth(cellWidth);
                dayTitle.setPadding(0,10,0,10);

                //hours title
                TextViewTitle hoursTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);

                hoursTitle.setText(getContext().getResources().getString(R.string.hours));
                hoursTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                hoursTitle.setWidth(cellWidth);
                hoursTitle.setPadding(0,10,0,10);

                //add the titles to the linear layout
                titleLayout.addView(dayTitle);
                titleLayout.addView(hoursTitle);

                //add the linear layout to the table
                openingHoursTable.addView(titleLayout);

                //Build the table for the opening hours
                for (OpeningHoursResult.OpeningHours oh : openingHoursResult.getOpeningHours()) {
                    LinearLayout rowLayout = new LinearLayout(getContext());
                    rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    rowLayout.setLayoutParams(rowParams);
                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        rowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                    else{
                        rowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    }

                    //day cell
                    TextViewRegularText dayColumn = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER);
                    dayColumn.setText(oh.getDay());
                    dayColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    dayColumn.setWidth(cellWidth);
                    dayColumn.setPadding(0,20,0,20);
                    dayColumn.setGravity(Gravity.CENTER);

                    //hours title
                    TextViewRegularText hoursTimeColumn = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER); //column of the start time
                    hoursTimeColumn.setText(oh.getStartTime() + " - " + oh.getEndTime());
                    hoursTimeColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    hoursTimeColumn.setWidth(cellWidth);
                    hoursTimeColumn.setPadding(0,20,0,20);
                    hoursTimeColumn.setGravity(Gravity.CENTER);

                    rowLayout.addView(dayColumn);
                    rowLayout.addView(hoursTimeColumn);

                    openingHoursTable.addView(rowLayout);
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
