package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.RecurringEventsHandler;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.bl.Memory;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.animals.AnimalActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;
import com.zoovisitors.pl.customViews.ImageViewEncAsset;
import com.zoovisitors.pl.customViews.TextViewTitle;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.customViews.ButtonCustomView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<Bitmap> imagesInAsset;
    private CustomRelativeLayout encHeader;

    //facebook fields
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Target target;
    private Bitmap encImage;
    private int index;
    private Map<ImageView, Integer> imageViewIntegerMap;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setActionBar(R.color.greenIcon);
        Bundle clickedEnclosure = getIntent().getExtras();
        index = 0;
        imageViewIntegerMap = new HashMap<>();
        enclosure = (Enclosure) clickedEnclosure.getSerializable("enc");
        int id = enclosure.getId();
        recurringEventsHandler = new RecurringEventsHandler(enclosure.getRecurringEvents());

        imagesInAsset = new ArrayList<Bitmap>();

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

    private void draw() {
        setContentView(R.layout.activity_enclosure);

        //calculate the screen width.
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int layoutWidth = Double.valueOf(screenWidth/1.5).intValue();

        //initialize enclosure header
        encHeader = new CustomRelativeLayout(getBaseContext(), enclosure.getPictureUrl(), enclosure.getName(), enclosure.getAudioUrl() ,layoutWidth);
        encHeader.init();

        LinearLayout encMainLayout = (LinearLayout) findViewById(R.id.enclosureMainLayout);
        encMainLayout.addView(encHeader,0);

        //initialize the closest event section
        LinearLayout enclosureColsestEventLayout = findViewById(R.id.enclosureClosestEventLayout);
        if (GlobalVariables.language == 1 || GlobalVariables.language == 3) {
            enclosureColsestEventLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
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
            }, 0l, delay * 5);
        } else { //else remove them from the main layout so it won't appear.
            LinearLayout enclosureClosestEventLayout = (LinearLayout) findViewById(R.id.enclosureClosestEventLayout);

            encMainLayout.removeView(enclosureClosestEventLayout);
        }

        //initialize the facebook and show on map buttons
        ButtonCustomView facebookShare = (ButtonCustomView) findViewById(R.id.shareOnFacebook);
        facebookShare.designButton(R.color.transparent, R.mipmap.facebook_icon, R.string.shareOnFacebook, 16, R.color.black, 0, 125);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto)
                            .build();
                    shareDialog.show(content);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        facebookShare.setOnClickListener((v) -> {
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(EnclosureActivity.this, "Share Successful", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(EnclosureActivity.this, "Share Cancel", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(EnclosureActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(encImage)
                    .build();

            if (ShareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }

        });


        ButtonCustomView showOnMapButton = (ButtonCustomView) findViewById(R.id.showOnMap);
        showOnMapButton.designButton(R.color.transparent, R.mipmap.show_on_map, R.string.showOnMap, 16, R.color.black, 0, 125);

        showOnMapButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(EnclosureActivity.this, MapActivity.class);
                    startActivity(intent);

                }
        );

        //initialize the title and the story of the enclosure
        if (enclosure.getStory().equals("")){
            LinearLayout storyLayout = findViewById(R.id.enc_story_layout);
            encMainLayout.removeView(storyLayout);
        }
        else{
            TextView enclosureStoryText = findViewById(R.id.enc_story_text);
            enclosureStoryText.setText(enclosure.getStory());
        }

        LinearLayout whoLivesLayout = findViewById(R.id.who_lives_here_layout);
        for (Animal an: animals) {
            CustomRelativeLayout animalCard = getAnCard(an);

            whoLivesLayout.addView(animalCard);
        }

        //initialize the 'pictures and videos' section
        if (enclosure.getPictures().length == 0 && enclosure.getVideos().length == 0 ){
            LinearLayout encAssetsLayout = findViewById(R.id.enc_assets_layout);
            encMainLayout.removeView(encAssetsLayout);
        }
        else {
            assetsLayout = findViewById(R.id.enc_asset_grid_layout);
            int assetWidth = screenWidth/4;
            int assetHeight = screenWidth/4;
            addImagesToAssets(assetWidth, assetHeight);
            addVideosToAssets(assetWidth, assetHeight);
        }

    }

    private CustomRelativeLayout getAnCard(Animal animal) {
        int screenSize = getResources().getDisplayMetrics().widthPixels;
        int layoutWidth = Double.valueOf(screenSize/2.5).intValue();

        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),animal.getPictureUrl(), animal.getName(),null, layoutWidth);
        card.init();

        card.setOnClickListener(v -> {
                Intent intent = new Intent(GlobalVariables.appCompatActivity, AnimalActivity.class);
                Bundle clickedAnimal = new Bundle();
                clickedAnimal.putSerializable("animal", animal);

                intent.putExtras(clickedAnimal);
                GlobalVariables.appCompatActivity.startActivity(intent);
            });

        return card;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void addImagesToAssets(int width, int height) {
        Intent assetsPopUp = new Intent(GlobalVariables.appCompatActivity, EnclosureAssetsPopUp.class);
        for (Enclosure.PictureEnc pe : enclosure.getPictures()) {
            GlobalVariables.bl.getImage(pe.getPictureUrl(), width, height, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    ImageViewEncAsset imageView = new ImageViewEncAsset(GlobalVariables.appCompatActivity);
                    imageView.setImageBitmap((Bitmap) response);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.setMargins(4, 0, 0, 4);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    assetsLayout.addView(imageView);
                    imagesInAsset.add((Bitmap) response);
                    GlobalVariables.bl.insertStringandBitmap(pe.getPictureUrl(), (Bitmap) response);
                    assetsPopUp.putExtra("imageUrl" + index, pe.getPictureUrl());
                    imageViewIntegerMap.put(imageView, index);
                    index++;

                    imageView.setOnClickListener(v -> {
                        assetsPopUp.putExtra("pos", imageViewIntegerMap.get(imageView));
                        startActivity(assetsPopUp);
                    });
                }

                @Override
                public void onFailure(Object response) {
                    Log.e("ASSET", "PICTURE FAILED");
                }
            });

        }
        assetsPopUp.putExtra("arraySize", enclosure.getPictures().length);
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
                    Log.e("ASSET", "VIDEO FAILED " + imageUrl);
                }
            });
        }
    }

    private void handleClosestEvent(long starTimeArg) {
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
                                              } else {
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

    private void blinkingText(Timer timer) {
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

    @Override
    protected void onPause() {
        encHeader.stopAudio();
        super.onPause();
    }
}


//    private void printKeyHash() {
//        try{
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature s :  info.signatures){
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(s.toByteArray());
//                Log.e("KEY", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
