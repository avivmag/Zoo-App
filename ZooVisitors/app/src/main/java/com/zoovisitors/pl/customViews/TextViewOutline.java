package com.zoovisitors.pl.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zoovisitors.R;

/**
 * Created by Gili on 30/04/2018.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewOutline extends TextView {
    public TextViewOutline(Context context) {
        super(context);
    }

    public TextViewOutline(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewOutline(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewOutline(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        int textColor = getTextColors().getDefaultColor();
        setTextColor(getResources().getColor(R.color.black));
        getPaint().setStrokeWidth(10);
        getPaint().setStyle(Paint.Style.STROKE);
        super.onDraw(canvas);
        setTextColor(textColor);
        getPaint().setStrokeWidth(0);
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
    }
}
