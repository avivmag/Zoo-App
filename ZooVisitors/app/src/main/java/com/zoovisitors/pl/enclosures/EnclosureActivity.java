package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.ImageViewEncAsset;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.customViews.buttonCustomView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
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

    //facebook fields
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Target target;
    private Bitmap encImage;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setActionBar(R.color.greenIcon);
        Bundle clickedEnclosure = getIntent().getExtras();
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
                encImage = (Bitmap) response;

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
        buttonCustomView facebookShare = (buttonCustomView) findViewById(R.id.shareOnFacebook);
        facebookShare.designButton(R.color.transparent, R.mipmap.facebook_icon, R.string.shareOnFacebook, 16, R.color.black,0, 125);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        target = new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                if (ShareDialog.canShow(SharePhotoContent.class)){
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
            Log.e("URL", GlobalVariables.ServerAddress + enclosure.getPictureUrl());
//            Picasso.with(getBaseContext())
//                    .load(GlobalVariables.ServerAddress + enclosure.getPictureUrl())
//                    .into(target);

//            target.onBitmapLoaded(encImage, null);

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


        buttonCustomView showOnMapButton = (buttonCustomView) findViewById(R.id.showOnMap);
        showOnMapButton.designButton(R.color.transparent, R.mipmap.show_on_map, R.string.showOnMap, 16, R.color.black,0, 125);

        showOnMapButton.setOnClickListener(
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
                    imagesInAsset.add((Bitmap) response);

                    imageView.setOnClickListener(v -> {
                        Intent assetsPopUp = new Intent(GlobalVariables.appCompatActivity, EnclosureAssetsPopUp.class);
                        assetsPopUp.putExtra("arraySize", imagesInAsset.size());
                        for (int i=0; i<imagesInAsset.size(); i++) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            imagesInAsset.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            assetsPopUp.putExtra("image" + i, byteArray);

//                            assetsPopUp.putExtra("images" + i, imagesInAsset.get(i));
                            startActivity(assetsPopUp);
                        }
                    });

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
}
