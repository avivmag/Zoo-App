package com.zoovisitors.pl.enclosures;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 28/12/2017.
 */

public class EnclosureListRecyclerAdapter extends RecyclerView.Adapter<EnclosureListRecyclerAdapter.ViewHolder> {

    //TODO: When we can upload images delete the initialization
    private String[] enclosuresImages = {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private String[] enclosuresNames; //= {"monkeys_enclosure", "african_enclosure", "reptiles_enclosure", "birds_enclosure"};
    private Enclosure[] enclosures;

    private AppCompatActivity tempActivity;
    private int[] images;

    public EnclosureListRecyclerAdapter(AppCompatActivity appCompatActivity, Enclosure[] enclosures){
        this.tempActivity = appCompatActivity;
        this.enclosures = enclosures;

        enclosuresNames = new String[enclosures.length];
        for (int i = 0; i<enclosures.length; i++)
            enclosuresNames[i] = enclosures[i].getName();

        //TODO: When we can upload images insert this lines
//        enclosuresImages = new String[enclosures.length];
//        for (int i = 0; i<enclosures.length; i++)
//            enclosuresImages[i] = enclosures[i].getImage();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView enclosure_card_image;
        public TextView enclosureName;

        public ViewHolder(View itemView){
            super(itemView);
            List<Integer> imagesList = new ArrayList<Integer>();
            for (String image : enclosuresImages) {
//                AppCompatActivity tempActivity = new AppCompatActivity();
                imagesList.add(tempActivity.getResources().getIdentifier(image, "mipmap", tempActivity.getPackageName()));

            }

            int[] ret = new int[imagesList.size()];
            for(int i = 0;i < ret.length;i++)
                ret[i] = imagesList.get(i);

            images = ret;

            enclosure_card_image = (ImageView) itemView.findViewById(R.id.schedule_card_image);
            enclosureName = (TextView) itemView.findViewById(R.id.enclosure_card_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(tempActivity, EnclosureActivity.class);
                    Bundle clickedEnclosure = new Bundle();
                    clickedEnclosure.putInt("image", images[pos]); //Clicked image
                    clickedEnclosure.putString("name", enclosuresNames[pos]);
                    clickedEnclosure.putInt("id", enclosures[pos].getId());
                    clickedEnclosure.putString("story", enclosures[pos].getStory());
                    intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                    tempActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.enclosure_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.enclosureName.setText(enclosuresNames[i]);
        viewHolder.enclosure_card_image.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return enclosuresNames.length;
    }
}
