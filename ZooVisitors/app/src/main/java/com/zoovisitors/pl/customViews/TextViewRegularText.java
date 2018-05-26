package com.zoovisitors.pl.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zoovisitors.R;

@SuppressLint("AppCompatCustomView")
public class TextViewRegularText extends TextView {
    private int textAlignment;
    public TextViewRegularText(Context context) {
        super(context);
    }

    public TextViewRegularText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewRegularText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextViewRegularText(Context context, int textAlignment){
        super(context);
        this.textAlignment = textAlignment;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        setTextColor(getResources().getColor(R.color.black));
        setTextSize(20);
        setIncludeFontPadding(false);
        setTextAlignment(textAlignment==0 ? TEXT_ALIGNMENT_TEXT_START : textAlignment);
        super.onDraw(canvas);
    }
}
