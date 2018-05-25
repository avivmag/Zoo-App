package com.zoovisitors.pl;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.VideoView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.OpeningHours;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.callbacks.FunctionInterface;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.customViews.ProgressBarCustomView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class LoadingScreen extends BaseActivity {
    private final static int HUNDRED = 100;

    private boolean endAllThreads = false;
    private CountDownLatch doneSignal;
    private VideoView videoview;
    private List<FunctionInterface> tasks;

    //progress bar fields
    private double progressPercentage;
    private ProgressBarCustomView pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hiding the action bar in this activity
        android.support.v7.app.ActionBar AB= getSupportActionBar();
        AB.hide();

        setContentView(R.layout.activity_loading_screen);
        //Initialize business layer (change for testing)
        GlobalVariables.appCompatActivity = this;
        GlobalVariables.bl = new BusinessLayerImpl(GlobalVariables.appCompatActivity);
        GlobalVariables.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        //TODO: Delete this when sending device id to the server
        Log.e("TOKEN", "token " + GlobalVariables.firebaseToken);
        progressPercentage = 0;
        pb = (ProgressBarCustomView) findViewById(R.id.loading_progress_bar);
        pb.setProgressPrecentage((int) progressPercentage);

        tasks = new ArrayList<>();


//        videoview = (VideoView) findViewById(R.id.loading_video);
//        videoview.setTranslationX(-525f);
//        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loading);
//        videoview.setVideoURI(uri);
//        videoview.start();




        tasks.add(() -> {
            GlobalVariables.bl.getEnclosures(new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    GlobalVariables.testEnc = (Enclosure[]) response;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    increasePercentage();
                    doneSignal.countDown();
                }

                @Override
                public void onFailure(Object response) {
                    Log.e("Can't make task", (String) response);
                }
            });
        });

        tasks.add(() -> {
            GlobalVariables.bl.getOpeningHours(new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    GlobalVariables.testOp = (OpeningHours []) response;
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    increasePercentage();
                    doneSignal.countDown();
                }

                @Override
                public void onFailure(Object response) {
                    Log.e("Can't make task", (String) response);
                }
            });
        });

        tasks.add(() -> {
            changeToHebrew();
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            increasePercentage();
            doneSignal.countDown();
        });

        makeTasks();



    }


    private void goToMain(){
        Intent loadingIntent = new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(loadingIntent);
    }


    private void changeToHebrew() {
        if (GlobalVariables.firstEnter == 0) {
            GlobalVariables.firstEnter++;
            Locale myLocale = new Locale("iw");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
    }


    private void makeTasks(){
        doneSignal = new CountDownLatch(tasks.size());
        for (FunctionInterface f : tasks){
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    f.whatToDo();
                }
            });
            task.start();
        }

        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doneSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                goToMain();
            }
        });
        task.start();
    }

    private synchronized void increasePercentage(){
        progressPercentage++;
        Log.e("PERC", "" + ((int) ((progressPercentage/(double) tasks.size()) * HUNDRED)));
//        Log.e("PERC", "" + )
        pb.post(()-> {pb.setProgressPrecentage((int) ((progressPercentage/(double) tasks.size()) * HUNDRED));});
//        pb.post(()-> {pb.setProgressPrecentage();});
    }
}
