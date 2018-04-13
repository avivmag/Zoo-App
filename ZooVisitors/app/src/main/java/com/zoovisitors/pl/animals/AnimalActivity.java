package com.zoovisitors.pl.animals;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.Species;
import com.zoovisitors.pl.BaseActivity;

public class AnimalActivity extends BaseActivity {

    private Animal animal;
    private Bundle clickedAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clickedAnimal = getIntent().getExtras();
        setContentView(R.layout.activity_animal);
        animal = (Animal) clickedAnimal.getSerializable("animal");

        // Find the view pager that will allow the user to swipe between fragments
        final ViewPager viewPager = (ViewPager) findViewById(R.id.animal_viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        AnimalFragmentAdapter adapter = new AnimalFragmentAdapter(this, getSupportFragmentManager());
        adapter.setAnimal(animal);
//        adapter.setSpecies(species.getAbout());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.animalTab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.getTabAt(0).setText(getResources().getText(R.string.about_animal));
        tabLayout.getTabAt(1).setText(getResources().getText(R.string.about_species));
    }
}
