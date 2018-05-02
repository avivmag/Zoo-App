package com.zoovisitors.pl.schedule;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Schedule;
import com.zoovisitors.bl.callbacks.GetObjectInterface;

/**
 * Created by Gili on 10/03/2018.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<com.zoovisitors.pl.schedule.ScheduleRecyclerAdapter.ViewHolder> {

    private Schedule[] schedulers;

    public ScheduleRecyclerAdapter(Schedule[] schedulers) {

        this.schedulers = schedulers;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView event;
        public TextView date;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.schedule_card_title);
            event = (TextView) itemView.findViewById(R.id.schedule_card_desc);
            date = (TextView) itemView.findViewById(R.id.schedule_card_date);
            image = (ImageView) itemView.findViewById(R.id.enc_card_image);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.title.setText(schedulers[position].getTitle());
        viewHolder.event.setText(schedulers[position].getDescription());
        
        String startDate = schedulers[position].getStartTime().substring(0, 10);
        String endDate = schedulers[position].getEndTime().substring(0, 10);
        String scheduleDate = startDate + " - " + endDate;

        //viewHolder.date.setTextSize(12);
        viewHolder.date.setText(scheduleDate);
        int width = 200;
        int height = 200;
        GlobalVariables.bl.getImage(schedulers[position].getImageUrl(), width, height, new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
                viewHolder.image.setImageBitmap((Bitmap) response);
            }

            @Override
            public void onFailure(Object response) {
                Log.e("IMAGE", (String) response);
            }
        });

//        viewHolder.image.setImageResource(R.mipmap.aligator);


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