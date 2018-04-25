package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
//import com.facebook.*;


/**
 * Created by Gili on 28/12/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

public class EnclosureActivity extends BaseActivity {

    private TextView enclosureNameTextView;
    private ImageView enclosureImageView;
    private Map<String, String> closesEventMap;
    private AppCompatActivity tempActivity = this;
    private RecyclerView recycleViewAnim;
    private RecyclerView.LayoutManager layoutManagerAnim;
    private RecyclerView.Adapter adapterAnim;
    private RecyclerView recycleViewAsset;
    private RecyclerView.LayoutManager layoutManagerAsset;
    private RecyclerView.Adapter adapterAsset;

    private Animal[] animals;
    private Bundle clickedEnclosure;
    private TextView enclosureStoryText;
    private Enclosure enclosure;
    private GridLayout assetsLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickedEnclosure = getIntent().getExtras();
        enclosure = (Enclosure) clickedEnclosure.getSerializable("enc");
        int id = enclosure.getId();

        GlobalVariables.bl.getAnimals(id, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                animals = (Animal[]) response;
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

        if(clickedEnclosure != null) {

        }

        GlobalVariables.bl.getImage(enclosure.getPictureUrl(), 500, 500, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosureImageView.setImageBitmap((Bitmap) response);
               // enclosureImageView.setImageResource(R.mipmap.african_enclosure);


            }

            @Override
            public void onFailure(Object response) {
                Log.e("PIC", "NOT PIC");
            }
        });

        enclosureNameTextView.setText(enclosure.getName());
        enclosureStoryText.setText(enclosure.getStory());

        ((TextView) findViewById(R.id.closesEvent)).setText(closesEventMap.get(enclosure.getName() + "_closesEvent"));

        //Cards and Recycle of the animals
        recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle);
        layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        recycleViewAnim.setLayoutManager(layoutManagerAnim);

        adapterAnim = new AnimalsRecyclerAdapter(animals);
        recycleViewAnim.setAdapter(adapterAnim);

        //ImageView facebookShare = (ImageView) findViewById(R.id.shareOnFacebookImage);

        assetsLayout = (GridLayout) findViewById(R.id.enc_asset_grid_layout);
        int assetWidth = 250;
        int assetHeight = 250;
        addImagesToAssets(assetWidth, assetHeight);
        addVideosToAssets(assetWidth, assetHeight);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void addImagesToAssets(int width, int height){
        for (Enclosure.PictureEnc pe : enclosure.getPictures()){
            GlobalVariables.bl.getImage(pe.getPictureUrl(), width, height, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    ImageView imageView = new ImageView(GlobalVariables.appCompatActivity);
                    imageView.setImageBitmap((Bitmap) response);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.setMargins(3,0,0,3);
                    imageView.setLayoutParams(layoutParams);
                    assetsLayout.addView(imageView);
                }

                @Override
                public void onFailure(Object response) {
                    Log.e("ASSET","PICTURE FAILED");
                }
            });
        }
    }

    private void addVideosToAssets(int width, int height) {
        String youtubeVideoPrefix = "https://www.youtube.com/watch?v=";
        for (Enclosure.VideoEnc ve : enclosure.getVideos()) {
            ImageButton imageButton = new ImageButton(GlobalVariables.appCompatActivity);
            String imageUrl = "https://img.youtube.com/vi/" + ve.getVideoUrl() + "/0.jpg";
            GlobalVariables.bl.getImageFullUrl(imageUrl, 210, 210, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    imageButton.setImageBitmap((Bitmap) response);
                    imageButton.setOnClickListener(
                            v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoPrefix + ve.getVideoUrl()))));
                    FrameLayout frameLayout = new FrameLayout(GlobalVariables.appCompatActivity);
                    ImageView youtubeImageView = new ImageView(GlobalVariables.appCompatActivity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    youtubeImageView.setLayoutParams(layoutParams);
                    youtubeImageView.setImageResource(R.mipmap.youtube_play);
                    youtubeImageView.setX(140);
                    youtubeImageView.setY(80);
                    frameLayout.addView(imageButton);
                    frameLayout.addView(youtubeImageView);
                    assetsLayout.addView(frameLayout);
                }

                @Override
                public void onFailure(Object response) {
                    Log.e("ASSET","VIDEO FAILED " + imageUrl);
                }
            });
        }
    }
}
