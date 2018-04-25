package com.zoovisitors.cl.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoovisitors.GlobalVariables;

/**
 * Created by aviv on 08-Jan-18.
 */

public class NetworkImpl implements NetworkInterface {
    private RequestQueue queue;
    public NetworkImpl(Context context)
    {
        queue = Volley.newRequestQueue(context);
    }

    public void post(String innerURL, final ResponseInterface<String> responseInterface)
    {
        // Instantiate the RequestQueue.
        String url ="http://" + GlobalVariables.ServerAddress + "/" + innerURL;

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

    public void postImage(String innerURL, int width, int height, final ResponseInterface<Bitmap> responseInterface)
    {
        // Instantiate the RequestQueue.
        String url ="http://" + GlobalVariables.ServerAddress + "/" + innerURL;

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

    public void postImageWithoutPrefix(String url, int width, int height, final ResponseInterface<Bitmap> responseInterface)
    {
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
}
