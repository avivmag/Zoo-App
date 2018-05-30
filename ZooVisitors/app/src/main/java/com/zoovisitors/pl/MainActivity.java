package com.zoovisitors.pl;

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
import com.zoovisitors.pl.customViews.ButtonCustomView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
        //setActionBar(R.color.transparent);
        setContentView(R.layout.activity_main);
        isNotificationChecked = true;
        getSupportActionBar().hide();
        setActionBarTransparentColor();
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

        //Set design for each button
        MainButtonCustomView encButton = (MainButtonCustomView) findViewById(R.id.enclosureListButton);
        encButton.mainDesignButton(R.mipmap.enc_button, R.string.our_enclosures);

        MainButtonCustomView otherInfoButton = (MainButtonCustomView) findViewById(R.id.otherInfoButton);
        otherInfoButton.designButton(R.mipmap.enc_button, R.string.other_info, 20, R.color.white, 20, 150);

        MainButtonCustomView personalButton = (MainButtonCustomView) findViewById(R.id.personalStoriesButton);
        personalButton.designButton(R.mipmap.enc_button, R.string.personal, 20, R.color.white, 20, 150);

        MainButtonCustomView mapButton = (MainButtonCustomView) findViewById(R.id.mapButton);
        mapButton.designButton(R.mipmap.enc_button, R.string.map, 20, R.color.white, 20, 150);

        MainButtonCustomView wazebutton = (MainButtonCustomView) findViewById(R.id.wazeButton);
        wazebutton.designButton(R.mipmap.enc_button, R.string.nav, 20, R.color.white, 20, 150);

        MainButtonCustomView scheduleButton = (MainButtonCustomView) findViewById(R.id.scheduleButton);
        scheduleButton.designButton(R.mipmap.enc_button, R.string.schedule, 20, R.color.white, 20, 150);


        LanguageMap = new HashMap<String, String>();
        //put language in the app according to values/strings/(**)
        LanguageMap.put("Hebrew", "iw");
        LanguageMap.put("English", "en");
        LanguageMap.put("Arabic", "ar");
        LanguageMap.put("Russian", "rus");

        //Scroller initialize
        GlobalVariables.bl.getNewsFeed(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {

                feed = (WallFeed[]) response;

                //feed wall initiation
                scrollView = findViewById(R.id.feedWall);
                scrollView.setClickable(false);
                newsFeedLinearLayout = findViewById(R.id.feedWallLayout);

                for (WallFeed s: feed) {
                    TextView tv = new TextView(GlobalVariables.appCompatActivity);
                    tv.setText(s.getInfo());
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setTextSize(18);
                    LinearLayout lineBorder = new LinearLayout(GlobalVariables.appCompatActivity);
                    lineBorder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    newsFeedLinearLayout.addView(tv);
                    newsFeedLinearLayout.addView(lineBorder);
                }


//                newsFeedList = new String[feed.length];
//                for (int i=0; i<feed.length; i++){
//                    newsFeedList[i] = feed[i].getInfo();
//                }

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        startAutoScrolling();
                    }
                });
            }

            @Override
            public void onFailure(Object response) {
                Log.e("FEED", "Can't get feed");
            }
        });

        //watch all button
        findViewById(R.id.feedWallButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feed == null){
                    feed = new WallFeed[]{new WallFeed("NO NEWS FEED","")};
                }
                Log.e("NEWS", feed[0].getTitle());

                Intent watchAll = new Intent(GlobalVariables.appCompatActivity, WatchAll.class);
                Bundle newsFeedBundle = new Bundle();
                newsFeedBundle.putSerializable("NewsFeed", feed);
                watchAll.putExtras(newsFeedBundle);
                startActivity(watchAll);
            }
        });

        //enclosure button
        encButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enclosureIntent = new Intent(MainActivity.this, EnclosureListActivity.class);
                startActivity(enclosureIntent);
            }
        });

        //other info button
        findViewById(R.id.otherInfoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherInfoIntent = new Intent(MainActivity.this, GeneralInfoActivity.class);
                startActivity(otherInfoIntent);
            }
        });

        //map button
        findViewById(R.id.mapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherInfoIntent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(otherInfoIntent);
            }
        });

        //ScheduleActivity button
        findViewById(R.id.scheduleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherInfoIntent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(otherInfoIntent);
            }
        });

        //Personal stories button
        findViewById(R.id.personalStoriesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherInfoIntent = new Intent(MainActivity.this, PersonalStoriesActivity.class);
                startActivity(otherInfoIntent);
            }
        });

        //waze image
        findViewById(R.id.wazeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAppInstalled(GlobalVariables.appCompatActivity, "com.waze")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?ll=31.258137,34.745620")));//&navigate=yes
                }
                else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.waze.com/location?newsFeddLinearLayout=31.258137,%2034.745620")));
                }

            }
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
                GlobalVariables.language = 3;
                arbItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                setLocale(LanguageMap.get("Arabic"));
                return;
            case R.id.language_eng:
                GlobalVariables.language = 2;
                engItem.setChecked(true);
                arbItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                setLocale(LanguageMap.get("English"));
                return;
            case R.id.language_heb:
                GlobalVariables.language = 1;
                hebItem.setChecked(true);
                engItem.setChecked(false);
                arbItem.setChecked(false);
                rusItem.setChecked(false);
                setLocale(LanguageMap.get("Hebrew"));
                return;
            case R.id.language_rus:
                GlobalVariables.language = 4;
                rusItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                arbItem.setChecked(false);
                setLocale(LanguageMap.get("Russian"));
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
