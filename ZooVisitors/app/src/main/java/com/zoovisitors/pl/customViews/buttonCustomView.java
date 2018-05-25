package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 11/04/2018.
 */

public class buttonCustomView extends LinearLayout {

    View rootView;
    TextView iconText;
    ImageView iconImage;

    public buttonCustomView(Context context) {
        super(context);
        init(context, null);
    }

    public buttonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public buttonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init (Context context, @Nullable AttributeSet set){
        setOrientation(VERTICAL);
        rootView = inflate(context, R.layout.icon_layout, this);
        iconText = (TextView) rootView.findViewById(R.id.icon_text);
        iconText.setId(generateViewId());
        iconImage = (ImageView) rootView.findViewById(R.id.icon_image);
        iconImage.setId(generateViewId());

    }

    public int getTextId(){
        return iconText.getId();
    }

    public int getImageId(){
        return iconImage.getId();
    }

    public void designButton(int background, int image, int text, int text_size, int text_color, int image_bottom_margin, int button_size){

        iconText.setTextSize(text_size);
        iconText.setText(text);
        iconText.setTextColor(getResources().getColor(text_color));

        iconImage.setImageResource(image);
        LayoutParams layoutParams = new LayoutParams(button_size, button_size);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.bottomMargin = image_bottom_margin;
        iconImage.setLayoutParams(layoutParams);
        setBackgroundColor(getResources().getColor(background));
        iconImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.performClick();
            }
        });
    }
}
