package com.zoovisitors.pl.animals;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;

/**
 * A simple {@link Fragment} subclass.
 */

public class aboutAnimalFragment extends Fragment {

    private Animal animal;

    public aboutAnimalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_animal_res, container, false);

        TextView animalStory = (TextView) rootView.findViewById(R.id.story_animal_text);
        animalStory.setText(animal.getInteresting());

        return rootView;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
