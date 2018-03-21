package com.zoovisitors.bl;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.AboutUs;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfo;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.backend.OpeningHours;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.Schedule;
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


    @Override
    public void getAnimals(int id, final GetObjectInterface goi) {

        // TODO: example for how to retrieve data from the network, be aware that you should update your ip in GlobalVariables class.
        Log.e("ID", "" + id);
        ni.post("/animals/enclosure/" + id + "/" + GlobalVariables.language, new ResponseInterface() {
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
    public void getAllAnimals(final GetObjectInterface goi) {
        ni.post("animals/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Animal[] animals = gson.fromJson(response, Animal[].class);
                goi.onSuccess(animals);
            }

            @Override
            public void onFailure(String response) {
                Log.e("Animals", "Can't get animals from server");
            }
        });
    }

    @Override
    public void getEnclosures(final GetObjectInterface goi) {
        ni.post("enclosures/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Enclosure[] enc = gson.fromJson(response, Enclosure[].class);
                goi.onSuccess(enc);
            }

            @Override
            public void onFailure(String response) {
                Log.e("ENCLOSURES", "Can't get enclosures from server");
            }
        });
    }


    @Override
    public void getNewsFeed(final GetObjectInterface goi) {

        ni.post("Wallfeed/all/" + GlobalVariables.language, new ResponseInterface() {



            @Override
            public void onSuccess(String response) {
                NewsFeed[] newsFeed = gson.fromJson(response, NewsFeed[].class);
                goi.onSuccess(newsFeed);
            }

            @Override
            public void onFailure(String response) {
                Log.e("FEED", "Can't get feed from server");
            }
        });
    }

    @Override
    public void getSpecies() {
        Species species = gson.fromJson(json, Species.class);
    }

    @Override
    public void getSchedule(final GetObjectInterface goi) {
        ni.post("SpecialEvents/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Log.e("Sched1111111", response);
                Schedule[] schedules = gson.fromJson(response, Schedule[].class);
                goi.onSuccess(schedules);
            }

            @Override
            public void onFailure(String response) {
                Log.e("Schedule", "Can't get schedule from server");
            }
        });
    }

    @Override
    public void getPrices(final GetObjectInterface goi) {
        ni.post("prices/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                Price[] prices = gson.fromJson(response, Price[].class);
                goi.onSuccess(prices);
            }

            @Override
            public void onFailure(String response) {
                Log.e("Price", "Can't get prices from server");
            }
        });
    }

    @Override
    public void getOpeningHours(final GetObjectInterface goi) {
        ni.post("OpeningHours/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                OpeningHours[] openingHours = gson.fromJson(response, OpeningHours[].class);
                goi.onSuccess(openingHours);
            }

            @Override
            public void onFailure(String response) {
                Log.e("OpeningHours", "Can't get opening hours from server");
            }
        });
    }

    @Override
    public void getAboutUs(final GetObjectInterface goi) {
        ni.post("about/info/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                AboutUs[] aboutUs = gson.fromJson(response, AboutUs[].class);
                goi.onSuccess(aboutUs);
            }

            @Override
            public void onFailure(String response) {
                Log.e("AboutUs", "Can't get abous us from server");
            }
        });
    }

    @Override
    public void getContactInfo(final GetObjectInterface goi) {
        ni.post("ContactInfos/all/" + GlobalVariables.language, new ResponseInterface() {
            @Override
            public void onSuccess(String response) {
                ContactInfo[] contactInfo = gson.fromJson(response, ContactInfo[].class);
                goi.onSuccess(contactInfo);
            }

            @Override
            public void onFailure(String response) {
                Log.e("ContactInfo", "Can't get contact info from server");
            }
        });
    }
}