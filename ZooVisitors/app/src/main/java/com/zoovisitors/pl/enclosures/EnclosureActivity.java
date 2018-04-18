package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.GetObjectInterface;
//import com.facebook.*;


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

    private Animal[] animals;
    private Bundle clickedEnclosure;
    private TextView enclosureStoryText;
    private Enclosure enclosure;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickedEnclosure = getIntent().getExtras();
        enclosure = (Enclosure) clickedEnclosure.getSerializable("enc");
        GlobalVariables.bl = new BusinessLayerImpl(this);
        int id = enclosure.getId();

        GlobalVariables.bl.getAnimals(id, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                animals = (Animal[]) response;

                animalsNames = new String[animals.length];
                for (int i = 0; i < animals.length; i++)
                    animalsNames[i] = animals[i].getName();
                draw();
            }

            @Override
            public void onFailure(Object response) {
                Log.e("GetAnimals", (String) response);
            }
        });

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
        enclosureStoryText = (TextView) findViewById(R.id.enc_story_text);


//        int enclosureImageNumber = -1;
//        String enclosureName = "";
//        String enclosureStory = "";
        if(clickedEnclosure != null) {

//            enclosureImageNumber = clickedEnclosure.getInt("image");
//            enclosureName = clickedEnclosure.getString("name");
//            enclosureStory = clickedEnclosure.getString("story");
        }

        GlobalVariables.bl.getImage(enclosure.getPictureUrl(), new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosureImageView.setImageBitmap((Bitmap) response);
               // enclosureImageView.setImageResource(R.mipmap.african_enclosure);

                enclosureNameTextView.setText(enclosure.getName());
                enclosureStoryText.setText(enclosure.getStory());

                ((TextView) findViewById(R.id.closesEvent)).setText(closesEventMap.get(enclosure.getName() + "_closesEvent"));

                //Cards and Recycle
                recycleView = (RecyclerView) findViewById(R.id.animal_recycle);
                layoutManager = new LinearLayoutManager(GlobalVariables.appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
                recycleView.setLayoutManager(layoutManager);

                adapter = new AnimalsRecyclerAdapter(animalsImages, animals);
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
            public void onFailure(Object response) {
                Log.e("PIC", "NOT PIC");
            }
        });


        //ImageView facebookShare = (ImageView) findViewById(R.id.shareOnFacebookImage);

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
