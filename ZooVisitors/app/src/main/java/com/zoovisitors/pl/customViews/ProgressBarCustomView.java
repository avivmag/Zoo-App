package com.zoovisitors.pl.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zoovisitors.R;

/**
 * Created by Gili on 24/05/2018.
 */

public class ProgressBarCustomView extends LinearLayout {

    private View rootView;
    private TextView progressText;
    private ProgressBar pb;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarCustomView(Context context) {
        super(context);
        init(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init (Context context, @Nullable AttributeSet set){
        setOrientation(VERTICAL);
        rootView = inflate(context, R.layout.progress_bar_layout, this);
        progressText = (TextView) rootView.findViewById(R.id.progress_text);
        pb = (ProgressBar) rootView.findViewById(R.id.progress_progress);
        pb.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.orangeNegev), android.graphics.PorterDuff.Mode.SRC_IN);
//        pb.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.orangeNegev)));
    }

    public void setProgressPrecentage(int percentage){
        pb.setProgress(percentage);
        progressText.setText("" + percentage + "%");
    }
}
