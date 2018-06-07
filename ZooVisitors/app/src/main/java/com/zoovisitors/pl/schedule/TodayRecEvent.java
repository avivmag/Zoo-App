package com.zoovisitors.pl.schedule;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TodayRecEvent extends BaseActivity {

    Enclosure.RecurringEventString[] allRecEvents;
    LinearLayout allRecEveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_rec_event);
        allRecEveList = findViewById(R.id.all_rec_events);

        //calculate the window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.85), (int) (height * 0.9));

        List<Enclosure.RecurringEventString> todaysEvents = new ArrayList<>();
        //get all the news feed
        GlobalVariables.bl.getAllRecEvents(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                allRecEvents = (Enclosure.RecurringEventString[]) response;
                boolean found = false;

                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                for (Enclosure.RecurringEventString rec: allRecEvents) {
                    if (rec.getDay() == day)
                        todaysEvents.add(rec);
                }

                TextView recEveTitle = findViewById(R.id.rec_events_popup_title);
                recEveTitle.setPaintFlags(recEveTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                recEveTitle.setTypeface(null, Typeface.BOLD);

                for (Enclosure.RecurringEventString rec : todaysEvents) {
                    //initiate the recurring Events layout
                    LinearLayout recLayout = new LinearLayout(getBaseContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    recLayout.setBackground(getResources().getDrawable(R.drawable.dashed_bottom_line));
                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        recLayout.setGravity(Gravity.RIGHT);
                    }

                    recLayout.setLayoutParams(params);
                    recLayout.setOrientation(LinearLayout.VERTICAL);

                    //initiate the title
                    TextViewTitle titleText = new TextViewTitle(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
                    titleText.setText(rec.getTitle());

                    //initiate the time
                    TextView dateText = new TextView(getBaseContext());
                    dateText.setTextColor(getResources().getColor(R.color.black));
                    dateText.setTextSize(12);
                    dateText.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                    dateText.setIncludeFontPadding(false);

                    String text = "["+ rec.getStartTime() + "-" + rec.getEndTime() + "]";
                    dateText.setText(text);


                    //initiate the description TextView
                    TextViewRegularText regularText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_GRAVITY);
                    regularText.setText(rec.getDescription());

                    recLayout.addView(titleText);
                    recLayout.addView(dateText);
                    recLayout.addView(regularText);

                    allRecEveList.addView(recLayout);
                    found = true;
                }

                if (!found){
                    addErrorMessage();
                }
            }

            @Override
            public void onFailure(Object response) {
                addErrorMessage();
            }
        });
    }

    private void addErrorMessage() {
        TextView error = new TextView(getBaseContext());
        error.setVisibility(View.VISIBLE);
        error.setGravity(Gravity.CENTER_HORIZONTAL);
        error.setTextColor(getResources().getColor(R.color.black));
        error.setTextSize(20);
        error.setText(R.string.error_no_rec_events);

        allRecEveList.addView(error);
    }
}
