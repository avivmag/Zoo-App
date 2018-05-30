package com.zoovisitors.pl.animals;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import java.util.HashMap;
import java.util.Map;

public class AnimalActivity extends BaseActivity {

    private Map<Integer, Integer> conservationNumToPicture;
    private CustomRelativeLayout animalHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(R.color.greenIcon);
        setContentView(R.layout.activity_animal);

        //get the animal entity.
        Animal animal = (Animal) getIntent().getExtras().getSerializable("animal");

        //calculate the screen width.
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int layoutWidth = Double.valueOf(screenWidth/1.5).intValue();

        //initialize the animal header
        animalHeader = new CustomRelativeLayout(getBaseContext(),animal.getPictureUrl() ,animal.getName(), animal.getAudioUrl() ,layoutWidth);
        animalHeader.init();

        LinearLayout animalMainLayout = findViewById(R.id.linear_animal_activity);
        animalMainLayout.addView(animalHeader,0);


        //initialize the preservation section
        initializeConversationMap();

        ImageView preservation = findViewById(R.id.preservation_image_view);

        if (animal.getPreservation() == 7 || animal.getPreservation() <= 1){ //means that the preservation is unknown
            LinearLayout animalConservationLayout = findViewById(R.id.animal_conservation_layout);

            animalConservationLayout.removeView(preservation);
            TextViewRegularText doesntKnowTextView = new TextViewRegularText(getApplicationContext());

            doesntKnowTextView.setText(getResources().getString(R.string.doesnt_know));


            animalConservationLayout.addView(doesntKnowTextView);
        }
        else {
            preservation.setImageResource(conservationNumToPicture.get(animal.getPreservation()));
        }

        //initialize animal category
        LinearLayout animalCategory = findViewById(R.id.animal_category_layout);
        if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
            animalCategory.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            animalCategory.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        TextView category = findViewById(R.id.animal_category_text);
        category.setText(animal.getCategory());

        //initialize animal series
        LinearLayout animalSeries = findViewById(R.id.animal_series_layout);
        if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
            animalSeries.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            animalSeries.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        TextView series = findViewById(R.id.animal_series_text);
        series.setText(animal.getSeries());

        //initialize animal family
        LinearLayout animalFamily = findViewById(R.id.animal_family_layout);
        if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
            animalFamily.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else{
            animalFamily.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        TextView family = findViewById(R.id.animal_family_text);
        family.setText(animal.getFamily());

        //initialize animal distribution
        TextView dist = findViewById(R.id.animal_distribution_text);
        dist.setText(animal.getDistribution());

        //initialize animal food
        TextView food = findViewById(R.id.animal_food_text);
        food.setText(animal.getFood());

        //initialize animal reproduction
        TextView rep = findViewById(R.id.animal_reproduction_text);
        rep.setText(animal.getReproduction());

        //initialize animal interesting
        TextView interesting = findViewById(R.id.animal_interesting_text);
        interesting.setText(animal.getInteresting());
    }

    // this method initialize the hash mao of the conservation section.
    private void initializeConversationMap() {
        conservationNumToPicture = new HashMap<>();
        conservationNumToPicture.put(8, R.mipmap.conservation8); //extinct - probably won't be in use
        conservationNumToPicture.put(6, R.mipmap.conservation6); //ExtinctWildlife
        conservationNumToPicture.put(5, R.mipmap.conservation5); //CriticallyEndangered
        conservationNumToPicture.put(4, R.mipmap.conservation4); //Endangered
        conservationNumToPicture.put(3, R.mipmap.conservation3); //Vulnerable
        conservationNumToPicture.put(2, R.mipmap.conservation2); //NearThreatened
        conservationNumToPicture.put(1, R.mipmap.conservation1); //LeastConcern
    }

    @Override
    protected void onPause() {
        animalHeader.stopAudio();
        super.onPause();
    }
}
