package com.zoovisitors.pl.enclosures;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.zoovisitors.backend.callbacks.GetObjectInterface;
import com.zoovisitors.bl.Memory;
import com.zoovisitors.pl.animals.AnimalActivity;

/**
 * Created by Gili on 28/12/2017.
 */

public class AnimalsRecyclerAdapter extends RecyclerView.Adapter<AnimalsRecyclerAdapter.ViewHolder> {

    private Animal[] animals;
    private int layout;

    public AnimalsRecyclerAdapter(Animal[] animals, int layout){
        this.animals = animals;
        this.layout = layout;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView animal_card_image;
        public TextView animalName;

        public ViewHolder(View itemView){
            super(itemView);
            if (layout == R.layout.enclosure_card) {
                animal_card_image = (ImageView) itemView.findViewById(R.id.enc_card_image);
                animalName = (TextView) itemView.findViewById(R.id.enclosure_card_text);
            }
            else{
                animal_card_image = (ImageView) itemView.findViewById(R.id.animal_card_image);
                animalName = (TextView) itemView.findViewById(R.id.animal_card_text);

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(GlobalVariables.appCompatActivity, AnimalActivity.class);
                    Bundle clickedAnimal = new Bundle();
                    clickedAnimal.putSerializable("animal", animals[pos]);

                    intent.putExtras(clickedAnimal);
                    GlobalVariables.appCompatActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.animalName.setText(animals[i].getName());
        int width = 400;
        int height = 400;

        GlobalVariables.bl.getImage(animals[i].getPictureUrl(), width, height, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.animal_card_image.setImageBitmap((Bitmap) response);
                GlobalVariables.bl.insertStringandBitmap(animals[i].getPictureUrl(), (Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {
                viewHolder.animal_card_image.setImageResource(R.mipmap.no_image_available);
                GlobalVariables.bl.insertStringandBitmap(animals[i].getPictureUrl(), BitmapFactory.decodeResource(
                        GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));

            }
        });
    }

    @Override
    public int getItemCount() {
        return animals.length;
    }
}
