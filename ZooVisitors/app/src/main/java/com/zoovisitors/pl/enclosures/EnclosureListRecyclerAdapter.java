package com.zoovisitors.pl.enclosures;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.GetObjectInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 28/12/2017.
 */

public class EnclosureListRecyclerAdapter extends RecyclerView.Adapter<EnclosureListRecyclerAdapter.ViewHolder> {

    //TODO: When we can upload images delete the initialization
    private Enclosure[] enclosures;
    private int[] images;

    public EnclosureListRecyclerAdapter(Enclosure[] enclosures){
        this.enclosures = enclosures;

        //TODO: When we can upload images insert this lines
//        enclosuresImages = new String[enclosures.length];
//        for (int i = 0; i<enclosures.length; i++)
//            enclosuresImages[i] = enclosures[i].getImageURL();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView enclosure_card_image;
        public TextView enclosureName;

        public ViewHolder(View itemView){
            super(itemView);
//            List<Integer> imagesList = new ArrayList<Integer>();
//            for (String image : enclosuresImages) {
////                AppCompatActivity tempActivity = new AppCompatActivity();
//                imagesList.add(GlobalVariables.appCompatActivity.getResources().getIdentifier(image, "mipmap", GlobalVariables.appCompatActivity.getPackageName()));
//
//            }
//
//            int[] ret = new int[imagesList.size()];
//            for(int i = 0;i < ret.length;i++)
//                ret[i] = imagesList.get(i);
//
//            images = ret;

            enclosure_card_image = (ImageView) itemView.findViewById(R.id.schedule_card_image);
            enclosureName = (TextView) itemView.findViewById(R.id.enclosure_card_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent intent = new Intent(GlobalVariables.appCompatActivity, EnclosureActivity.class);
                    Bundle clickedEnclosure = new Bundle();

                    clickedEnclosure.putSerializable("enc", enclosures[pos]);
                    intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                    GlobalVariables.appCompatActivity.startActivity(intent);
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
        GlobalVariables.bl.getImage(enclosures[i].getPictureUrl(), 500, 500, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.enclosureName.setText(enclosures[i].getName());
//                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GlobalVariables.appCompatActivity, null);
//                layoutParams.setMargins(0,0,0,5);
//                viewHolder.enclosure_card_image.setLayoutParams(layoutParams);
                viewHolder.enclosure_card_image.setImageBitmap((Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return enclosures.length;
    }
}
