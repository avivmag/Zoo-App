package com.zoovisitors.pl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.pl.enclosures.EnclosureListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class LoadingScreen extends AppCompatActivity{
    private final int NUMBER_OF_THREAD = 1;
    private boolean endAllThreads = false;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        final VideoView videoview = (VideoView) findViewById(R.id.loading_video);
        videoview.setTranslationX(-525f);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.loading);
        videoview.setVideoURI(uri);
        Log.e("ll","ll");


        List<Thread> threadList = new ArrayList<Thread>();
        final CountDownLatch doneSignal = new CountDownLatch(NUMBER_OF_THREAD);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
//                for (int i = 0; i<100000; i++){
//                    Log.e("kk","kddddddd");
//                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                endAllThreads = true;
            }
        }.execute();
        videoview.start();
//        try {
//            doneSignal.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            while (endAllThreads == false);
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
}
