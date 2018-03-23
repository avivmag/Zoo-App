package com.zoovisitors.bl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
import com.zoovisitors.dal.data_handler.InternalStorage;

/**
 * Created by Gili on 13/01/2018.
 */

public class BusinessLayerImpl implements BusinessLayer {

    private NetworkInterface ni;
    private InternalStorage is;
    private Gson gson;
    private String json;

    public BusinessLayerImpl(AppCompatActivity appCompatActivity) {
        ni = new NetworkImpl(appCompatActivity);
        is = new InternalStorage(appCompatActivity);
        gson = new Gson();
    }


    @Override
    public void getAnimals(int id, final GetObjectInterface goi) {

        // TODO: example for how to retrieve data from the network, be aware that you should update your ip in GlobalVariables class.
        Log.e("ID", "" + id);
        ni.post("/animals/enclosure/" + id + "/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Animal[] animals = gson.fromJson(response, Animal[].class);

                if (animals.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(animals);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get animals from the server");
            }
        });



        //getJson("animals/1");
    }

    @Override
    public void getAllAnimals(final GetObjectInterface goi) {
        ni.post("animals/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Animal[] animals = gson.fromJson(response, Animal[].class);

                if (animals.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(animals);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get animals from server");
            }
        });
    }

    @Override
    public void getEnclosures(final GetObjectInterface goi) {
        ni.post("enclosures/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Enclosure[] enc = gson.fromJson(response, Enclosure[].class);

                if (enc.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(enc);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get enclosures from server");
            }
        });
    }

    public void getImage(String url, GetObjectInterface goi) {
        // if resource exists
        if(is.isExists(url)) {
            Drawable d = is.loadImageFromInternalStorage(url);
            if(d == null)
                goi.onFailure("file not found.");
            else
                goi.onSuccess(d);
        } else {

        }
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        is.saveImageToInternalStorage(url, response);
                        getImage(url, goi);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        goi.onFailure("Cannot reach url");
                    }
                });
    }

    @Override
    public void getNewsFeed(final GetObjectInterface goi) {

        ni.post("Wallfeed/all/" + GlobalVariables.language, new ResponseInterface<String>() {



            @Override
            public void onSuccess(String response) {
                NewsFeed[] newsFeed = gson.fromJson(response, NewsFeed[].class);

                if (newsFeed.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(newsFeed);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get feed from server");
            }
        });
    }

    @Override
    public void getSchedule(final GetObjectInterface goi) {
        ni.post("SpecialEvents/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Schedule[] schedules = gson.fromJson(response, Schedule[].class);
                if (schedules.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(schedules);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure( "Can't get schedule from server");
            }
        });
    }

    @Override
    public void getPrices(final GetObjectInterface goi) {
        ni.post("prices/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Price[] prices = gson.fromJson(response, Price[].class);

                if (prices.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(prices);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get prices from server");
            }
        });
    }

    @Override
    public void getOpeningHours(final GetObjectInterface goi) {
        ni.post("OpeningHours/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                OpeningHours[] openingHours = gson.fromJson(response, OpeningHours[].class);

                if (openingHours.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(openingHours);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get opening hours from server");
            }
        });
    }

    @Override
    public void getAboutUs(final GetObjectInterface goi) {
        ni.post("about/info/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                AboutUs[] aboutUs = gson.fromJson(response, AboutUs[].class);

                if (aboutUs.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(aboutUs);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get abous us from server");
            }
        });
    }

    @Override
    public void getContactInfo(final GetObjectInterface goi) {
        ni.post("ContactInfos/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                ContactInfo[] contactInfo = gson.fromJson(response, ContactInfo[].class);

                if (contactInfo.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(contactInfo);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get contact info from server");
            }
        });
    }

    @Override
    public void sendDeviceId() {
        ni.post("/" + "?deviceID=" + GlobalVariables.deviceId, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Log.e("DeviceID","Succeed sending");
            }

            @Override
            public void onFailure(String response) {
                Log.e("DeviceID","cannot send to server");
            }
        });
    }
}