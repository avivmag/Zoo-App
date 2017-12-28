package com.zoovisitors.pl;


import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 28/12/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] enclosuresImages = {"enclosure0", "enclosure1", "enclosure2", "enclosure3"};
    private String[] enclosuresNames = {"enclosure0", "enclosure1", "enclosure2", "enclosure3"};

    private AppCompatActivity tempActivity;

    private int[] images;

    public RecyclerAdapter(AppCompatActivity appCompatActivity){
        this.tempActivity = appCompatActivity;
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

            enclosure_card_image = (ImageView) itemView.findViewById(R.id.enclosure_card_image);
            enclosureName = (TextView) itemView.findViewById(R.id.enclosure_card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Snackbar.make(v, "Click" + pos, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
