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
import android.widget.TableRow;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.ContactInfo;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.TextViewTitle;

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

        //get all the contact hours
        GlobalVariables.bl.getContactInfo(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                ContactInfo[] contactInfos = (ContactInfo[]) response;

                TableLayout contactInfoTable = (TableLayout) rootView.findViewById(R.id.info_table_table);

                ((ImageView) rootView.findViewById(R.id.info_table_image)).setImageResource(R.mipmap.swan);

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

                //way title
                TextViewTitle wayTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);
                wayTitle.setText(getContext().getResources().getString(R.string.contact_info));
                wayTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                wayTitle.setWidth(cellWidth);
                wayTitle.setPadding(0,10,0,10);

                //address title
                TextViewTitle addressTitle = new TextViewTitle(getContext(), View.TEXT_ALIGNMENT_CENTER);

                addressTitle.setText(getContext().getResources().getString(R.string.conatct_address));
                addressTitle.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                addressTitle.setWidth(cellWidth);
                addressTitle.setPadding(0,10,0,10);

                //add the titles to the linear layout
                titleLayout.addView(wayTitle);
                titleLayout.addView(addressTitle);

                //add the linear layout to the table
                contactInfoTable.addView(titleLayout);

                //Build the table for the info
                for (ContactInfo ci : contactInfos) {
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

                    //via cell
                    TextView viaColumn = new TextView(getContext()); //column of the via
                    viaColumn.setText(ci.getVia());
                    viaColumn.setTextSize(12);
                    viaColumn.setTextColor(getResources().getColor(R.color.black));
                    viaColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    viaColumn.setWidth(cellWidth);
                    viaColumn.setHeight(120);
                    viaColumn.setPadding(0,10,0,10);
                    viaColumn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    viaColumn.setIncludeFontPadding(false);
                    viaColumn.setGravity(Gravity.CENTER);


                    //address cell
                    TextView addressColumn = new TextView(getContext()); //column of the address
                    addressColumn.setText("" + ci.getAddress());
                    addressColumn.setTextSize(12);
                    addressColumn.setTextColor(getResources().getColor(R.color.black));
                    addressColumn.setBackground(getContext().getResources().getDrawable(R.drawable.cell_border_shape));
                    addressColumn.setWidth(cellWidth);
                    addressColumn.setHeight(120);
                    addressColumn.setPadding(0,10,0,10);
                    addressColumn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    addressColumn.setIncludeFontPadding(false);
                    addressColumn.setGravity(Gravity.CENTER);

                    rowLayout.addView(viaColumn);
                    rowLayout.addView(addressColumn);

                    contactInfoTable.addView(rowLayout);
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
