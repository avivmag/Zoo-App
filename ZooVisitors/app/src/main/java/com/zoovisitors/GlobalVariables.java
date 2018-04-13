package com.zoovisitors;

import android.support.v7.app.AppCompatActivity;

import com.zoovisitors.bl.BusinessLayer;

/**
 * Created by aviv on 10-Jan-18.
 */

public class GlobalVariables {
    public static String ServerAddress = "negevzoo.sytes.net:50555/";
    public static String LOG_TAG = "zoovisitors";
    public static int language = 1; //Hebrew
    public static boolean DEBUG = true;
    public static AppCompatActivity appCompatActivity;
    public static BusinessLayer bl;
    public static int firstEnter = 0;
    public static AppCompatActivity foregroundActivity;
    public static String firebaseToken;
}
