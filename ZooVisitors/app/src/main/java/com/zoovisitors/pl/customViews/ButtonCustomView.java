package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 11/04/2018.
 */

public class ButtonCustomView extends RelativeLayout {

    View rootView;
    TextView iconText;
    ImageView iconImage;
    ImageView iconTransImage;

    public ButtonCustomView(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ButtonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init (Context context, @Nullable AttributeSet set){
        rootView = inflate(context, R.layout.icon_layout, this);
        iconText = (TextView) rootView.findViewById(R.id.icon_text);
//        iconText.setId(generateViewId());
        iconImage = (ImageView) rootView.findViewById(R.id.icon_image);
//        iconImage.setId(generateViewId());
        iconTransImage = (ImageView) rootView.findViewById(R.id.icon_image_text);
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
        //layoutParams. = Gravity.CENTER;
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

    public void mainDesignButton(int image, int text){

        iconText.setTextSize(18);
        iconText.setText(text);
        iconText.setTextColor(getResources().getColor(R.color.buttonTextColor));

        //LayoutParams imageLayoutParams = new LayoutParams(400, 900);
        //imageLayoutParams.gravity = Gravity.CENTER;
        //imageLayoutParams.bottomMargin = 20;
        //iconImage.setLayoutParams(imageLayoutParams);
        //iconImage.setScaleType(ImageView.ScaleType.FIT_XY);
        iconImage.setImageResource(image);
        iconImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.performClick();
            }
        });

        //iconTransImage.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        //LayoutParams imageTextlayoutParams = new LayoutParams(200, 200);
        //iconTransImage.setLayoutParams(imageTextlayoutParams);
    }
}
