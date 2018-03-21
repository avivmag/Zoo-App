package com.zoovisitors.pl.schedule;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;

/**
 * Created by Gili on 10/03/2018.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<com.zoovisitors.pl.schedule.ScheduleRecyclerAdapter.ViewHolder> {

    private Schedule[] schedulers;

    public ScheduleRecyclerAdapter(Schedule[] schedulers) {

        this.schedulers = schedulers;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView schedule_card_image;
        public TextView event;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            event = (TextView) itemView.findViewById(R.id.schedule_card_desc);
            date = (TextView) itemView.findViewById(R.id.schedule_card_date);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.event.setText(schedulers[position].getDescription());
        
        String startDate = schedulers[position].getStartTime().substring(0, 10);
        String endDate = schedulers[position].getEndTime().substring(0, 10);
        String scheduleDate = startDate + " - " + endDate;

        viewHolder.date.setText(scheduleDate);
    }

    @Override
    public int getItemCount() {
        return schedulers.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.schedule_card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
}