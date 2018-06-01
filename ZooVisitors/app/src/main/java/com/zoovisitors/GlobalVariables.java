package com.zoovisitors;

import android.support.v7.app.AppCompatActivity;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.cl.gps.GpsService;
import com.zoovisitors.pl.BaseActivity;

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
    public static int firstEnter = 0;
    public static BaseActivity foregroundActivity;
    public static String firebaseToken;
    public static boolean notifications = true;
    public static GpsService gpsService;
}
