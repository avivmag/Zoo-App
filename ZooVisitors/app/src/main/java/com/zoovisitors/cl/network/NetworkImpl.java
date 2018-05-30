package com.zoovisitors.cl.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.ResponseInterface;
import com.zoovisitors.backend.callbacks.UpdateInterface;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviv on 08-Jan-18.
 */

public class NetworkImpl implements NetworkInterface {
    private RequestQueue queue;

    public NetworkImpl(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void post(String innerURL, final ResponseInterface<String> responseInterface) {
        // Instantiate the RequestQueue.
        String url = "http://" + GlobalVariables.ServerAddress + "/" + innerURL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseInterface.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseInterface.onFailure(error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void postImage(String innerURL, int width, int height, final ResponseInterface<Bitmap> responseInterface) {
        // Instantiate the RequestQueue.
        String url = "http://" + GlobalVariables.ServerAddress + "/" + innerURL;

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        responseInterface.onSuccess(response);
                    }
                },
                width,
                height,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseInterface.onFailure(error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        queue.add(request);
    }

    public void postImageWithoutPrefix(String url, int width, int height, final ResponseInterface<Bitmap> responseInterface) {
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        responseInterface.onSuccess(response);
                    }
                },
                width,
                height,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseInterface.onFailure(error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        queue.add(request);
    }

    @Override
    public void postAudio(String innerUrl, ResponseInterface<MediaPlayer> responseInterface) {
        String url = "http://" + GlobalVariables.ServerAddress + "/" + innerUrl;
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(url);
            responseInterface.onSuccess(mp);
        } catch (IOException e) {
            responseInterface.onFailure("IOException on setDataSource");
        }

    }


    @Override
    public void getInitDataString(String innerUrl, UpdateInterface updateInterface) {
        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... urls) {
                int count;
                int lengthOfFile;
                int total;
                try {
                    URL url = new URL(urls[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // getting file length
                    lengthOfFile = connection.getContentLength();


                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 1024 * 1024 * 10);

                    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                    byte data[] = new byte[1024];

                    total = 0;
                    String strAllData = "";
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        strAllData += new String(data, 0, count);
                        publishProgress((int) ((total * 100) / lengthOfFile));
                    }
                    input.close();
                    return strAllData;

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String strAllData) {
                if (strAllData != null)
                    updateInterface.onSuccess(strAllData);
                else
                    updateInterface.onFailure(GlobalVariables.appCompatActivity.getResources().getString(R.string.init_bad));
            }

            @Override
            protected void onProgressUpdate(Integer... integers) {
                updateInterface.onUpdate(integers[0]);
            }
        }.execute("http://" + GlobalVariables.ServerAddress + "/" + innerUrl);
    }



}
