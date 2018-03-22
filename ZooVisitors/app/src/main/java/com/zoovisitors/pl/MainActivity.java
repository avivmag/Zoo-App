package com.zoovisitors.pl;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    private ScrollView sv;
    private LinearLayout ll;
    private Menu langMenu;
    private NewsFeed[] feed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalVariables.appCompatActivity = this;
        //Initialize business layer (change for testing)
        GlobalVariables.bl = new BusinessLayerImpl(GlobalVariables.appCompatActivity);
        //GlobalVariables.bl = new BussinesLayerImplTestForPartialData(GlobalVariables.appCompatActivity);
        //Token for notification
        Log.e("TOKEN", "token "+ FirebaseInstanceId.getInstance().getToken());
        //Scroller initalize
        GlobalVariables.bl.getNewsFeed(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {

                feed = (NewsFeed[]) response;

                //feed wall initiation
                sv = findViewById(R.id.feedWall);
                sv.setClickable(false);
                ll = findViewById(R.id.feedWallLayout);
                for (NewsFeed s: feed) {
                    TextView tv = new TextView(GlobalVariables.appCompatActivity);
                    tv.setText(s.getStory());
                    ll.addView(tv);
                }

                sv.post(new Runnable() {
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
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("waze://?ll=31.258137,34.745620")));//&navigate=yes
                }
                else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.waze.com/location?ll=31.258137,%2034.745620")));
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
        return true;
    }

    private void startAutoScrolling(){

        int jumpSteps = ll.getMeasuredHeight()/sv.getHeight();
        ObjectAnimator animator[] = new ObjectAnimator[jumpSteps];

        for (int i = 0; i < jumpSteps; i++){
            int scrollPos = (int) (sv.getHeight() + 1.0) * (i+1);
            animator[i] = ObjectAnimator.ofInt(sv, "scrollY", scrollPos);
            animator[i].setDuration(100000);
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
            case R.id.language_arb:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSelectLanguage(MenuItem item){
        //TODO: add a language change functionality

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
                return;
            case R.id.language_eng:
                GlobalVariables.language = 2;
                engItem.setChecked(true);
                arbItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                return;
            case R.id.language_heb:
                GlobalVariables.language = 1;
                hebItem.setChecked(true);
                engItem.setChecked(false);
                arbItem.setChecked(false);
                rusItem.setChecked(false);
                return;
            case R.id.language_rus:
                GlobalVariables.language = 4;
                rusItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                arbItem.setChecked(false);
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
}