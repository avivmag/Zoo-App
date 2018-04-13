package com.zoovisitors.pl.enclosures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;

import java.util.ArrayList;
import java.util.List;

public class EnclosureListActivity extends AppCompatActivity {
    private RecyclerView recycleViewEnc;
    private RecyclerView.LayoutManager layoutManagerEnc;
    private RecyclerView.Adapter adapterEnc;
    private RecyclerView recycleViewAnim;
    private RecyclerView.LayoutManager layoutManagerAnim;
    private RecyclerView.Adapter adapterAnim;
    private SearchView searchEncAnimal;
    private String[] enclosuresImages = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private String[] enclosuresNames;// = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private Animal[] animals;
    //TODO: Delete this line when we have get images
    private String[] animalsImages = {"chimpanse", "gorilla", "olive_baboon"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure_list);
        GlobalVariables.bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                final Enclosure[] enclosures = (Enclosure[]) response;
                enclosuresNames = new String[enclosures.length];
                for (int i = 0; i<enclosures.length; i++)
                    enclosuresNames[i] = enclosures[i].getName();

                //Adapt the recycle to view the card
                recycleViewEnc = (RecyclerView) findViewById(R.id.enclosure_recycle);
                layoutManagerEnc = new LinearLayoutManager(GlobalVariables.appCompatActivity);
                recycleViewEnc.setLayoutManager(layoutManagerEnc);
                adapterEnc = new EnclosureListRecyclerAdapter(enclosures);
                recycleViewEnc.setAdapter(adapterEnc);


                GlobalVariables.bl.getAllAnimals(new GetObjectInterface() {
                    @Override
                    public void onSuccess(Object response) {

                        animals = (Animal[]) response;

//                        animalsNames = new String[animals.length];
//                        for (int i = 0; i<animals.length; i++)
//                            animalsNames[i] = animals[i].getName();


                        //Adapt the recycle to view the card
                        recycleViewAnim = (RecyclerView) findViewById(R.id.animal_recycle_enc_list);
                        layoutManagerAnim = new LinearLayoutManager(GlobalVariables.appCompatActivity);
                        recycleViewAnim.setLayoutManager(layoutManagerAnim);

                        //adapterAnim = new AnimalsRecyclerAdapter(tempActivity, animalsImages, animals);
                        //recycleViewAnim.setAdapter(adapterAnim);
                    }

                    @Override
                    public void onFailure(Object response) {

                    }
                });

                searchEncAnimal = (SearchView) findViewById(R.id.searchEncAnim);
                searchEncAnimal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query.equals("")) {
                            adapterEnc = new EnclosureListRecyclerAdapter(enclosures);
                            recycleViewEnc.setAdapter(adapterEnc);
                        }

                        List<Enclosure> matchedSearchEnclosures = new ArrayList<Enclosure>();

                        for (Enclosure enc : enclosures){
                            if (enc.getName().contains(query))
                                matchedSearchEnclosures.add(enc);
                        }

                        Enclosure[] enclosuresToAdapt = new Enclosure[matchedSearchEnclosures.size()];
                        for (int i = 0; i< matchedSearchEnclosures.size(); i++) {
                            enclosuresToAdapt[i] = matchedSearchEnclosures.get(i);
                        }

                        adapterEnc = new EnclosureListRecyclerAdapter(enclosuresToAdapt);
                        recycleViewEnc.setAdapter(adapterEnc);

                        //Search for animals

                        List<Animal> matchedSearchAnimals = new ArrayList<Animal>();

                        for (Animal animal : animals){
                            if (animal.getName().contains(query))
                                matchedSearchAnimals.add(animal);
                        }

                        Animal[] animalsToAdapt = new Animal[matchedSearchAnimals.size()];
                        for (int i = 0; i< matchedSearchAnimals.size(); i++) {
                            animalsToAdapt[i] = matchedSearchAnimals.get(i);
                        }

                        adapterAnim = new AnimalsRecyclerAdapter(animalsImages, animalsToAdapt);
                        recycleViewAnim.setAdapter(adapterAnim);


                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }

            @Override
            public void onFailure(Object response) {
                Log.e("ENCLOSURES", "Cant get enclosures");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}