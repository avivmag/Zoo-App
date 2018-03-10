package com.zoovisitors.pl.enclosures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.BusinessLayer;
import com.zoovisitors.bl.BusinessLayerImpl;
import com.zoovisitors.bl.GetObjectInterface;

import java.util.ArrayList;
import java.util.List;

public class EnclosureListActivity extends AppCompatActivity {
    private BusinessLayer bl;
    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private SearchView searchEncAnimal;
    private String[] enclosuresImages = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private String[] enclosuresNames;// = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private AppCompatActivity tempActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure_list);
        bl = new BusinessLayerImpl(this);
        bl.getEnclosures(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                final Enclosure[] enclosures = (Enclosure[]) response;
                enclosuresNames = new String[enclosures.length];
                for (int i = 0; i<enclosures.length; i++)
                    enclosuresNames[i] = enclosures[i].getName();
                recycleView = (RecyclerView) findViewById(R.id.enclosure_recycle);
                layoutManager = new LinearLayoutManager(tempActivity);
                recycleView.setLayoutManager(layoutManager);
                searchEncAnimal = (SearchView) findViewById(R.id.searchEncAnim);

                Log.e("TEMP", tempActivity.toString());

                adapter = new EnclosureListRecyclerAdapter(tempActivity, enclosures);
                recycleView.setAdapter(adapter);

                searchEncAnimal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        List<String> tempImagesList = new ArrayList<String>();
                        for (String s : enclosuresImages) {
                            if (s.contains(query))
                                tempImagesList.add(s);
                        }
                        String[] tempImagesArray = new String[tempImagesList.size()];
                        for(int i = 0; i<tempImagesArray.length; i++)
                            tempImagesArray[i] = tempImagesList.get(i);



                        List<String> tempNamesList = new ArrayList<String>();
                        for (String s : enclosuresNames) {
                            if (s.contains(query))
                                tempNamesList.add(s);
                        }
                        String[] tempNamesArray = new String[tempImagesList.size()];
                        for(int i = 0; i<tempNamesArray.length; i++)
                            tempNamesArray[i] = tempNamesList.get(i);



                        adapter = new EnclosureListRecyclerAdapter(tempActivity, enclosures);
                        recycleView.setAdapter(adapter);
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