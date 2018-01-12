package com.zoovisitors.pl;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import com.zoovisitors.R;
import com.zoovisitors.dal.network.NetworkImpl;
import com.zoovisitors.dal.network.ResponseInterface;
import com.zoovisitors.pl.GeneralInfo.GeneralInfoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ScrollView sv;
    private LinearLayout ll;
    private Menu langMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Scroller initalize
        List<String> feedDummy = new ArrayList<>();

        for (int i = 0; i<100; i++) {
            feedDummy.add("Kaki"+i);
        }

        //feed wall initiation
        sv = findViewById(R.id.feedWall);
        sv.setClickable(false);
        ll = findViewById(R.id.feedWallLayout);

        for (String s: feedDummy) {
            TextView tv = new TextView(this);
            tv.setText(s);
            ll.addView(tv);
        }

        sv.post(new Runnable() {
            @Override
            public void run() {
                startAutoScrolling();
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

    public void  onSelectLangauge(MenuItem item){
        //TODO: add a language change functionality

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
                return;
            case R.id.language_eng:
                engItem.setChecked(true);
                arbItem.setChecked(false);
                hebItem.setChecked(false);
                rusItem.setChecked(false);
                return;
            case R.id.language_heb:
                hebItem.setChecked(true);
                engItem.setChecked(false);
                arbItem.setChecked(false);
                rusItem.setChecked(false);
                return;
            case R.id.language_rus:
                rusItem.setChecked(true);
                engItem.setChecked(false);
                hebItem.setChecked(false);
                arbItem.setChecked(false);
                return;
        }
    }
}
