package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.BusinessLayer;


/**
 * Created by Gili on 28/12/2017.
 */
import java.util.HashMap;
import java.util.Map;

public class EnclosureActivity extends AppCompatActivity {

    private TextView enclosureNameTextView;
    private ImageView enclosureImageView;
    private Map<String, String> closesEventMap;
    private String[] animalsImages = {"chimpanse", "gorilla", "olive_baboon"};
    private String[] animalsNames;// = {"chimpanse", "gorilla", "olive_baboon"};
    private AppCompatActivity tempActivity = this;
    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private String json;
    private BusinessLayer bl;
    private Gson gson;
    private Animal[] animals;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bl = new BusinessLayerImpl();

        animals = bl.getAnimals();//
        animalsNames = new String[animals.length];
        for (int i = 0; i < animals.length; i++)
            animalsNames[i] = animals[i].getName();
        draw();
    }

    private void draw(){
        setContentView(R.layout.activity_enclosure);
        closesEventMap = new HashMap<String, String>();
        closesEventMap.put("african_enclosure_closesEvent", "Closes event: 10:00-11:00 Feeding");
        closesEventMap.put("monkeys_enclosure_closesEvent", "Closes event: 14:00-15:00 Dancing");
        closesEventMap.put("birds_enclosure_closesEvent", "Closes event: 12:00-12:30 Swimming");
        closesEventMap.put("reptiles_enclosure_closesEvent", "Closes event: 08:00-17:00 Doing nothing");

        enclosureNameTextView = (TextView) findViewById(R.id.enclosureName);
        enclosureImageView = (ImageView) findViewById(R.id.enclosureImage);

        Bundle clickedEnclosure = getIntent().getExtras();
        int enclosureImageNumber = -1;
        String enclosureName = "";
        if(clickedEnclosure != null) {
            enclosureImageNumber = clickedEnclosure.getInt("image");
            enclosureName = clickedEnclosure.getString("name");
        }
        enclosureImageView.setImageResource(enclosureImageNumber);
        enclosureNameTextView.setText(enclosureName);

        ((TextView) findViewById(R.id.closesEvent)).setText(closesEventMap.get(enclosureName + "_closesEvent"));

        //Cards and Recycle
        recycleView = (RecyclerView) findViewById(R.id.animal_recycle);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycleView.setLayoutManager(layoutManager);

        adapter = new AnimalsRecyclerAdapter(this, animalsImages, animalsNames, animals);
        recycleView.setAdapter(adapter);
        ImageButton imageButton = (ImageButton) findViewById(R.id.enclosure_video);
        imageButton.setImageResource(getResources().getIdentifier("monkey_video", "mipmap", tempActivity.getPackageName()));
        imageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=xOI0PSaIfVA")));
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        int id = item.getItemId();
//        if(id == R.id.action_settings)
//            return true;
        return super.onOptionsItemSelected(item);
    }

}
