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
import android.widget.VideoView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class LoadingScreen extends AppCompatActivity{
    private final int NUMBER_OF_THREAD = 3;
    private boolean endAllThreads = false;
    private CountDownLatch doneSignal;
    private VideoView videoview;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        GlobalVariables.firebaseToken = FirebaseInstanceId.getInstance().getToken();
        //TODO: Delete this when sending device id to the server
        Log.e("TOKEN", "token " + GlobalVariables.firebaseToken);

       videoview = (VideoView) findViewById(R.id.loading_video);
        videoview.setTranslationX(-525f);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loading);
        videoview.setVideoURI(uri);

        Log.e("ll", "ll");

        doneSignal = new CountDownLatch(NUMBER_OF_THREAD);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Void doInBackground(Void... voids) {

                doStartVideo();
                doChangeToHebrew();
                doWait();

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    doneSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                goToMain();
            }
        }.execute();
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



    private void doChangeToHebrew(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                changeToHebrew();
                doneSignal.countDown();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {}
        }.execute();
    }


    private void doWait(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doneSignal.countDown();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {}
        }.execute();
    }


    private void doStartVideo(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                videoview.start();
                Log.e("VIDEO", "VIDEO");
                doneSignal.countDown();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {}
        }.execute();
    }




}
