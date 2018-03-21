package com.zoovisitors.pl.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;

import javax.microedition.khronos.opengles.GL;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Schedule[] schedulers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

//        //Delete when I get object from server
//        this.schedulers = new Schedule[10];
//        for (int i = 0; i<10; i++){
//            this.schedulers[i] = new Schedule();
//            this.schedulers[i].setName("" + i);
//        }

        GlobalVariables.bl.getSchedule(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                schedulers = (Schedule[]) response;
                recycleView = (RecyclerView) findViewById(R.id.schedule_recycle);
                layoutManager = new LinearLayoutManager(GlobalVariables.appCompatActivity);
                recycleView.setLayoutManager(layoutManager);
                Log.e("SChed", schedulers[0].getDescription());
                adapter = new ScheduleRecyclerAdapter(schedulers);
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