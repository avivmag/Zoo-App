package com.zoovisitors.pl.schedule;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 10/03/2018.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<com.zoovisitors.pl.schedule.ScheduleRecyclerAdapter.ViewHolder> {

    private Schedule[] schedulers;

    public ScheduleRecyclerAdapter(AppCompatActivity appCompatActivity, Schedule[] schedulers) {

        this.schedulers = schedulers;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView schedule_card_image;
        public TextView event;

        public ViewHolder(View itemView) {
            super(itemView);
            event = (TextView) itemView.findViewById(R.id.schedule_card_text);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.event.setText(schedulers[position].getName());
        //viewHolder.enclosure_card_image.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
}