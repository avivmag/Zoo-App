package com.zoovisitors;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.pl.BaseActivity;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by aviv on 10-Jan-18.
 */

public class GlobalVariables {
    public static boolean DEBUG = false;
    public static String ServerAddress = "negevzoo.sytes.net:50" +
            (DEBUG ? "555/" : "000/");
    public static String LOG_TAG = "zoovisitors";
    public static int language;
    public static AppCompatActivity appCompatActivity;
    public static BusinessLayer bl;
    public static String firebaseToken = FirebaseInstanceId.getInstance().getToken();
    public static boolean notifications = true;
}
