package com.zoovisitors.pl.personalStories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.pl.animals.AnimalActivity;
import com.zoovisitors.pl.enclosures.EnclosureListRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 11/04/2018.
 */

public class PersonalStoriesRecyclerAdapter extends RecyclerView.Adapter<PersonalStoriesRecyclerAdapter.ViewHolder> {
    private String[] animalsImages;
    private int[] images;
    private Animal[] animals;

    public PersonalStoriesRecyclerAdapter(String[] animalsImages, Animal[] animals) {
        this.animalsImages = animalsImages;
        this.animals = animals;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView animal_card_image;
        public TextView animalName;

        public ViewHolder(View itemView){
            super(itemView);
            List<Integer> imagesList = new ArrayList<Integer>();
            for (String image : animalsImages) {
                imagesList.add(GlobalVariables.appCompatActivity.getResources().getIdentifier(image, "mipmap", GlobalVariables.appCompatActivity.getPackageName()));
            }

            int[] ret = new int[imagesList.size()];
            for(int i = 0;i < ret.length;i++)
                ret[i] = imagesList.get(i);

            images = ret;

            animal_card_image = (ImageView) itemView.findViewById(R.id.animal_card_image);
            animalName = (TextView) itemView.findViewById(R.id.animal_card_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(GlobalVariables.appCompatActivity, AnimalActivity.class);
                    Bundle clickedAnimal = new Bundle();
                    clickedAnimal.putInt("image", images[pos]); //Clicked image
                    clickedAnimal.putString("name", animals[pos].getName());
                    clickedAnimal.putSerializable("animal", animals[pos]);

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
        viewHolder.animalName.setText(animals[i].getName());
        viewHolder.animal_card_image.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return animals.length;
    }

}
