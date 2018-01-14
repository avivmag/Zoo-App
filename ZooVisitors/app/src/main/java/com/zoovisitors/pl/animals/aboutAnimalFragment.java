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

/**
 * A simple {@link Fragment} subclass.
 */

public class aboutAnimalFragment extends Fragment {

    private AppCompatActivity appCompatActivity;
    private String story;


    public aboutAnimalFragment() {

    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TextView storyTextView = (TextView) appCompatActivity.findViewById(R.id.animal_story);
        //Log.e("About", story);
        //storyTextView.setText(story);
        View rootView = inflater.inflate(R.layout.about_animal_res, container, false);
        return rootView;
    }

}
