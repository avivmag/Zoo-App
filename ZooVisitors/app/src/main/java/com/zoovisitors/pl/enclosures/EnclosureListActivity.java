package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.animals.AnimalActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class EnclosureListActivity extends BaseActivity {
//    private RecyclerView recycleViewEnc;
//    private RecyclerView.LayoutManager layoutManagerEnc;
//    private RecyclerView.Adapter adapterEnc;
//    private RecyclerView recycleViewAnim;
//    private RecyclerView.LayoutManager layoutManagerAnim;
//    private RecyclerView.Adapter adapterAnim;
    //screen attributes
    private int layoutWidth;
    private LinearLayout.LayoutParams params;
    //enc list attributes
    private CustomRelativeLayout[] enclosureCards;
    private LinearLayout firstEncCol;
    private LinearLayout secondEncCol;
    //animal list attributes
    private Animal[] animals;
    private CustomRelativeLayout[] animalCards;
    private LinearLayout firstAnCol;
    private LinearLayout secondAnCol;
    //search view attributes
    private SearchView searchEncAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure_list);

        //set the action bar.
        setActionBar(R.color.greenIcon);

        //calculate the screen width.
        int screenSize = getResources().getDisplayMetrics().widthPixels;
        layoutWidth = screenSize/2;

        //initialize the screen layout params
        params = new LinearLayout.LayoutParams(layoutWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = layoutWidth;

        //initialize the enclosure list attributes
        firstEncCol = findViewById(R.id.first_column_enc_list);
        firstEncCol.setLayoutParams(params);

        secondEncCol = findViewById(R.id.second_column_enc_list);
        secondEncCol.setLayoutParams(params);

        //get all enclosures
        GlobalVariables.bl.getEnclosures(new GetObjectInterface() {

            @Override
            public void onSuccess(Object response) {
                Enclosure[] enclosures = (Enclosure[]) response;
                enclosureCards = new CustomRelativeLayout[enclosures.length];

                for (int i = 0; i<enclosures.length; i++)
                {
                    enclosureCards[i] = getEncCard(enclosures[i]);
                }

                addMatchEnc("");

                //initialize the animal list attributes
                firstAnCol = findViewById(R.id.first_column_animals_list);
                firstAnCol.setLayoutParams(params);

                secondAnCol = findViewById(R.id.second_column_animals_list);
                secondAnCol.setLayoutParams(params);

                //get all the animals
                GlobalVariables.bl.getAllAnimals(new GetObjectInterface() {
                    @Override
                    public void onSuccess(Object response) {
                        animals = (Animal[]) response;
                        animalCards = new CustomRelativeLayout[animals.length];

                        //Adapt the recycle to view the card
//                        recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle_enc_list);
//                        layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity);
//                        recycleViewAnim.setLayoutManager(layoutManagerAnim);

                    }

                    @Override
                    public void onFailure(Object response) {

                    }
                });

//                //Adapt the recycle to view the card
//                recycleViewEnc = (RecyclerView) findViewById(R.id.enclosure_recycle);
//                layoutManagerEnc = new LinearLayoutManager(GlobalVariables.appCompatActivity);
//                recycleViewEnc.setLayoutManager(layoutManagerEnc);
//                adapterEnc = new EnclosureListRecyclerAdapter(enclosures);
//                recycleViewEnc.setAdapter(adapterEnc);

//                GlobalVariables.bl.getAllAnimals(new GetObjectInterface() {
//                    @Override
//                    public void onSuccess(Object response) {
//
//                        animals = (Animal[]) response;
//
//                        //Adapt the recycle to view the card
//                        recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle_enc_list);
//                        layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity);
//                        recycleViewAnim.setLayoutManager(layoutManagerAnim);
//
//                    }
//
//                    @Override
//                    public void onFailure(Object response) {
//
//                    }
//                });


                //initialize the search view
                searchEncAnimal = findViewById(R.id.searchEncAnim);
                searchEncAnimal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        clearSearchList();
                        addMatchEnc(query);
                        addMatchAn(query);
                        //if it's empty string - delete everything
//                        if (query.equals("")) {
//
//                            adapterEnc = new EnclosureListRecyclerAdapter(enclosures);
//                            recycleViewEnc.setAdapter(adapterEnc);
//                        }

//                        List<Enclosure> matchedSearchEnclosures = new ArrayList<Enclosure>();


//                        Enclosure[] enclosuresToAdapt = new Enclosure[matchedSearchEnclosures.size()];
//                        for (int i = 0; i< matchedSearchEnclosures.size(); i++) {
//                            enclosuresToAdapt[i] = matchedSearchEnclosures.get(i);
//                        }
//
//                        adapterEnc = new EnclosureListRecyclerAdapter(enclosuresToAdapt);
//                        recycleViewEnc.setAdapter(adapterEnc);

//                        List<Animal> matchedSearchAnimals = new ArrayList<Animal>();

//                        Animal[] animalsToAdapt = new Animal[matchedSearchAnimals.size()];
//                        for (int i = 0; i< matchedSearchAnimals.size(); i++) {
//                            animalsToAdapt[i] = matchedSearchAnimals.get(i);
//                        }
//
//                        adapterAnim = new AnimalsRecyclerAdapter(animalsToAdapt, R.layout.enclosure_card);
//                        recycleViewAnim.setAdapter(adapterAnim);

                        InputMethodManager in = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(searchEncAnimal.getWindowToken(), 0);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        clearSearchList();
                        addMatchEnc(newText);
                        addMatchAn(newText);
//                            adapterEnc = new EnclosureListRecyclerAdapter(enclosures);
//                            recycleViewEnc.setAdapter(adapterEnc);
//                        }

//                        List<Enclosure> matchedSearchEnclosures = new ArrayList<Enclosure>();

//                        boolean layoutTurn = true;
//                        for (CustomRelativeLayout enc : enclosures){
//                            if (enc.getName().contains(newText)){
//                                if (layoutTurn){
//                                    firstEncCol.addView(card);
//                                    layoutTurn = false;
//                                }
//                                else {
//                                    secondEncCol.addView(card);
//                                    layoutTurn = true;
//                                }
////                                matchedSearchEnclosures.add(enc);
//                            }
//                        }

//                        Enclosure[] enclosuresToAdapt = new Enclosure[matchedSearchEnclosures.size()];
//                        for (int i = 0; i< matchedSearchEnclosures.size(); i++) {
//                            enclosuresToAdapt[i] = matchedSearchEnclosures.get(i);
//                        }
//
//                        adapterEnc = new EnclosureListRecyclerAdapter(enclosuresToAdapt);
//                        recycleViewEnc.setAdapter(adapterEnc);

                        //Search for animals

//                        List<Animal> matchedSearchAnimals = new ArrayList<Animal>();

//                        boolean layoutTurn = true;
//                        for (CustomRelativeLayout animal : animals){
//                            if (animal.getName().contains(newText)){
//                                if (layoutTurn){
//                                    firstAnCol.addView(card);
//                                    layoutTurn = false;
//                                }
//                                else {
//                                    secondAnCol.addView(card);
//                                    layoutTurn = true;
//                                }
//                            }
////                                matchedSearchAnimals.add(animal);
//                        }

//                        Animal[] animalsToAdapt = new Animal[matchedSearchAnimals.size()];
//                        for (int i = 0; i< matchedSearchAnimals.size(); i++) {
//                            animalsToAdapt[i] = matchedSearchAnimals.get(i);
//                        }
//
//                        adapterAnim = new AnimalsRecyclerAdapter(animalsToAdapt, R.layout.enclosure_card);
//                        recycleViewAnim.setAdapter(adapterAnim);
                        return true;
                    }
                });

                //setting close button listener
                View closeButton = searchEncAnimal.findViewById(getResources().getIdentifier("android:id/search_close_btn", null, null));
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchEncAnimal.setQuery("", false);
                        clearSearchList();
                        addMatchEnc("");
                    }
                });
            }

            @Override
            public void onFailure(Object response) {
                Log.e("ENCLOSURES", "Cant get enclosures");
            }
        });
    }

    private void addMatchAn(String query) {
        boolean layoutTurn = true;

        for (int i = 0; i < animals.length; i++){
            if (animals[i].getName().contains(query)){
                //lazy initiation.
                if (animalCards[i] == null){
                    animalCards[i] = getAnCard(animals[i]);
                }

                if (layoutTurn){
                    firstAnCol.addView(animalCards[i]);
                    layoutTurn = false;
                }
                else {
                    secondAnCol.addView(animalCards[i]);
                    layoutTurn = true;
                }
            }
        }
    }

    private void addMatchEnc(String newText) {
        boolean layoutTurn = true;
        for (CustomRelativeLayout enc : enclosureCards){
            if (enc.getName().contains(newText)){
                if (layoutTurn){
                    firstEncCol.addView(enc);
                    layoutTurn = false;
                }
                else {
                    secondEncCol.addView(enc);
                    layoutTurn = true;
                }
            }
        }
    }

    private void clearSearchList() {
        firstEncCol.removeAllViews();
        secondEncCol.removeAllViews();
        firstAnCol.removeAllViews();
        secondAnCol.removeAllViews();
    }

    private CustomRelativeLayout getAnCard(Animal animal) {
        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),animal.getPictureUrl(), animal.getName(), layoutWidth);
        card.init();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalVariables.appCompatActivity, AnimalActivity.class);
                Bundle clickedAnimal = new Bundle();
                clickedAnimal.putSerializable("animal", animal);

                intent.putExtras(clickedAnimal);
                GlobalVariables.appCompatActivity.startActivity(intent);
            }
        });

        return card;
    }

    private CustomRelativeLayout getEncCard(Enclosure enc) {
        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),enc.getPictureUrl(), enc.getName(), layoutWidth);
        card.init();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalVariables.appCompatActivity, EnclosureActivity.class);
                Bundle clickedEnclosure = new Bundle();

                clickedEnclosure.putSerializable("enc", enc);
                intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                GlobalVariables.appCompatActivity.startActivity(intent);
            }
        });

        return card;
    }

}