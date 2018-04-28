package com.zoovisitors.pl.schedule;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;


public class ScheduleActivity extends BaseActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Schedule[] schedulers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.logo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Send notification
        //MyFirebaseMessagingService.sendSelfNotification(MapActivity.class, "האכלת אריות","13:00 - 13:30");

        GlobalVariables.bl.getSchedule(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                schedulers = (Schedule[]) response;
                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
                layoutManager = new LinearLayoutManager(GlobalVariables.appCompatActivity);
                recycleView.setLayoutManager(layoutManager);
                adapter = new ScheduleRecyclerAdapter(schedulers);
                recycleView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Object response) {
                TextView error  = (TextView) findViewById(R.id.error_sched_text);
                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
                recycleView.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
                error.setText((String) response);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}