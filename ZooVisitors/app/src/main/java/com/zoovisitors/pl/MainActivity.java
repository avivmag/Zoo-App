package com.zoovisitors.pl;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.schedule.ScheduleActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private LinearLayout newsFeddLinearLayout;
    private Menu langMenu;
    private NewsFeed[] feed;
    private Map<String, String> LanguageMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalVariables.appCompatActivity = this;
        //changeToHebrew();

        //Initialize business layer (change for testing)
        GlobalVariables.bl = new BusinessLayerImpl(GlobalVariables.appCompatActivity);
        //GlobalVariables.bl = new BussinesLayerImplTestForPartialData(GlobalVariables.appCompatActivity);
        //Token for notification
        GlobalVariables.deviceId = FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN", "token "+ FirebaseInstanceId.getInstance().getToken());

        GlobalVariables.bl.sendDeviceId();

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
                newsFeddLinearLayout = findViewById(R.id.feedWallLayout);
                for (NewsFeed s: feed) {
                    TextView tv = new TextView(GlobalVariables.appCompatActivity);
                    tv.setText(s.getStory());
                    tv.setTextColor(getResources().getColor(R.color.black));
                    tv.setTextSize(18);
                    LinearLayout lineBorder = new LinearLayout(GlobalVariables.appCompatActivity);
                    lineBorder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    newsFeddLinearLayout.addView(tv);
                    newsFeddLinearLayout.addView(lineBorder);
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


        //enclosure button
        findViewById(R.id.enclosureListButton).setOnClickListener(new View.OnClickListener() {
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

        //waze image
        findViewById(R.id.wazeImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAppInstalled(GlobalVariables.appCompatActivity, "com.waze")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?newsFeddLinearLayout=31.258137,34.745620")));//&navigate=yes
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

        int jumpSteps = newsFeddLinearLayout.getMeasuredHeight()/ scrollView.getHeight();
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

    public void onClickWatchAllButton(View v){
        //TODO:!!!!!!
    }

    public void onClickEnclosuresButton(View v){
        //TODO:!!!!!!
    }

    public void onClickScheduleButton(View v){
        //TODO:!!!!!!
    }

    public void onClickMoreInfoButton(View v){
        //TODO:!!!!!!
    }

    public void onClickMapButton(View v){
        //TODO:!!!!!!
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
}