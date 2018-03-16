package com.zoovisitors.pl.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;
import com.zoovisitors.pl.enclosures.EnclosureActivity;

public class MapActivity extends AppCompatActivity {

    private MapView map_frame;
    private Enclosure[] enclosures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map_frame = findViewById(R.id.map_test_frame);

        BusinessLayer bl = new BusinessLayerImpl(this);

        bl.getEnclosures(0, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosures = (Enclosure[]) response;



                map_frame.addImageIcon("zoo_background","",0, 0);
                for(int i = 0; i<5;i++)
                {
                    map_frame.addImageIcon("animal_" + i, "",150 + 150*i, 200 + (int)(Math.random()*100));
                }

            }

            @Override
            public void onFailure(Object response) {
                Log.e("GILI", "Callback failed");
            }
        });
    }
}
