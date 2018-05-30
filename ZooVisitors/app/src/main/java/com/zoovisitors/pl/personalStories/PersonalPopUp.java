package com.zoovisitors.pl.personalStories;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

public class PersonalPopUp extends BaseActivity {

    Animal.PersonalStories[] stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pop_up);

        //get the clicked personal story
//        Animal.PersonalStories animal = (Animal.PersonalStories) getIntent().getExtras().getSerializable("animal");
        int storyIndex = (int) getIntent().getExtras().getSerializable("animal");


        GlobalVariables.bl.getPersonalStories(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                stories = (Animal.PersonalStories[])response;
            }

            @Override
            public void onFailure(Object response) {

            }
        });

        Animal.PersonalStories animal = stories[storyIndex];

        //calculate the window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));

        //calculate the image size
        int layoutWidth = width/2;

        //initiate the story header
        CustomRelativeLayout storyHeader = new CustomRelativeLayout(getBaseContext(),animal.getPersonalPicture(), animal.getName(), layoutWidth);
        storyHeader.init();

        LinearLayout personalStoryLayout = findViewById(R.id.personal_story_layout);
        personalStoryLayout.addView(storyHeader,0);

        //initiate the story text
        TextView animalStory = findViewById(R.id.personal_story);
        animalStory.setText(animal.getStory());
    }
}
