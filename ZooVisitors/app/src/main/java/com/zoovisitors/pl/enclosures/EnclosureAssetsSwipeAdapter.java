package com.zoovisitors.pl.enclosures;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

import java.util.List;

/**
 * Created by Gili on 19/05/2018.
 */

public class EnclosureAssetsSwipeAdapter extends PagerAdapter {
    private List<Bitmap> images;
    private LayoutInflater layoutInflater;
    private int pos;
    private Activity activity;

    public EnclosureAssetsSwipeAdapter(List<Bitmap> images, int pos, Activity activity) {
        this.images = images;
        this.pos = pos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.activity_enclosure_assets_popup_content, container, false);
        ImageView imageView = item_view.findViewById(R.id.enclosure_assets_popup_image);

        imageView.setImageBitmap(images.get(pos));
        Log.e("VIEW", images.get(pos).toString());
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int pos, Object object){
        container.removeView((LinearLayoutCompat) object);
    }
}
