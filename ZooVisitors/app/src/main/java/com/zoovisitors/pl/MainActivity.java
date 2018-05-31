package com.zoovisitors.pl;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.pl.customViews.MainButtonCustomView;
import com.zoovisitors.pl.general_info.GeneralInfoActivity;
import com.zoovisitors.pl.enclosures.EnclosureListActivity;
import com.zoovisitors.pl.general_info.WatchAll;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.personalStories.PersonalStoriesActivity;
import com.zoovisitors.pl.schedule.ScheduleActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class MainActivity extends BaseActivity {
    private ScrollView scrollView;
    private LinearLayout newsFeedLinearLayout;
    private Menu langMenu;
    private WallFeed[] feed;
    private Map<String, String> LanguageMap;
    private boolean isNotificationChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isNotificationChecked = true;
        getSupportActionBar().hide();
        setActionBarTransparentColor();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        GlobalVariables.bl.updateIfInPark(true, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                //Toast.makeText(GlobalVariables.appCompatActivity, "Token has been send", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Object response) {
                //Toast.makeText(GlobalVariables.appCompatActivity, "TOKEN NOT SEND", Toast.LENGTH_LONG).show();
            }
        });


        ((Button) findViewById(R.id.three_dots)).setOnClickListener(v->{
            showPopup(v);
        });


        //create the buttons layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = width/2;

        LinearLayout firstButtonsLayout = findViewById(R.id.first_buttons_layout);
        firstButtonsLayout.setLayoutParams(params);

        LinearLayout secondButtonsLayout = findViewById(R.id.second_buttons_layout);
        secondButtonsLayout.setLayoutParams(params);

        //initiates the listeners
        View.OnClickListener encListener = v -> {
            Intent enclosureIntent = new Intent(MainActivity.this, EnclosureListActivity.class);
            startActivity(enclosureIntent);
        };
        View.OnClickListener otherInfoListener = v -> {
            Intent otherInfoIntent = new Intent(MainActivity.this, GeneralInfoActivity.class);
            startActivity(otherInfoIntent);
        };
        View.OnClickListener mapListener = v -> {
            Intent otherInfoIntent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(otherInfoIntent);
        };
        View.OnClickListener scheduleListener = (v -> {
            Intent otherInfoIntent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(otherInfoIntent);
        });
        View.OnClickListener personalStoryListener = (v -> {
            Intent otherInfoIntent = new Intent(MainActivity.this, PersonalStoriesActivity.class);
            startActivity(otherInfoIntent);
        });
        View.OnClickListener navigateListener = v -> {
            if (isAppInstalled(GlobalVariables.appCompatActivity, "com.waze")){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?ll=31.258137,34.745620")));//&navigate=yes
            }
            else{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.waze.com/location?newsFeddLinearLayout=31.258137,%2034.745620")));
            }

        };

        //create the buttons
        int buttonsHeight = height/15*2;
        MainButtonCustomView encButton = new MainButtonCustomView(getBaseContext(), R.mipmap.enc_button, getResources().getString(R.string.our_enclosures), buttonsHeight, encListener);
        MainButtonCustomView otherInfoButton = new MainButtonCustomView(getBaseContext(),  R.mipmap.info_button, getResources().getString(R.string.other_info), buttonsHeight,otherInfoListener);
        MainButtonCustomView personalButton = new MainButtonCustomView(getBaseContext(), R.mipmap.personal_button, getResources().getString(R.string.personal), buttonsHeight,personalStoryListener);
        MainButtonCustomView mapButton = new MainButtonCustomView(getBaseContext(), R.mipmap.map_button, getResources().getString(R.string.map), buttonsHeight,mapListener);
        MainButtonCustomView wazebutton = new MainButtonCustomView(getBaseContext(), R.mipmap.waze_button, getResources().getString(R.string.nav), buttonsHeight,navigateListener);
        MainButtonCustomView scheduleButton = new MainButtonCustomView(getBaseContext(), R.mipmap.schedule_button, getResources().getString(R.string.schedule), buttonsHeight,scheduleListener);

        firstButtonsLayout.addView(encButton);
        secondButtonsLayout.addView(personalButton);
        firstButtonsLayout.addView(mapButton);
        secondButtonsLayout.addView(scheduleButton);
        firstButtonsLayout.addView(wazebutton);
        secondButtonsLayout.addView(otherInfoButton);

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

                //initates the scroller
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
                    TextView infoText = new TextView(getBaseContext());
                    infoText.setTextColor(getResources().getColor(R.color.black));
                    infoText.setTextSize(16);
                    infoText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    infoText.setText(s.getInfo());

                    //add the text to the feed layot
                    feedLayout.addView(dateText);
                    feedLayout.addView(infoText);

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
        findViewById(R.id.feedWallButton).setOnClickListener(v -> {
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
        switch (item.getItemId()) {
            case R.id.notifications:
                //item.setChecked(!item.isChecked());
                isNotificationChecked = !isNotificationChecked;
                if (GlobalVariables.notifications) {
                    GlobalVariables.bl.unsubscribeToNotification(new GetObjectInterface() {
                        @Override
                        public void onSuccess(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: Off", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: failed! try again", Toast.LENGTH_LONG).show();
                        }
                    });
                    GlobalVariables.notifications = false;
                }
                else {
                    GlobalVariables.bl.updateIfInPark(false, new GetObjectInterface() {
                        @Override
                        public void onSuccess(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: On", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Object response) {
                            Toast.makeText(GlobalVariables.appCompatActivity, "Notifications: failed! try again", Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                    GlobalVariables.notifications = true;
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
        notification.setChecked(isNotificationChecked);
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
}
