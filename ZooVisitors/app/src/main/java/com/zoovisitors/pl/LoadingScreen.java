package com.zoovisitors.pl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.UpdateInterface;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.backend.callbacks.FunctionInterface;
import com.zoovisitors.pl.customViews.ProgressBarCustomView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class LoadingScreen extends BaseActivity {
    private final static int HUNDRED = 100;
    private CountDownLatch doneSignal;
    private List<FunctionInterface> tasks;

    //progress bar fields
    private double progressPercentage;
    private ProgressBarCustomView pb;
    private static long total = 0;
    private static int lengthOfFile = 0;

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
        //TODO: Delete this when sending device id to the server
        Log.e("TOKEN", "token " + GlobalVariables.firebaseToken);
        progressPercentage = 0;
        pb = (ProgressBarCustomView) findViewById(R.id.loading_progress_bar);

        GlobalVariables.bl.getAllDataInit(new UpdateInterface() {
            @Override
            public void onSuccess(Object response) {
                goToMain();
                //TODO: AVIV: Response is the whole data
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
            }
            GlobalVariables.firstEnter++;
        }
    }

//    /**
//     * Async Task to download file from URL
//     */
//    private class DownloadFile extends AsyncTask<String, Integer, String> {
//
//        private ProgressDialog progressDialog;
//        private String fileName;
//        private String folder;
//        private boolean isDownloaded;
//
//        /**
//         * Before starting background thread
//         * Show Progress Bar Dialog
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        @Override
//        protected String doInBackground(String... f_url) {
//            int count;
//            try {
//                URL url = new URL(f_url[0]);
//                URLConnection connection = url.openConnection();
//                connection.connect();
//                // getting file length
//                lengthOfFile = connection.getContentLength();
//
//
//                // input stream to read file - with 8k buffer
//                InputStream input = new BufferedInputStream(url.openStream(), 1024 * 1024 * 10);
//
//                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//
//                byte data[] = new byte[1024];
//
//                total = 0;
//                String strAllData = "";
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    strAllData += new String(data, 0, count);
//                    publishProgress((int) ((total * 100) / lengthOfFile));
//                }
//                input.close();
//                return "Downloaded";
//
//            } catch (Exception e) {
//                Log.e("Error: ", e.getMessage());
//            }
//
//            return "Something went 1wrong";
//        }
//
//        /**
//         * Updating progress bar
//         */
//        @Override
//        protected void onProgressUpdate(Integer... val) {
//            pb.post(() -> pb.setProgressPrecentage(val[0]));
//        }
//
//
//        @Override
//        protected void onPostExecute(String message) {
//            goToMain();
//        }
//    }
}