package com.zoovisitors.pl.general_info;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.pl.BaseActivity;

public class GeneralInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_info);
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        GlobalVariables.bl.sendDeviceId();

        // Find the view pager that will allow the user to swipe between fragments
        final ViewPager viewPager = (ViewPager) findViewById(R.id.general_info_pager);

        // Create an adapter that knows which fragment should be shown on each page
        GeneralInfoFragmentAdapter adapter = new GeneralInfoFragmentAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.generalInfoTab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.getTabAt(0).setText(getResources().getText(R.string.about_us));
        tabLayout.getTabAt(1).setText(getResources().getText(R.string.prices));
        tabLayout.getTabAt(2).setText(getResources().getText(R.string.opening_hours));
        tabLayout.getTabAt(3).setText(getResources().getText(R.string.contact_info));
    }
}
