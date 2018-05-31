package com.zoovisitors.pl.schedule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;
import com.zoovisitors.pl.general_info.WatchAll;

import java.time.LocalDateTime;

public class ScheduleActivity extends BaseActivity {

    private Schedule[] schedulers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setActionBar(R.color.blueIcon);

        Button todayEventsButton = findViewById(R.id.todays_events_button);
        todayEventsButton.setOnClickListener(v -> {
            Intent recEveAll = new Intent(getBaseContext(), TodayRecEvent.class);
            startActivity(recEveAll);
        });

        //get all the schedule
        GlobalVariables.bl.getSchedule(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                schedulers = (Schedule[]) response;

                int screenWidth = getResources().getDisplayMetrics().widthPixels;

                //get the list layout
                LinearLayout scheduleListLayout = findViewById(R.id.schedule_list);

                for (Schedule s: schedulers) {


                    //initiates the text layout
                    LinearLayout textLayout = new LinearLayout(getBaseContext());
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textParams.weight = 1;
                    textLayout.setLayoutParams(textParams);
                    textLayout.setOrientation(LinearLayout.VERTICAL);
                    textLayout.setBaselineAligned(false);


                    //initiates the date TextView
                    TextViewRegularText dateText;
                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        dateText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
                    }
                    else{
                        dateText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
                    }
                    dateText.setText(s.getStartTime().substring(0, 10) + " - " + s.getEndTime().substring(0, 10));

                    //initiates the title TextView
                    TextViewTitle titleText = new TextViewTitle(getBaseContext(),View.TEXT_ALIGNMENT_TEXT_START);
                    titleText.setText(s.getTitle());

                    //initiates the description TextView
                    TextViewRegularText descText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
                    descText.setText(s.getDescription());

                    textLayout.addView(dateText,0);
                    textLayout.addView(titleText,1);
                    textLayout.addView(descText,2);

                    //initiates the ImageView
                    ImageView image = new ImageView(getBaseContext());


                    GlobalVariables.bl.getImage(s.getImageUrl(), 200, 200, new GetObjectInterface() {

                        @Override
                        public void onSuccess(Object response) {
                            image.setImageBitmap((Bitmap) response);
                            ((LinearLayout.LayoutParams)image.getLayoutParams()).weight = 0;
                            GlobalVariables.bl.insertStringandBitmap(s.getImageUrl(), (Bitmap) response);
                        }

                        @Override
                        public void onFailure(Object response) {

                            GlobalVariables.bl.insertStringandBitmap(s.getImageUrl(), BitmapFactory.decodeResource(
                                    GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));
                        }
                    });



                    //initiates a schedule layout
                    LinearLayout scheduleLayout = new LinearLayout(getBaseContext());
                    LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    relativeParams.weight = 3;
                    scheduleLayout.setLayoutParams(relativeParams);
                    scheduleLayout.setOrientation(LinearLayout.HORIZONTAL);
                    scheduleLayout.setBackground(getResources().getDrawable(R.drawable.dashed_bottom_line));

                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        scheduleLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                    else
                        scheduleLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                    scheduleLayout.addView(textLayout);
                    scheduleLayout.addView(image);

                    scheduleListLayout.addView(scheduleLayout);
                }
            }

            @Override
            public void onFailure(Object response) {
                TextView error  = (TextView) findViewById(R.id.error_sched_text);
                error.setVisibility(View.VISIBLE);
                error.setText((String) response);
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
}