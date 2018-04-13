package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 11/04/2018.
 */

public class buttonCustomView extends View {
    public buttonCustomView(Context context) {
        super(context);
        init(null);
    }

    public buttonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public buttonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init (@Nullable AttributeSet set){
        ImageView img = new ImageView(GlobalVariables.appCompatActivity);
        img.setImageResource(R.mipmap.african_enclosure);
    }
}
