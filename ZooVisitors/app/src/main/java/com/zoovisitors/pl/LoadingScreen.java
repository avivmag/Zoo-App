package com.zoovisitors.pl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.UpdateInterface;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.pl.customViews.ProgressBarCustomView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoadingScreen extends BaseActivity {

    //progress bar fields
    private ProgressBarCustomView pb;
    private Map<Integer, String> languageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hiding the action bar in this activity
        android.support.v7.app.ActionBar AB= getSupportActionBar();
        AB.hide();



        languageMap = new HashMap<Integer, String>();
        //put language in the app according to values/strings/(**)
        languageMap.put(1, "iw");
        languageMap.put(2, "en");
        languageMap.put(3, "ar");
        languageMap.put(4, "ru");

        setContentView(R.layout.activity_loading_screen);
        //Initialize business layer (change for testing)
        GlobalVariables.appCompatActivity = this;
        changeLanguage();
        GlobalVariables.bl = new BusinessLayerImpl(getApplicationContext());
        GlobalVariables.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        pb = (ProgressBarCustomView) findViewById(R.id.loading_progress_bar);
        pb.setProgressPrecentage(0);

        GlobalVariables.bl.getAllDataInit(new UpdateInterface() {
            @Override
            public void onSuccess(Object response) {
                goToMain();
            }

            @Override
            public void onFailure(Object response) {
                new AlertDialog.Builder(LoadingScreen.this)
                        .setTitle("")
                        .setMessage((String) response)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (whichButton == dialog.BUTTON_POSITIVE){
                                    System.exit(0);
                                }
                            }}).show();
            }

            @Override
            public void onUpdate(Object response) {
                pb.post(() -> pb.setProgressPrecentage((Integer) response));
            }
        });
    }

    private void goToMain(){
        Intent loadingIntent = new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(loadingIntent);
    }

    private void changeLanguage() {
        SharedPreferences sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
        int languageNotInstantiate = getResources().getInteger(R.integer.language_not_instantiate);
        GlobalVariables.language = sharedPref.getInt(getString(R.string.language_preferences), languageNotInstantiate);
        if (GlobalVariables.language == languageNotInstantiate) {
            switch (Locale.getDefault().getLanguage()) {
                case "he": //Hebrew
                    GlobalVariables.language = 1;
                    break;
                case "en": //English
                    GlobalVariables.language = 2;
                    break;
                case "ar": //Arabic
                    GlobalVariables.language = 3;
                    break;
                case "ru": //Russian
                    GlobalVariables.language = 4;
                    break;
                default:
                    GlobalVariables.language = 1;
                    break;
            }
            sharedPref = GlobalVariables.appCompatActivity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.language_preferences), GlobalVariables.language);
            editor.commit();
            setLocale();
        }
        else
            setLocale();

    }


    private void setLocale() {

        Locale myLocale = new Locale(languageMap.get(GlobalVariables.language));
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}