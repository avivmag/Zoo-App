package com.zoovisitors.pl.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Gili on 30/04/2018.
 */

@SuppressLint("AppCompatCustomView")
public class ImageViewEncAsset extends ImageView {
    public ImageViewEncAsset(Context context) {
        super(context);
    }

    public ImageViewEncAsset(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewEncAsset(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageViewEncAsset(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = 350;
        params.height = 350;
        super.setLayoutParams(params);
    }
}
