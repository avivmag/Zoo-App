package com.zoovisitors.bl;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.zoovisitors.backend.AboutZoo;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.backend.Species;
import com.zoovisitors.cl.network.NetworkImpl;
import com.zoovisitors.cl.network.NetworkInterface;
import com.zoovisitors.cl.network.ResponseInterface;

/**
 * Created by Gili on 13/01/2018.
 */

public class BusinessLayerImpl implements BusinessLayer {

    private NetworkInterface ni;
    private Gson gson;
    private String json;

    public BusinessLayerImpl(AppCompatActivity appCompatActivity) {
        ni = new NetworkImpl(appCompatActivity);
        gson = new Gson();
    }


    private void getJson(String url){
        ni.post(url, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Log.e("AVIV", "success: " + response);
                json = response;
            }

            @Override
            public void onFailure(String response) {
                Log.e("AVIV", "failure: " + response);
            }
        });
    }


    @Override
    public void getAnimals(int pos, final GetObjectInterface goi) {

        // TODO: example for how to retrieve data from the network, be aware that you should update your ip in GlobalVariables class.
        pos++;
        Log.e("POS", "" + pos);
        ni.post("animals/" + pos, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Animal[] animals = gson.fromJson(response, Animal[].class);
                goi.onSuccess(animals);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure(null);
            }
        });



        //getJson("animals/1");
    }

    @Override
    public Enclosure[] getEnclosures() {
        getJson("");

        while (json==null);
        Enclosure[] enclosures = gson.fromJson(json, Enclosure[].class);
        return enclosures;
    }

    @Override
    public AboutZoo getAboutZoo() {
        getJson("");

        while (json==null);
        AboutZoo aboutZoo = gson.fromJson(json, AboutZoo.class);
        return aboutZoo;
    }

    @Override
    public NewsFeed getNewsFeed() {
        getJson("");

        while (json==null);
        NewsFeed newsFeed = gson.fromJson(json, NewsFeed.class);
        return newsFeed;
    }

    @Override
    public Species getSpecies() {
        getJson("");

        while (json==null);
        Species species = gson.fromJson(json, Species.class);
        return species;
    }
}
