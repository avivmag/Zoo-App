package com.zoovisitors.pl.general_info;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Price;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;

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

        //get all the prices.
        GlobalVariables.bl.getPrices(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                Price[] prices = (Price[]) response;

                TableLayout pricesTable = (TableLayout) rootView.findViewById(R.id.info_table_table);

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

                //population title
                TextViewTitle popTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);
                popTitle.setText(getContext().getResources().getString(R.string.population));
                popTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                popTitle.setWidth(cellWidth);
                popTitle.setPadding(0,10,0,10);

                //price
                TextViewTitle priceTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);

                priceTitle.setText(getContext().getResources().getString(R.string.prices));
                priceTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                priceTitle.setWidth(cellWidth);
                priceTitle.setPadding(0,10,0,10);

                titleLayout.addView(popTitle);
                titleLayout.addView(priceTitle);

                pricesTable.addView(titleLayout);

                //Build the rest of the prices table
                for (Price p : prices) {
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
                    rowLayout.setBaselineAligned(false);
                    rowLayout.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));

                    //pop cell
                    TextViewRegularText popColumn = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER); //column of the population
                    popColumn.setText(p.getPopulation());
                    popColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    popColumn.setWidth(cellWidth);
                    popColumn.setHeight(150);
                    popColumn.setPadding(0,10,0,10);
                    popColumn.setGravity(Gravity.CENTER);

                    //price cell
                    TextViewRegularText priceColumn = new TextViewRegularText(getContext(), View.TEXT_ALIGNMENT_CENTER); //column of the prices
                    priceColumn.setText("" + p.getPricePop() + "₪");
                    priceColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    priceColumn.setWidth(cellWidth);
                    priceColumn.setHeight(150);
                    priceColumn.setPadding(0,10,0,10);
                    priceColumn.setGravity(Gravity.CENTER);

                    rowLayout.addView(popColumn);
                    rowLayout.addView(priceColumn);

                    pricesTable.addView(rowLayout);
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
