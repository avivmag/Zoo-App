package com.zoovisitors.pl.personalStories;

import android.content.Intent;
import android.graphics.Bitmap;
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

            animal_card_image = (ImageView) itemView.findViewById(R.id.animal_card_image);
            animalName = (TextView) itemView.findViewById(R.id.animal_card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(GlobalVariables.appCompatActivity, PersonalPopUp.class);
                    Bundle clickedAnimal = new Bundle();
                    clickedAnimal.putSerializable("animal", personalStories[pos]);
                    intent.putExtra("image", images[pos]);
                    intent.putExtras(clickedAnimal);
                    GlobalVariables.appCompatActivity.startActivity(intent);
                }
            });
        }

    }

    @Override
    public PersonalStoriesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.animal_card, viewGroup, false);
        PersonalStoriesRecyclerAdapter.ViewHolder viewHolder = new PersonalStoriesRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PersonalStoriesRecyclerAdapter.ViewHolder viewHolder, int i) {
        final int width = 200;
        final int height = 200;

        viewHolder.animalName.setText(personalStories[i].getName());

        GlobalVariables.bl.getImage(personalStories[i].getPictureUrl(), width, height, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.animal_card_image.setImageBitmap((Bitmap) response);
                images[i] = (Bitmap) response;
            }

            @Override
            public void onFailure(Object response) {
                LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(width, height);
                viewHolder.animal_card_image.setLayoutParams(layoutParams);
                viewHolder.animal_card_image.setImageResource(R.mipmap.no_image_available);
                images[i] = null;
            }
        });


    }

    @Override
    public int getItemCount() {
        return personalStories.length;
    }

}
