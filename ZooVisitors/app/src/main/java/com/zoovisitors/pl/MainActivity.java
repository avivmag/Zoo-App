package com.zoovisitors.pl;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.cl.gps.GpsService;
import com.zoovisitors.pl.general_info.GeneralInfoActivity;
import com.zoovisitors.pl.enclosures.EnclosureListActivity;
import com.zoovisitors.pl.general_info.WatchAll;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.personalStories.PersonalStoriesActivity;
import com.zoovisitors.pl.schedule.ScheduleActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends BaseActivity {
    private ScrollView scrollView;
    private LinearLayout newsFeedLinearLayout;
    private Menu langMenu;
    private WallFeed[] feed;
    private Map<String, String> LanguageMap;
    private static final int PERMISSION_REQUEST_LOCATION = 370;
    private boolean refusedToTurnOnGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        setActionBarTransparentColor();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        ((Button) findViewById(R.id.three_dots)).setOnClickListener(v->{
            showPopup(v);
        });

        setButtonsLayouts();

        LanguageMap = new HashMap<String, String>();
        //put language in the app according to values/strings/(**)
        LanguageMap.put("Hebrew", "iw");
        LanguageMap.put("English", "en");
        LanguageMap.put("Arabic", "ar");
        LanguageMap.put("Russian", "ru");

        //Scroller initialize weed Wall Feeds
        GlobalVariables.bl.getNewsFeed(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                //getting all the feeds.
                feed = (WallFeed[]) response;

                //initiates the scroller
                scrollView = findViewById(R.id.feedWall);
                scrollView.setClickable(false);
                ViewGroup.LayoutParams scrollParams = scrollView.getLayoutParams();
                scrollParams.width = width*2/3;
                scrollParams.height = height/5;

                //This override the option to scroll while there is animation.
                // after the animation ends, scrolling will be enabled.
                scrollView.setOnTouchListener((v, event) -> true);

                newsFeedLinearLayout = findViewById(R.id.feedWallLayout);

                for (WallFeed s: feed) {
                    //initiates the linear layout of the feed
                    LinearLayout feedLayout = new LinearLayout(getBaseContext());
                    feedLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    feedLayout.setOrientation(LinearLayout.VERTICAL);
                    feedLayout.setBackground(getResources().getDrawable(R.drawable.dashed_bottom_line));
                    feedLayout.setLayerType(View.LAYER_TYPE_SOFTWARE,null);

                    //initiates the date of the feed
                    TextView dateText = new TextView(getBaseContext());
                    dateText.setTextColor(getResources().getColor(R.color.black));
                    dateText.setTextSize(14);
                    dateText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    dateText.setText(s.getCreated());
                    //initiates the title.
                    TextView titleText = new TextView(getBaseContext());
                    titleText.setTextColor(getResources().getColor(R.color.black));
                    titleText.setTextSize(16);
                    titleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    titleText.setText(s.getTitle());

                    //add the text to the feed layot
                    feedLayout.addView(dateText);
                    feedLayout.addView(titleText);

                    //add the feed layout to the feed list
                    newsFeedLinearLayout.addView(feedLayout);
                }

                scrollView.post(() -> startAutoScrolling());
            }

            @Override
            public void onFailure(Object response) {
                Log.e("FEED", "Can't get feed");
            }
        });

        //initiates the watch all News Feed button
        Button feedWallButton = findViewById(R.id.feedWallButton);

        if (GlobalVariables.language == 4){
            feedWallButton.setTextSize(12);
        }

        feedWallButton.setOnClickListener(v -> {
            if (feed == null){
                feed = new WallFeed[] {new WallFeed("NO NEWS FEED","")};
            }

            Intent watchAll = new Intent(getBaseContext(), WatchAll.class);
            Bundle newsFeedBundle = new Bundle();
            newsFeedBundle.putSerializable("NewsFeed", feed);
            watchAll.putExtras(newsFeedBundle);
            startActivity(watchAll);
        });

    }

    private void startAutoScrolling(){
        int jumpSteps = newsFeedLinearLayout.getMeasuredHeight()/ scrollView.getHeight();
        ObjectAnimator animator[] = new ObjectAnimator[jumpSteps];

        for (int i = 0; i < jumpSteps; i++){
            int scrollPos = (int) (scrollView.getHeight() + 1.0) * (i+1);
            animator[i] = ObjectAnimator.ofInt(scrollView, "scrollY", scrollPos);
            animator[i].setDuration(30000);
        }

        for (int i = 0; i < jumpSteps; i++) {
            animator[i].start();
        }

        if (jumpSteps > 0){
            animator[0].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator[jumpSteps-1].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView.setOnTouchListener(null);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    scrollView.setOnTouchListener(null);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
    }

    public void  onClickNotification(MenuItem item){
        if(item.getItemId() == R.id.notifications) {
                //item.setChecked(!item.isChecked());
                int notificationFromPref = readFromPreferences(R.string.notification_preferences, R.integer.notification_not_instantiate);
                if (notificationFromPref == 1){
                    writeToPreferences(R.string.notification_preferences, 0);
                    GlobalVariables.bl.unsubscribeToNotification(new GetObjectInterface() {
                        @Override
                        public void onSuccess(Object response) {
                            item.setChecked(false);
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: Off", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: failed! try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
                else {
                    item.setChecked(true);
                    writeToPreferences(R.string.notification_preferences, 1);

                    GlobalVariables.bl.subscribeToNotification(new GetObjectInterface() {
                        @Override
                        public void onSuccess(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: On", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Something went wrong...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }
    }

    public void onSelectLanguage(MenuItem item){
        MenuItem hebItem = langMenu.getItem(1);
        MenuItem engItem = langMenu.getItem(0);
        MenuItem arbItem = langMenu.getItem(2);
        MenuItem rusItem = langMenu.getItem(3);

        switch (item.getItemId()){
            case R.id.language_arb:
                arbItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                if (GlobalVariables.language != 3) {
                    GlobalVariables.language = 3;
                    writeToPreferences(R.string.language_preferences, GlobalVariables.language);
                    setLocale(LanguageMap.get("Arabic"));
                }
                return;
            case R.id.language_eng:
                engItem.setChecked(true);
                arbItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                if (GlobalVariables.language != 2) {
                    GlobalVariables.language = 2;
                    writeToPreferences(R.string.language_preferences, GlobalVariables.language);
                    setLocale(LanguageMap.get("English"));
                }
                return;
            case R.id.language_heb:
                hebItem.setChecked(true);
                engItem.setChecked(false);
                arbItem.setChecked(false);
                rusItem.setChecked(false);
                if (GlobalVariables.language != 1) {
                    GlobalVariables.language = 1;
                    writeToPreferences(R.string.language_preferences, GlobalVariables.language);
                    setLocale(LanguageMap.get("Hebrew"));
                }
                return;
            case R.id.language_rus:
                rusItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                arbItem.setChecked(false);
                if (GlobalVariables.language != 4) {
                    GlobalVariables.language = 4;
                    writeToPreferences(R.string.language_preferences, GlobalVariables.language);
                    setLocale(LanguageMap.get("Russian"));
                }
                return;
        }


    }

    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Sets the app to get strings from the @lang language
     * @param lang
     */
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent intent = new Intent(this, LoadingScreen.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.main_menu, menu);


        MenuItem notification = menu.findItem(R.id.notifications);
        int notificationFromPref = readFromPreferences(R.string.notification_preferences, R.integer.notification_not_instantiate);
        if (notificationFromPref == -1){
            notification.setChecked(true);
            writeToPreferences(R.string.notification_preferences, 1);
            GlobalVariables.bl.subscribeToNotification(new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: On", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Object response) {
                    Toast.makeText(GlobalVariables.appCompatActivity, "Something went wrong...", Toast.LENGTH_LONG).show();
                }
            });
        }
        else if  (notificationFromPref == 1){
            notification.setChecked(true);
            writeToPreferences(R.string.notification_preferences, 1);
        }
        else {
            notification.setChecked(false);
        }
        MenuItem subm = menu.findItem(R.id.language);
        langMenu = subm.getSubMenu();
        MenuItem hebItem = langMenu.getItem(1);
        MenuItem engItem = langMenu.getItem(0);
        MenuItem arbItem = langMenu.getItem(2);
        MenuItem rusItem = langMenu.getItem(3);
        switch (GlobalVariables.language){
            case 1:
                hebItem.setChecked(true);
                break;
            case 2:
                engItem.setChecked(true);
                break;
            case 3:
                arbItem.setChecked(true);
                break;
            case 4:
                rusItem.setChecked(true);
                break;
        }
        popup.show();
    }

    /**
     * @return true if it needs to handle the permission and the general flow should not continue.
     */
    private boolean handlePermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.gps_no_permission_dialog_title)
                        .setMessage(R.string.gps_no_permission_dialog_message)
                        .setPositiveButton(R.string.gps_no_permission_dialog_approve, (dialog, id) -> {
                            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                        })
                        .setNegativeButton(R.string.gps_no_permission_dialog_disapprove, (dialog, id) -> {
                            refusedToTurnOnGPS = true;
                        })
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            }
            return true;
        }
        return false;
    }

    /**
     * @return true if it needs to handle the activation and the general flow should not continue.
     */
    public boolean handleActivation() {
        if (((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER))
            return false;

        if (!isFinishing())
            new AlertDialog.Builder(this)
                    .setTitle(R.string.gps_no_activated_dialog_title)
                    .setMessage(this.getResources().getString(R.string.gps_no_activated_dialog_activate_needed))
                    .setPositiveButton(this.getResources().getString(R.string.gps_no_activated_dialog_open_settings), (paramDialogInterface, paramInt) -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        this.startActivity(myIntent);
                    })
                    .setNegativeButton(this.getString(R.string.gps_no_activated_dialog_do_not_open_settings), (paramDialogInterface, paramInt) -> {
                        refusedToTurnOnGPS = true;
                    })
                    .show();
        return true;
    }

    private boolean runningUpdates = false;
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        if (!refusedToTurnOnGPS && !handlePermissions() && !handleActivation() && !runningUpdates) {
            runningUpdates = true;
            startService(new Intent(this, GpsService.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION:
                break;
        }
    }

    private void setButtonsLayouts() {
        setLayoutsImageAndText(R.id.personal_layout, R.mipmap.personal_button, R.string.personal);
        setLayoutsImageAndText(R.id.enc_layout, R.mipmap.enc_button, R.string.our_enclosures);
        setLayoutsImageAndText(R.id.schedule_layout, R.mipmap.schedule_button, R.string.schedule);
        setLayoutsImageAndText(R.id.map_layout, R.mipmap.map_button, R.string.map);
        setLayoutsImageAndText(R.id.waz_layout, R.mipmap.waze_button, R.string.nav);
        setLayoutsImageAndText(R.id.other_info_layout, R.mipmap.info_button, R.string.other_info);
    }

    public void onClickMenuButton(View v) {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.enc_layout:
                intent = new Intent(MainActivity.this, EnclosureListActivity.class);
                break;
            case R.id.other_info_layout:
                intent = new Intent(MainActivity.this, GeneralInfoActivity.class);
                break;
            case R.id.map_layout:
                intent = new Intent(MainActivity.this, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.schedule_layout:
                intent = new Intent(MainActivity.this, ScheduleActivity.class);
                break;
            case R.id.personal_layout:
                intent = new Intent(MainActivity.this, PersonalStoriesActivity.class);
                break;
            case R.id.waz_layout:
                if (isAppInstalled(GlobalVariables.appCompatActivity, "com.waze")){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?ll=31.258137,34.745620"));//&navigate=yes
                }
                else{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.waze.com/location?newsFeddLinearLayout=31.258137,%2034.745620"));
                }
                break;
            default:
                return;
        }
        startActivity(intent);
    }

    private void setLayoutsImageAndText(int layoutId, int imageId, int textId) {
        ConstraintLayout cl = (ConstraintLayout) findViewById(layoutId);
        ((ImageView) cl.findViewById(R.id.icon_image)).setImageResource(imageId);
        ((TextView) cl.findViewById(R.id.icon_text)).setText(textId);

        //if it's russian than it's too long
        if (GlobalVariables.language == 4){
            ((TextView) cl.findViewById(R.id.icon_text)).setTextSize(12);
        }

    }

    private int readFromPreferences(int stringId, int integerId){
        SharedPreferences sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        int integerNotInstantiate = getResources().getInteger(integerId);
        return sharedPref.getInt(getString(stringId), integerNotInstantiate);
    }

    private void writeToPreferences(int stringId, int write){
        SharedPreferences sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(stringId), write);
        editor.commit();
    }
}