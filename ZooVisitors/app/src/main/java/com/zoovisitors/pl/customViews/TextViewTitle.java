package com.zoovisitors.pl.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.zoovisitors.R;

@SuppressLint("AppCompatCustomView")
public class TextViewTitle extends TextView {
    private int textAlignment;

    public TextViewTitle(Context context) {
        super(context);
    }

    public TextViewTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public  TextViewTitle(Context context, int textAlignment){
        super(context);
        this.textAlignment = textAlignment;
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        setTextColor(getResources().getColor(R.color.black));
        setTextSize(18);
        setIncludeFontPadding(false);
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setTypeface(null, Typeface.BOLD);
        setTextAlignment(textAlignment==0 ? TEXT_ALIGNMENT_TEXT_START : textAlignment);
        super.onDraw(canvas);
    }
}
