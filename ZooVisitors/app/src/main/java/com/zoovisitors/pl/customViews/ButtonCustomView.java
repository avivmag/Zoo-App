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

public class ButtonCustomView extends LinearLayout {

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
        iconText = rootView.findViewById(R.id.icon_text);
        iconImage = rootView.findViewById(R.id.icon_image);
    }

    public int getTextId(){
        return iconText.getId();
    }

    public int getImageId(){
        return iconImage.getId();
    }

    public void designButton(int background, int image, int text, int text_size, int text_color, int button_size){
        setOrientation(VERTICAL);
        iconText.setTextSize(text_size);
        iconText.setText(text);
        iconText.setTextColor(getResources().getColor(text_color));

        iconImage.setImageResource(image);
        LayoutParams layoutParams = new LayoutParams(button_size, button_size);
        layoutParams.setMargins(0,5,0,5);
        iconImage.setLayoutParams(layoutParams);
        setBackgroundColor(getResources().getColor(background));
        iconImage.setOnClickListener(v -> {
                rootView.performClick();
        });
    }
}
