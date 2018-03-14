package com.zoovisitors.pl.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private AppCompatActivity tempActivity = this;
    private Schedule[] schedulers;
    private BusinessLayer bl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        bl = new BusinessLayerImpl(this);

//        //Delete when I get object from server
//        this.schedulers = new Schedule[10];
//        for (int i = 0; i<10; i++){
//            this.schedulers[i] = new Schedule();
//            this.schedulers[i].setName("" + i);
//        }

        bl.getSchedule(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                schedulers = (Schedule[]) response;
                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
                layoutManager = new LinearLayoutManager(tempActivity);
                recycleView.setLayoutManager(layoutManager);

                adapter = new ScheduleRecyclerAdapter(tempActivity, schedulers);
                recycleView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Object response) {

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