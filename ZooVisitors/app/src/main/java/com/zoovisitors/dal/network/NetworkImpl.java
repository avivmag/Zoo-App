package com.zoovisitors.dal.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zoovisitors.GlobalVariables;

/**
 * Created by aviv on 08-Jan-18.
 */

public class NetworkImpl implements NetworkInterface {
    private Context context;
    public NetworkImpl(Context context)
    {
        this.context = context;
    }

    public void post(String innerURL, final ResponseInterface responseInterface)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
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
}
