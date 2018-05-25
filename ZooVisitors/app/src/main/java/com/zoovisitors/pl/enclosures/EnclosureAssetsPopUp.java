package com.zoovisitors.pl.enclosures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        setContentView(R.layout.activity_enclosure_assets_popup);

        int arraySize = 0;
        getIntent().getIntExtra("arraySize", arraySize);
        imagesInAsset = new ArrayList<Bitmap>();
        for (int i=0; i<arraySize; i++) {
            byte[] byteArray = getIntent().getByteArrayExtra("image" + i);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

//            imagesInAsset.add(getIntent().getParcelableExtra("images" + i));
            imagesInAsset.add(bmp);
        }

        viewPager = (ViewPager) findViewById(R.id.enclosure_assets_viewpager);
        enclosureAssetsSwipeAdapter = new EnclosureAssetsSwipeAdapter(imagesInAsset);
        viewPager.setAdapter(enclosureAssetsSwipeAdapter);
    }
}
