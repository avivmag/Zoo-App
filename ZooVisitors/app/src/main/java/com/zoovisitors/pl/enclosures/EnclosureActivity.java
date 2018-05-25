package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.zoovisitors.pl.customViews.buttonCustomView;
import java.util.Timer;
import java.util.TimerTask;

import static com.zoovisitors.bl.RecurringEventsHandler.getTimeAdjustedToWeekTime;

/**
 * Created by Gili on 28/12/2017.
 */

public class EnclosureActivity extends BaseActivity {

    private TextView closestEvent;
    private TextView closestEventTimer;
    private Enclosure enclosure;
    private Animal[] animals;
    private GridLayout assetsLayout;
    private RecurringEventsHandler recurringEventsHandler;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setActionBar(R.color.greenIcon);
        Bundle clickedEnclosure = getIntent().getExtras();
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

        //initialize the name textView
        TextView enclosureNameTextView = (TextView) findViewById(R.id.enclosureName);
        enclosureNameTextView.setText(enclosure.getName());

        //initialize the enclosure picture
        ImageView enclosureImageView = (ImageView) findViewById(R.id.enclosureImage);
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

        //initialize the closest event section
        LinearLayout enclosureColsestEventLayout = findViewById(R.id.enclosureClosestEventLayout);
        if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
            enclosureColsestEventLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            enclosureColsestEventLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        // if there are recurring events add them and start the timer.
        if (enclosure.getRecurringEvents().length > 0) {

            closestEvent = (TextView) findViewById(R.id.closesEventText);
            closestEventTimer = (TextView) findViewById(R.id.closestEventTimer);

            long delay = 6000;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    long startTime = getTimeAdjustedToWeekTime() + delay;
                    handleClosestEvent(startTime);
                }
            }, 0l ,delay*5);
        }
        else{ //else remove them from the main layout so it won't appear.
            LinearLayout encMainLayout = (LinearLayout) findViewById(R.id.enclosureMainLayout);
            LinearLayout enclosureClosestEventLayout = (LinearLayout) findViewById(R.id.enclosureClosestEventLayout);

            encMainLayout.removeView(enclosureClosestEventLayout);
        }


        //initialize the facebook and show on map buttons
        buttonCustomView facebookButton = (buttonCustomView) findViewById(R.id.shareOnFacebook);
        facebookButton.designButton(R.color.transparent, R.mipmap.facebook_icon, R.string.shareOnFacebook, 16, R.color.black,0, 125);

        buttonCustomView showOnMapButton = (buttonCustomView) findViewById(R.id.showOnMap);
        showOnMapButton.designButton(R.color.transparent, R.mipmap.show_on_map, R.string.showOnMap, 16, R.color.black,0, 125);

        showOnMapButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnclosureActivity.this, MapActivity.class);
                    startActivity(intent);

                }
        );

        facebookButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnclosureActivity.this, MapActivity.class);
                    startActivity(intent);

                }
        );

        //initialize the title and the story of the enclosure
        TextView enclosureStoryText = (TextView) findViewById(R.id.enc_story_text);
        enclosureStoryText.setText(enclosure.getStory());

        //initialize the 'who live here' section
        //Cards and Recycle of the animals
        RecyclerView recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle);
        RecyclerView.LayoutManager layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        recycleViewAnim.setLayoutManager(layoutManagerAnim);

        RecyclerView.Adapter adapterAnim = new AnimalsRecyclerAdapter(animals, R.layout.animal_card);
        recycleViewAnim.setAdapter(adapterAnim);

        //initialize the 'pictures and videos' section
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
        boolean blinking = false;
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
                                                    boolean blinking = true;
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
