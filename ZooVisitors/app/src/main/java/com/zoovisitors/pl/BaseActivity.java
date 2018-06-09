package com.zoovisitors.pl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

public class BaseActivity extends AppCompatActivity {
//    private static final int PERMISSION_REQUEST_GPS = 310;

    @Override
    protected void onResume() {
        super.onResume();
//        GlobalVariables.foregroundActivity = this;
    }

    //This function set the action bar to the activity with the given color
    public void setActionBar(int color){
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setDisplayShowCustomEnabled(true);
        bar.setCustomView(R.layout.custom_action_bar);
        bar.setDisplayUseLogoEnabled(true);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        android.support.v7.widget.Toolbar parent =(android.support.v7.widget.Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0,0);
        parent.setBackgroundResource(color);
        ImageView logoImage = findViewById(R.id.action_bar_logo);
        logoImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }

    public void setActionBarTransparentColor(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}