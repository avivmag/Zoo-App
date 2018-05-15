package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.RecurringEventsHandler;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.ImageViewEncAsset;
import com.zoovisitors.pl.map.MapActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.zoovisitors.bl.RecurringEventsHandler.getTimeAdjustedToWeekTime;

//import com.facebook.*;


/**
 * Created by Gili on 28/12/2017.
 */

public class EnclosureActivity extends BaseActivity {

    private TextView enclosureNameTextView;
    private ImageView enclosureImageView;
    private TextView closestEvent;
    private TextView closestEventTimer;
    private RecyclerView recycleViewAnim;
    private RecyclerView.LayoutManager layoutManagerAnim;
    private RecyclerView.Adapter adapterAnim;

    private Animal[] animals;
    private Bundle clickedEnclosure;
    private TextView enclosureStoryText;
    private Enclosure enclosure;
    private GridLayout assetsLayout;
    private RecurringEventsHandler recurringEventsHandler;
    private boolean blinking;

    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getSupportActionBar();

        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setIcon(R.mipmap.logo);

        super.onCreate(savedInstanceState);
        clickedEnclosure = getIntent().getExtras();
        enclosure = (Enclosure) clickedEnclosure.getSerializable("enc");
        int id = enclosure.getId();
        recurringEventsHandler = new RecurringEventsHandler(enclosure.getRecurringEvents());

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
        ((ImageView) findViewById(R.id.mapIconImage)).setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnclosureActivity.this, MapActivity.class);
                    startActivity(intent);

                }
        );

        ((TextView) findViewById(R.id.showOnMap)).setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnclosureActivity.this, MapActivity.class);
                    startActivity(intent);

                }
        );
        enclosureNameTextView = (TextView) findViewById(R.id.enclosureName);
        enclosureImageView = (ImageView) findViewById(R.id.enclosureImage);
        enclosureStoryText = (TextView) findViewById(R.id.enc_story_text);
        closestEvent = (TextView) findViewById(R.id.closesEventText);
        closestEventTimer = (TextView) findViewById(R.id.closestEventTimer);

        if(clickedEnclosure != null) {

        }

        GlobalVariables.bl.getImage(enclosure.getPictureUrl(), 500, 500, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                enclosureImageView.setImageBitmap((Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {
                enclosureImageView.setImageResource(R.mipmap.no_image_available);
            }
        });

        enclosureNameTextView.setText(enclosure.getName());
        enclosureStoryText.setText(enclosure.getStory());
        long delay = 6000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long startTime = getTimeAdjustedToWeekTime() + delay;
                handleClosestEvent(startTime);
            }
        }, 0l ,delay*5);

        //Cards and Recycle of the animals
        recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle);
        layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        recycleViewAnim.setLayoutManager(layoutManagerAnim);

        adapterAnim = new AnimalsRecyclerAdapter(animals, R.layout.animal_card);
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
                    ImageViewEncAsset imageView = new ImageViewEncAsset(GlobalVariables.appCompatActivity);
                    imageView.setImageBitmap((Bitmap) response);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.setMargins(4,0,0,4);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
            ImageViewEncAsset imageButton = new ImageViewEncAsset(GlobalVariables.appCompatActivity);
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
                    youtubeImageView.setY(110);
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

    private void handleClosestEvent(long starTimeArg){
        final long DELAY_TIME = 0;
        final long PERIOD = 250;
        blinking = false;
        Enclosure.RecurringEvent recurringEvent = recurringEventsHandler.getNextRecuringEvent();
        final long startTime = starTimeArg;
        closestEvent.post(() -> closestEvent.setText(recurringEvent.getTitle()));
//        String time = recurringEventsHandler.getTime(recurringEvent.getStartTime());
        Timer timer = new Timer();
        Timer blinkingTimer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                            closestEventTimer.post(() -> {
                                                long currentTime = getTimeAdjustedToWeekTime();
                                                if (startTime > currentTime) {
                                                    if (blinking) {
                                                        blinkingTimer.cancel();
                                                        closestEvent.post(() -> closestEvent.setVisibility(View.VISIBLE));
                                                    }
                                                    closestEventTimer.setText(recurringEventsHandler.getTime(startTime - currentTime));
                                                }
                                                else{
                                                    blinkingText(blinkingTimer);
                                                    blinking = true;
                                                    timer.cancel();
                                                }
                                            });
                                      }
                                  },
                DELAY_TIME,
                PERIOD);

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                blinkingText();
//            }
//        },
//        3000l);
    }

    private void blinkingText(Timer timer){
        final long PERIOD = 300;
        final long DELAY = 0l;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                closestEvent.post(() -> {
                    if (closestEvent.getVisibility() == View.VISIBLE)
                        closestEvent.setVisibility(View.INVISIBLE);
                    else
                        closestEvent.setVisibility(View.VISIBLE);
                });
            }
        }, DELAY, PERIOD);
    }
}
