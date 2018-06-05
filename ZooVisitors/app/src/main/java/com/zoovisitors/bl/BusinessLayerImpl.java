package com.zoovisitors.bl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.backend.AboutUs;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.ContactInfoResult;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.MapResult;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.backend.OpeningHoursResult;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.backend.Price;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.backend.callbacks.UpdateInterface;
import com.zoovisitors.cl.network.NetworkImpl;
import com.zoovisitors.cl.network.NetworkInterface;
import com.zoovisitors.backend.callbacks.ResponseInterface;
import com.zoovisitors.dal.DataFromServer;
import com.zoovisitors.dal.InternalStorage;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Gili on 13/01/2018.
 */

public class BusinessLayerImpl implements BusinessLayer {
    private NetworkInterface ni;
    private InternalStorage is;
    private Gson gson;
    private Memory memory;

    public BusinessLayerImpl(Activity activity) {
        ni = new NetworkImpl(activity);
        is = new InternalStorage(activity);
        gson = new Gson();
    }

    //TODO: GILI change Strings (No data in the server) to R.string...

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

//    @Override
//    public Enclosure[] getEnclosures() {
//        return memory.getEnclosures();
//    }

    @Override
    public void getEnclosures(final GetObjectInterface goi) {
        Enclosure[] enclosures = memory.getEnclosures();
        if (enclosures != null)
            goi.onSuccess(enclosures);
        else {
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
                    else{
                        memory.setEnclosures(enc);
                        goi.onSuccess(enc);
                    }
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get enclosures from server");
                }
            });
        }
    }

    @Override
    public Misc[] getMiscs() {
        return memory.getMiscMarkers();
    }

    public void getImage(String url, int width, int height, GetObjectInterface goi) {
        if (url == null)
            goi.onFailure("Null URL");
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
        WallFeed[] wallFeed = memory.getWallFeeds();
        if (wallFeed != null)
            goi.onSuccess(wallFeed);
        else {
            ni.post("Wallfeed/all/" + GlobalVariables.language, new ResponseInterface<String>() {

                @Override
                public void onSuccess(String response) {
                    WallFeed[] wallFeed = gson.fromJson(response, WallFeed[].class);

                    if (wallFeed.length <= 0)
                        goi.onFailure("No Data in the server");
                    else {
                        memory.setWallFeeds(wallFeed);
                        goi.onSuccess(wallFeed);
                    }
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get feed from server");
                }
            });
        }
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
        Price[] prices = memory.getPrices();
        if (prices != null)
            goi.onSuccess(prices);
        else {
            ni.post("prices/all/" + GlobalVariables.language, new ResponseInterface<String>() {
                @Override
                public void onSuccess(String response) {
                    Price[] prices = gson.fromJson(response, Price[].class);

                    if (prices.length <= 0)
                        goi.onFailure("No Data in the server");
                    else{
                        memory.setPrices(prices);
                        goi.onSuccess(prices);
                    }
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get prices from server");
                }
            });
        }
    }

    @Override
    public void getOpeningHours(final GetObjectInterface goi) {
        OpeningHoursResult openingHours = memory.getOpeningHoursResult();
        if (openingHours != null) {
            goi.onSuccess(openingHours);
        }
        else {
            ni.post("OpeningHours/all/" + GlobalVariables.language, new ResponseInterface<String>() {
                @Override
                public void onSuccess(String response) {
                    OpeningHoursResult openingHours = gson.fromJson(response, OpeningHoursResult.class);
                    if (openingHours == null)
                        goi.onFailure("No Data in the server");
                    else
                        memory.setOpeningHoursResult(openingHours);
                        goi.onSuccess(openingHours);
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get opening hours from server");
                }
            });
        }
    }

    @Override
    public void getAboutUs(final GetObjectInterface goi) {
        String aboutUsString = memory.getAboutUs();
        if (aboutUsString != null)
            goi.onSuccess(aboutUsString);
        else {
            ni.post("about/info/" + GlobalVariables.language, new ResponseInterface<String>() {
                @Override
                public void onSuccess(String response) {
                    AboutUs aboutUs = gson.fromJson(response, AboutUs.class);

                    if (aboutUs == null)
                        goi.onFailure("No Data in the server");
                    else{
                        memory.setAboutUs(aboutUs.getAboutUs());
                        goi.onSuccess(aboutUs.getAboutUs());
                    }
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get abous us from server");
                }
            });
        }
    }

    @Override
    public void getContactInfo(final GetObjectInterface goi) {
        ContactInfoResult contactInfoResults = memory.getContactInfoResult();
        if (contactInfoResults != null)
            goi.onSuccess(contactInfoResults);
        else {
            ni.post("ContactInfos/all/" + GlobalVariables.language, new ResponseInterface<String>() {
                @Override
                public void onSuccess(String response) {
                    ContactInfoResult contactInfo = gson.fromJson(response, ContactInfoResult.class);

                    if (contactInfo == null)
                        goi.onFailure("No Data in the server");
                    else {
                        memory.setContactInfoResult(contactInfoResults);
                        goi.onSuccess(contactInfo);
                    }
                }

                @Override
                public void onFailure(String response) {
                    goi.onFailure("Can't get contact info from server");
                }
            });
        }
    }

    @Override
    public Animal.PersonalStories[] getPersonalStories() {
        return memory.getAnimalStories();
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

    public void updateIfInPark(boolean isInPark){
        ni.post("notification/updateDevice/" + GlobalVariables.firebaseToken + "/" + isInPark, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) { }

            @Override
            public void onFailure(String response) { }
        });
    }

    @Override
    public void unsubscribeToNotification(final GetObjectInterface goi) {
        ni.post("notification/unsubscribe/" + GlobalVariables.firebaseToken, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                goi.onSuccess("Unsubscribe success");
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure("Unsubscribe failed");
            }
        });
    }

    @Override
    public void getAudio(String url, GetObjectInterface goi) {
        ni.postAudio(url, new ResponseInterface<MediaPlayer>() {
            @Override
            public void onSuccess(MediaPlayer response) {
                goi.onSuccess(response);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure(response);
            }
        });
    }

    @Override
    public void getAllDataInit(final UpdateInterface updateInterface) {
        ni.getInitDataString("app/all/" + GlobalVariables.language, new UpdateInterface() {
            @Override
            public void onSuccess(Object response) {
                DataFromServer dataFromServer = gson.fromJson((String) response, DataFromServer.class);
                memory = new Memory(dataFromServer.getEnclosures(), dataFromServer.getAnimalStories(),
                        dataFromServer.getMiscMarkers(), dataFromServer.getMapResult(), dataFromServer.getWallFeeds(),
                        dataFromServer.getContactInfoResult(), dataFromServer.getOpeningHoursResult(),
                        dataFromServer.getPrices(), dataFromServer.getAboutUs());

                updateInterface.onSuccess(response);
            }

            @Override
            public void onFailure(Object response) {
                updateInterface.onFailure(response);
            }

            @Override
            public void onUpdate(Object response) {
                updateInterface.onUpdate(response);
            }
        });

    }

    @Override
    public void insertStringandBitmap(String s, Bitmap d) {
        memory.setStringAndBitmap(s, d);
    }

    @Override
    public Bitmap getBitmapByString(String s) {
        return memory.getBitmapByString(s);
    }

    @Override
    public MapResult getMapResult() {
        return memory.getMapResult();
    }

    @Override
    public void getAllRecEvents(GetObjectInterface goi) {
        ni.post("enclosures/recurring/" + GlobalVariables.language, new ResponseInterface<String>() {
            @Override
            public void onSuccess(String response) {
                Enclosure.RecurringEventString[] recurringEvents = gson.fromJson(response, Enclosure.RecurringEventString[].class);
                goi.onSuccess(recurringEvents);
            }

            @Override
            public void onFailure(String response) {
                goi.onFailure(response);
            }
        });
    }

    @Override
    public CustomRelativeLayout[] getEnclosureCards() {
        return memory.getEnclosureCards();
    }

    @Override
    public CustomRelativeLayout[] getAllAnimalCards() {
        List<CustomRelativeLayout> animalCards = new ArrayList<>();
        Collection<List<CustomRelativeLayout>> allAnimalList =  memory.getEnclosuresAnimalCardMap().values();
        for (List<CustomRelativeLayout> animalInEncList : allAnimalList){
            for (CustomRelativeLayout animalCard : animalInEncList){
                animalCards.add(animalCard);
            }
        }
        return (CustomRelativeLayout[]) animalCards.toArray();
    }

    @Override
    public CustomRelativeLayout[] getAnimalCardsInEnclosure(int encId) {
        return (CustomRelativeLayout[]) memory.getEnclosuresAnimalCardMap().get(encId).toArray();
    }

    @Override
    public void setEnclosureCardsInMemory(CustomRelativeLayout[] cards) {
        memory.setEnclosureCards(cards);
    }

    @Override
    public void setAllAnimalCards(CustomRelativeLayout[] cards, Animal[] animals) {
        for (int i=0; i<animals.length; i++){
            List<CustomRelativeLayout> animalCards = memory.getEnclosuresAnimalCardMap().get(animals[i].getEncId());
            if (animalCards == null){
                animalCards = new ArrayList<CustomRelativeLayout>();
                animalCards.add(cards[i]);
                memory.getEnclosuresAnimalCardMap().put(i, animalCards);
            }
            else {
                animalCards.add(cards[i]);
                memory.getEnclosuresAnimalCardMap().put(i, animalCards);
            }
        }
    }
}