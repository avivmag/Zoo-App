package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.animals.AnimalActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

public class EnclosureListActivity extends BaseActivity {
    //screen attributes
    private int layoutWidth;
    private LinearLayout.LayoutParams params;
    //main layout
    private LinearLayout mainLayout;
    //enc list attributes
    private TextView encTitle;
    private CustomRelativeLayout[] enclosureCards;
    private LinearLayout firstEncCol;
    private LinearLayout secondEncCol;
    //animal list attributes
    private Animal[] animals;
    private TextView animalTitle;
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

        //get the main layout
        mainLayout = findViewById(R.id.list_layout);

        //initialize the enclosure list attributes
        encTitle = initializeTitle();
        encTitle.setText(getResources().getString(R.string.our_enclosures));
        mainLayout.addView(encTitle,0);
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
                    enclosureCards[i] = getEncCard(enclosures[i], i);
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
                        for (int i = 0 ; i < animals.length; i++){
                            animalCards[i] = getAnCard(animals[i]);
                        }
                    }

                    @Override
                    public void onFailure(Object response) {

                    }
                });


                //initialize the search view
                searchEncAnimal = findViewById(R.id.searchEncAnim);
                searchEncAnimal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        clearSearchList();
                        addMatchEnc(query);
                        addMatchAn(query);

                        InputMethodManager in = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(searchEncAnimal.getWindowToken(), 0);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        clearSearchList();
                        addMatchEnc(newText);
                        addMatchAn(newText);
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
                        InputMethodManager in = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(searchEncAnimal.getWindowToken(), 0);
                    }
                });
            }

            @Override
            public void onFailure(Object response) {
                Log.e("ENCLOSURES", "Cant get enclosures");
            }
        });
    }

    private TextView initializeTitle() {
        TextView title = new TextView(getBaseContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,0,5,0);
        title.setLayoutParams(params);

        title.setPadding(0,5,0,5);
        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(R.color.black));
        return title;
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

//        for (int i = 0; i < firstEncCol.getChildCount(); i++){
//            CustomRelativeLayout enc = (CustomRelativeLayout) firstEncCol.getChildAt(i);
//            if (!(enc.getName().contains(newText))){
//                firstEncCol.removeView(enc);
//            }
//        }
//
//        for (int i = 0; i < secondEncCol.getChildCount(); i++){
//            CustomRelativeLayout enc = (CustomRelativeLayout) firstEncCol.getChildAt(i);
//            if (!(enc.getName().contains(newText))){
//                firstEncCol.removeView(enc);
//            }
//        }
    }

    private void clearSearchList() {
        firstEncCol.removeAllViews();
        secondEncCol.removeAllViews();
        firstAnCol.removeAllViews();
        secondAnCol.removeAllViews();
    }

    private CustomRelativeLayout getAnCard(Animal animal) {
        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),animal.getPictureUrl(), animal.getName(),null, layoutWidth);
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

    private CustomRelativeLayout getEncCard(Enclosure enc, int encIndex) {
        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),enc.getPictureUrl(), enc.getName(),null, layoutWidth);
        card.init();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalVariables.appCompatActivity, EnclosureActivity.class);
                Bundle clickedEnclosure = new Bundle();

                clickedEnclosure.putSerializable("enc", encIndex);
                intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        return card;
    }

}