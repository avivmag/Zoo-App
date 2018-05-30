package com.zoovisitors.pl.schedule;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;

import java.time.LocalDateTime;

public class ScheduleActivity extends BaseActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Schedule[] schedulers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setActionBar(R.color.blueIcon);

        //get all the schedule
        GlobalVariables.bl.getSchedule(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                schedulers = (Schedule[]) response;

                int screenWidth = getResources().getDisplayMetrics().widthPixels;

                //get the list layout
                LinearLayout scheduleListLayout = findViewById(R.id.schedule_list);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                for (Schedule s: schedulers) {

                    //initiates a schedule layout
                    LinearLayout scheduleLayout = new LinearLayout(getBaseContext());
                    params.setMargins(0,10,0,10);
                    scheduleLayout.setLayoutParams(params);
                    scheduleLayout.setOrientation(LinearLayout.HORIZONTAL);
                    scheduleLayout.setBackground(getResources().getDrawable(R.drawable.up_down_border));
                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        scheduleLayout.setTextDirection(View.TEXT_DIRECTION_RTL);
                    }
                    else
                        scheduleLayout.setTextDirection(View.TEXT_DIRECTION_LTR);

                    //initiates the text layout
                    LinearLayout textLayout = new LinearLayout(getBaseContext());
                    params.width = screenWidth/2*3;
                    params.setMargins(5,0,5,0);
                    textLayout.setLayoutParams(params);
                    textLayout.setOrientation(LinearLayout.VERTICAL);

                    //initiates the date TextView
                    TextViewRegularText dateText;
                    if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                        dateText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_END);
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
                    params.width = screenWidth/3;
                    image.setLayoutParams(params);
                    GlobalVariables.bl.getImage(s.getImageUrl(), 200, 200, new GetObjectInterface() {

                        @Override
                        public void onSuccess(Object response) {
                            image.setImageBitmap((Bitmap) response);
//                            viewHolder.image.setImageBitmap();
                            GlobalVariables.bl.insertStringandBitmap(s.getImageUrl(), (Bitmap) response);
                        }

                        @Override
                        public void onFailure(Object response) {

                            GlobalVariables.bl.insertStringandBitmap(s.getImageUrl(), BitmapFactory.decodeResource(
                                    GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));
                        }
                    });

                    scheduleListLayout.addView(textLayout);
                    scheduleLayout.addView(image);
                }

//                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
//                layoutManager = new LinearLayoutManager(GlobalVariables.appCompatActivity);
//                recycleView.setLayoutManager(layoutManager);
//                adapter = new ScheduleRecyclerAdapter(schedulers);
//                recycleView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Object response) {
                TextView error  = (TextView) findViewById(R.id.error_sched_text);
//                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
//                recycleView.setVisibility(View.INVISIBLE);
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