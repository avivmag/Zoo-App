package com.zoovisitors.pl.personalStories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.CustomRelativeLayout;

public class PersonalStoriesActivity extends BaseActivity {

    private int layoutWidth;
    private Animal.PersonalStories[] stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stories);

        //set the action bar.
        setActionBar(R.color.lightGreenIcon);

        //calculate the screen width.
        int screenSize = getResources().getDisplayMetrics().widthPixels;
        layoutWidth = screenSize / 2;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layoutWidth, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        params.width = layoutWidth;

        LinearLayout firstCol = findViewById(R.id.first_column_story);
        firstCol.setLayoutParams(params);

        LinearLayout secondCol = findViewById(R.id.second_column_story);
        secondCol.setLayoutParams(params);

        stories = GlobalVariables.bl.getPersonalStories();
        CustomRelativeLayout card;
        for (int i = 0; i < stories.length / 2; i++) {
            card = getCard(stories[i]);
            firstCol.addView(card);
        }

        for (int i = stories.length / 2; i < stories.length; i++) {
            card = getCard(stories[i]);
            secondCol.addView(card);
        }
    }

    private CustomRelativeLayout getCard(Animal.PersonalStories story) {
        CustomRelativeLayout card = new CustomRelativeLayout(getBaseContext(),
                story.getPersonalPicture(), story.getName(),
                layoutWidth);
        card.init();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp.class);
                Bundle clickedAnimal = new Bundle();
                clickedAnimal.putSerializable("animalId", story.getId());
                intent.putExtras(clickedAnimal);
                startActivity(intent);
            }
        });

        return card;
    }
}
