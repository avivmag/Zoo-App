package com.zoovisitors.pl;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.zoovisitors.GlobalVariables;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        GlobalVariables.foregroundActivity = this;
    }
}
