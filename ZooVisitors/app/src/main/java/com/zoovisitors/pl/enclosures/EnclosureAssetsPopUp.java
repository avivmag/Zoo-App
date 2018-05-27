package com.zoovisitors.pl.enclosures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import com.zoovisitors.R;
import com.zoovisitors.bl.Memory;
import com.zoovisitors.pl.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gili on 19/05/2018.
 */

public class EnclosureAssetsPopUp extends BaseActivity {

    private ViewPager viewPager;
    private EnclosureAssetsSwipeAdapter enclosureAssetsSwipeAdapter;
    private List<Bitmap> imagesInAsset;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure_assets_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        Intent intent = getIntent();
        int arraySize = 0, pos = 0;
        arraySize = intent.getIntExtra("arraySize",0);
        pos = intent.getIntExtra("pos", 0);
        imagesInAsset = new ArrayList<Bitmap>();
        for (int i=0; i < arraySize; i++) {
            String url = intent.getStringExtra("imageUrl" + i);
            imagesInAsset.add(Memory.urlToBitmapMap.get(url));
        }

        viewPager = (ViewPager) findViewById(R.id.enclosure_assets_viewpager);
        enclosureAssetsSwipeAdapter = new EnclosureAssetsSwipeAdapter(imagesInAsset, pos, this);
        viewPager.setAdapter(enclosureAssetsSwipeAdapter);
        viewPager.setCurrentItem(pos);
    }
}
