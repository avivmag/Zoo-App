package com.zoovisitors.pl.enclosures;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zoovisitors.R;
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

        int arraySize = 0;
        getIntent().getIntExtra("arraySize", arraySize);
        imagesInAsset = new ArrayList<Bitmap>();
        for (int i=0; i<arraySize; i++)
            imagesInAsset.add(getIntent().getParcelableExtra("images" + i));

        viewPager = (ViewPager) findViewById(R.id.enclosure_assets_viewpager);
        enclosureAssetsSwipeAdapter = new EnclosureAssetsSwipeAdapter(imagesInAsset);
        viewPager.setAdapter(enclosureAssetsSwipeAdapter);
    }
}
