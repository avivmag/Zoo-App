package com.zoovisitors.pl;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;
import com.zoovisitors.pl.general_info.GeneralInfoActivity;
import com.zoovisitors.pl.enclosures.EnclosureListActivity;
import com.zoovisitors.pl.general_info.WatchAll;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.personalStories.PersonalStoriesActivity;
import com.zoovisitors.pl.schedule.ScheduleActivity;

import com.zoovisitors.pl.customViews.buttonCustomView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.System.exit;

public class MainActivity extends BaseActivity {
    private ScrollView scrollView;
    private LinearLayout newsFeedLinearLayout;
    private Menu langMenu;
    private NewsFeed[] feed;
    private Map<String, String> LanguageMap;
    private String[] newsFeedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.logo);


        //TODO: TESTING LOADING
        Log.e("TESTENC", GlobalVariables.testEnc[0].getName());

        Log.e("TESTOP", GlobalVariables.testOp[0].getDay());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GlobalVariables.appCompatActivity = this;
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        GlobalVariables.deviceId = telephonyManager.getDeviceId();
        //changeToHebrew();

        //GlobalVariables.bl = new BussinesLayerImplTestForPartialData(GlobalVariables.appCompatActivity);
        GlobalVariables.bl.sendDeviceId();

        //Set design for each button
        buttonCustomView encButton = (buttonCustomView) findViewById(R.id.enclosureListButton);
        encButton.designButton(R.color.greenIcon, R.mipmap.enc, R.string.our_enclosures);

        buttonCustomView otherInfoButton = (buttonCustomView) findViewById(R.id.otherInfoButton);
        otherInfoButton.designButton(R.color.brownIcon, R.mipmap.info, R.string.other_info);

        buttonCustomView personalButton= (buttonCustomView) findViewById(R.id.personalStoriesButton);
        personalButton.designButton(R.color.lightGreenIcon, R.mipmap.personal, R.string.personal);

        buttonCustomView mapButton = (buttonCustomView) findViewById(R.id.mapButton);
        mapButton.designButton(R.color.lightBlueIcon, R.mipmap.map, R.string.map);

        buttonCustomView wazebutton = (buttonCustomView) findViewById(R.id.wazeButton);
        wazebutton.designButton(R.color.lightBrownIcon, R.mipmap.waze_icon, R.string.nav);

        buttonCustomView scheduleButton = (buttonCustomView) findViewById(R.id.scheduleButton);
        scheduleButton.designButton(R.color.blueIcon, R.mipmap.schedule, R.string.schedule);




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

                feed = (NewsFeed[]) response;

                //feed wall initiation
                scrollView = findViewById(R.id.feedWall);
                scrollView.setClickable(false);
                newsFeedLinearLayout = findViewById(R.id.feedWallLayout);
                for (NewsFeed s: feed) {
                    TextView tv = new TextView(GlobalVariables.appCompatActivity);
                    tv.setText(s.getStory());
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setTextSize(18);
                    LinearLayout lineBorder = new LinearLayout(GlobalVariables.appCompatActivity);
                    lineBorder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    newsFeedLinearLayout.addView(tv);
                    newsFeedLinearLayout.addView(lineBorder);
                }

                newsFeedList = new String[feed.length];
                for (int i=0; i<feed.length; i++){
                    newsFeedList[i] = feed[i].getStory();
                }

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        startAutoScrolling();
                    }
                });
            }

            @Override
            public void onFailure(Object response) {
                Log.e("FEED", "Cant get feed");
            }
        });

        //watch all button
        findViewById(R.id.feedWallButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent watchAll = new Intent(MainActivity.this, WatchAll.class);
                Bundle newsFeedBundle = new Bundle();
                if (newsFeedList == null){
                    newsFeedList = new String[]{"NO NEWS FEED"};
                }
                Log.e("NEWS", newsFeedList[0]);
                newsFeedBundle.putSerializable("newsFeed", newsFeedList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

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
        return true;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notifications:
                item.setChecked(!item.isChecked());
                //TODO: send to the server to cancel/add notifications
                return true;
//            case R.id.language_arb:
//                return true;

            default:
                return super.onOptionsItemSelected(item);
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
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
        super.onBackPressed();
    }
//    private void designButton(int id, int background, int image, int text){
//        buttonCustomView button = (buttonCustomView) findViewById(id);
//        button.setBackgroundColor(background);
//        TextView iconText = (TextView) findViewById(button.getTextId());
//        ImageView iconImage = (ImageView) findViewById(button.getImageId());
//
//        iconText.setTextSize(20);
//        iconText.setText(text);
//
//        iconImage.setImageResource(image);
//    }
}
