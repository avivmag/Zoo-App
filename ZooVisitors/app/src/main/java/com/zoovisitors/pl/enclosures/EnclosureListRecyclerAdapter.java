package com.zoovisitors.pl.enclosures;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.dal.Memory;

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

            enclosure_card_image = (ImageView) itemView.findViewById(R.id.enc_card_image);
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
        viewHolder.enclosureName.setText(enclosures[i].getName());
        GlobalVariables.bl.getImage(enclosures[i].getPictureUrl(), 500, 500, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.enclosure_card_image.setImageBitmap((Bitmap) response);
                Memory.urlToBitmapMap.put(enclosures[i].getPictureUrl(), (Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {
                viewHolder.enclosure_card_image.setImageResource(R.mipmap.no_image_available);
                Memory.urlToBitmapMap.put(enclosures[i].getPictureUrl(), BitmapFactory.decodeResource(
                        GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));
            }
        });
    }

    @Override
    public int getItemCount() {
        return enclosures.length;
    }
}
