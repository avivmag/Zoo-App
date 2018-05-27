package com.zoovisitors.pl.personalStories;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.bl.Memory;
import com.zoovisitors.pl.BaseActivity;

public class PersonalPopUp extends BaseActivity {

    private int width;
    private int height;
    private Animal.PersonalStories animal;
    private Bundle clickedAnimal;
    private TextView animalName;
    private TextView animalStory;
    private ImageView animalPic;
    private Drawable image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pop_up);
        String url = getIntent().getStringExtra("url");
        image = GlobalVariables.bl.getDrawableByString(url);
        clickedAnimal = getIntent().getExtras();
        animal = (Animal.PersonalStories) clickedAnimal.getSerializable("animal");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));

        animalName = (TextView) findViewById(R.id.personal_animal_name);
        animalStory = (TextView) findViewById(R.id.personal_story);
        animalPic = (ImageView) findViewById(R.id.personal_animal_pic);

        animalName.setText(animal.getName());
        animalStory.setText(animal.getStory());

        if (image != null)
            animalPic.setImageDrawable(image);
        else
            animalPic.setImageResource(R.mipmap.no_image_available);

    }
}
