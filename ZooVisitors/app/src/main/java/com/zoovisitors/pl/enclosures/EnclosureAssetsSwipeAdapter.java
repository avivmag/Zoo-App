package com.zoovisitors.pl.enclosures;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

import java.util.List;

/**
 * Created by Gili on 19/05/2018.
 */

public class EnclosureAssetsSwipeAdapter extends PagerAdapter {
    private List<Bitmap> images;
    private LayoutInflater layoutInflater;

    public EnclosureAssetsSwipeAdapter(List<Bitmap> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos){
        layoutInflater = (LayoutInflater) GlobalVariables.appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.activity_enclosure_assets_popup_content, container, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.enclosure_assets_popup_image);
        imageView.setImageBitmap(images.get(pos));
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int pos, Object object){
        container.removeView((LinearLayout) object);
    }
}
