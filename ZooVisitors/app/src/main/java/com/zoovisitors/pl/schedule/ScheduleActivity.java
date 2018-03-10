package com.zoovisitors.pl.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private AppCompatActivity tempActivity = this;
    private Schedule[] schedulers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Delete when I get object from server
        this.schedulers = new Schedule[10];
        for (int i = 0; i<10; i++){
            this.schedulers[i] = new Schedule();
            this.schedulers[i].setName("" + i);
        }

        recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
        layoutManager = new LinearLayoutManager(tempActivity);
        recycleView.setLayoutManager(layoutManager);

        adapter = new ScheduleRecyclerAdapter(tempActivity, schedulers);
        recycleView.setAdapter(adapter);

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