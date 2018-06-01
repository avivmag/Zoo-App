package com.zoovisitors.pl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.UpdateInterface;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.cl.gps.GpsService;
import com.zoovisitors.pl.customViews.ProgressBarCustomView;
import java.util.Locale;

public class LoadingScreen extends BaseActivity {

    //progress bar fields
    private ProgressBarCustomView pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage();

        //hiding the action bar in this activity
        android.support.v7.app.ActionBar AB= getSupportActionBar();
        AB.hide();

        setContentView(R.layout.activity_loading_screen);
        //Initialize business layer (change for testing)
        GlobalVariables.appCompatActivity = this;
        GlobalVariables.bl = new BusinessLayerImpl(GlobalVariables.appCompatActivity);
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

        Log.e("AVIV", "Tryin=g");
        // trigger gps service
        Intent i= new Intent(getApplicationContext(), GpsService.class);
        startService(i);
    }

    private void goToMain(){
        Intent loadingIntent = new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(loadingIntent);
    }

    private void changeLanguage() {
        if (GlobalVariables.firstEnter == 0) {
            switch (Locale.getDefault().getLanguage()){
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
            GlobalVariables.firstEnter++;
        }
    }
}