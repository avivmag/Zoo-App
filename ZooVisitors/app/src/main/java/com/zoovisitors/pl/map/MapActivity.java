package com.zoovisitors.pl.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zoovisitors.R;

public class MapActivity extends AppCompatActivity {

    private MapView map_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map_frame = findViewById(R.id.map_test_frame);


        map_frame.addImageIcon("zoo_background","",0, 0);
        for(int i = 0; i<5;i++)
        {
            map_frame.addImageIcon("animal_" + i, "",150 + 150*i, 200 + (int)(Math.random()*100));
        }
    }
}
