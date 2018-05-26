package com.zoovisitors.pl.personalStories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.dal.Memory;
import com.zoovisitors.pl.animals.AnimalActivity;
import com.zoovisitors.pl.enclosures.EnclosureListRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 11/04/2018.
 */

public class PersonalStoriesRecyclerAdapter extends RecyclerView.Adapter<PersonalStoriesRecyclerAdapter.ViewHolder> {
    private Animal.PersonalStories[] personalStories;
    private Bitmap[] images;

    public PersonalStoriesRecyclerAdapter(Animal.PersonalStories[] personalStories) {
        this.personalStories = personalStories;
        images = new Bitmap[personalStories.length];
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView animal_card_image;
        public TextView animalName;


        public ViewHolder(View itemView){
            super(itemView);

            animal_card_image = (ImageView) itemView.findViewById(R.id.animal_personal_story_image);
            animalName = (TextView) itemView.findViewById(R.id.animal_personal_story_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp.class);
                    Bundle clickedAnimal = new Bundle();
                    clickedAnimal.putSerializable("animal", personalStories[pos]);
                    intent.putExtra("url", personalStories[pos].getPictureUrl());
                    intent.putExtras(clickedAnimal);
                    GlobalVariables.appCompatActivity.startActivity(intent);
                }
            });
        }

    }

    @Override
    public PersonalStoriesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.animal_personal_story_card, viewGroup, false);
        PersonalStoriesRecyclerAdapter.ViewHolder viewHolder = new PersonalStoriesRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PersonalStoriesRecyclerAdapter.ViewHolder viewHolder, int i) {
        //calculate image witdh according to the screen.
        int screenSize = GlobalVariables.appCompatActivity.getResources().getDisplayMetrics().widthPixels;
        int spaces = 30;
        int imageWidth = screenSize/2 - spaces;

        viewHolder.animalName.setText(personalStories[i].getName());

        GlobalVariables.bl.getImage(personalStories[i].getPictureUrl(), imageWidth, imageWidth, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.animal_card_image.setImageBitmap((Bitmap) response);
                images[i] = (Bitmap) response;
                Memory.urlToBitmapMap.put(personalStories[i].getPictureUrl(), (Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {
                LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(imageWidth, imageWidth);
                viewHolder.animal_card_image.setLayoutParams(layoutParams);
                viewHolder.animal_card_image.setImageResource(R.mipmap.no_image_available);
                images[i] = null;
                Memory.urlToBitmapMap.put(personalStories[i].getPictureUrl(), BitmapFactory.decodeResource(
                        GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));
            }
        });


    }

    @Override
    public int getItemCount() {
        return personalStories.length;
    }

}
