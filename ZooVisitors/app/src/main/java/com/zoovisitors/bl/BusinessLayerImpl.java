package com.zoovisitors.bl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.AboutUs;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfo;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.NewsFeed;
import com.zoovisitors.backend.OpeningHours;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.cl.network.NetworkImpl;
import com.zoovisitors.cl.network.NetworkInterface;
import com.zoovisitors.cl.network.ResponseInterface;
import com.zoovisitors.dal.InternalStorage;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by Gili on 13/01/2018.
 */

public class BusinessLayerImpl implements BusinessLayer {
    private NetworkInterface ni;
    private InternalStorage is;
    private Gson gson;
    private String json;

    public BusinessLayerImpl(Activity activity) {
        ni = new NetworkImpl(activity);
        is = new InternalStorage(activity);
        gson = new Gson();
    }


    @Override
    public void getAnimals(int id, final GetObjectInterface goi) {

        Log.e("ANIMALS", ""+id+"/" + GlobalVariables.language);

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
            @SuppressLint("NewApi")
            @Override
            public void onSuccess(String response) {
                Enclosure[] enc = gson.fromJson(response, Enclosure[].class);

//                // TODO: fake recurring events here, need to update the json somehow
//                // TODO: I should add three days to the real recurring events
//                long currentTime = (Calendar.getInstance().getTimeInMillis() + 7*24*60*60*1000 - 3*24*60*60*1000) % (7*24*60*60*1000);
//                for (int i = 0; i < enc.length; i++) {
//                    enc[i].setRecurringEvent(new Enclosure.RecurringEvent[]{
//                            Enclosure.RecurringEvent.createRecurringEvent(1,
//                                    "",
//                                    (currentTime + 5 * 1000) % (7*24*60*60*1000),
//                                    (currentTime + 10 * 1000) % (7*24*60*60*1000), "האכלה"),
//                            Enclosure.RecurringEvent.createRecurringEvent(2,
//                                    "",
//                                    (currentTime + 15 * 1000) % (7*24*60*60*1000),
//                                    (currentTime + 20 * 1000) % (7*24*60*60*1000), "פיפי בפינה")
//                    });
//                }

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

    @Override
    public void getMisc(final GetObjectInterface goi) {
        ni.post("misc/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Misc[] misc = gson.fromJson(response, Misc[].class);

                if (misc.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(misc);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get misc from server");
            }
        });
    }

//    @Override
//    public void getRecurringEvents(final GetObjectInterface goi) {
//        ni.post("recurringEvents/all/" + GlobalVariables.language, new ResponseInterface<String>() {
//            @Override
//            public void onSuccess(String response) {
//                RecurringEvent[] re = gson.fromJson(response, RecurringEvent[].class);
//
//                if (re.length <= 0)
//                    goi.onFailure("No Data in the server");
//                else
//                    goi.onSuccess(re);
//            }
//
//            @Override
//            public void onFailure(String response) {
//                goi.onFailure("Can't get misc from server");
//            }
//        });
//    }

    public void getImage(String url, int width, int height, GetObjectInterface goi) {
            ni.postImage(url, width, height, new ResponseInterface<Bitmap>() {
                @Override
                public void onSuccess(Bitmap response) {
                    goi.onSuccess(response);
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure(response);
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
    public void getPersonalStories(final GetObjectInterface goi){
        ni.post("animals/story/all/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Animal.PersonalStories[] animals = gson.fromJson(response, Animal.PersonalStories[].class);

                if (animals.length <= 0)
                    goi.onFailure("No Data in the server");
                else
                    goi.onSuccess(animals);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Can't get personal stories from the server");
            }
        });
    }

    @Override
    public void sendDeviceId() {
        ni.post("/" + "?deviceID=" + GlobalVariables.firebaseToken, new ResponseInterface<String>() {
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

    public void getImageFullUrl(String url, int width, int height, GetObjectInterface goi) {
        ni.postImageWithoutPrefix(url, width, height, new ResponseInterface<Bitmap>() {
            @Override
            public void onSuccess(Bitmap response) {
                goi.onSuccess(response);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure(response);
            }
        });
    }

    public void updateIfInPark(boolean isInPark, final GetObjectInterface goi){
        ni.post("notification/updateDevice/" + GlobalVariables.firebaseToken + "/" + isInPark, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                goi.onSuccess(response);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure(response);
            }
        });
    }
}