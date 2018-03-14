package com.zoovisitors.pl.enclosures;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.pl.animals.AnimalActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 28/12/2017.
 */

public class AnimalsRecyclerAdapter extends RecyclerView.Adapter<AnimalsRecyclerAdapter.ViewHolder> {

    private String[] animalsImages;// = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    //private String[] animalsNames; //= {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};

    private AppCompatActivity tempActivity;
    private int[] images;
    private Animal[] animals;

    public AnimalsRecyclerAdapter(AppCompatActivity appCompatActivity, String[] animalsImages, Animal[] animals){
        this.tempActivity = appCompatActivity;
        this.animalsImages = animalsImages;
        this.animals = animals;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView animal_card_image;
        public TextView animalName;

        public ViewHolder(View itemView){
            super(itemView);
            List<Integer> imagesList = new ArrayList<Integer>();
            for (String image : animalsImages) {
//                AppCompatActivity tempActivity = new AppCompatActivity();
                imagesList.add(tempActivity.getResources().getIdentifier(image, "mipmap", tempActivity.getPackageName()));
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

                    Intent intent = new Intent(tempActivity, AnimalActivity.class);
                    Bundle clickedAnimal = new Bundle();
                    clickedAnimal.putInt("image", images[pos]); //Clicked image
                    clickedAnimal.putString("name", animals[pos].getName());
                    clickedAnimal.putSerializable("animal", animals[pos]);

                    intent.putExtras(clickedAnimal);
                    tempActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.animal_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.animalName.setText(animals[i].getName());
        viewHolder.animal_card_image.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return animals.length;
    }
}
