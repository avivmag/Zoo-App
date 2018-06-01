package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 11/04/2018.
 */

public class MainButtonCustomView extends RelativeLayout {

    View rootView;
    TextView iconText;
    ImageView iconImage;

    private int imageId;
    private int height;
    private String buttonText;
    private OnClickListener listener;

    public MainButtonCustomView(Context context) {
        super(context);
        init(context, null);
    }

    public MainButtonCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainButtonCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MainButtonCustomView(Context context, int imageId, String buttonText, int height, OnClickListener listener) {
        super(context);

        this.height = height;
        this.buttonText = buttonText;
        this.imageId = imageId;
        this.listener = listener;

        init(context, null);
    }

    private void init (Context context, @Nullable AttributeSet set){
        //get the root view
        rootView = inflate(context, R.layout.menu_icon_layout, this);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height*12/10 ));

        //initiates iconText
        iconText = rootView.findViewById(R.id.icon_text);
        LayoutParams textParams = (LayoutParams) iconText.getLayoutParams();

        textParams.height = height*3/5;
        textParams.width = height*10/6;

        textParams.bottomMargin = 15;
        iconText.setLayoutParams(textParams);
        iconText.setText(buttonText);
        if (listener != null)
            iconText.setOnClickListener(listener);

        //if it's russian than it's to long
        if (GlobalVariables.language == 4){
           iconText.setTextSize(12);
           ((LayoutParams) iconText.getLayoutParams()).bottomMargin = 0;
        }

        iconImage = rootView.findViewById(R.id.icon_image);
        LayoutParams iconParams = (LayoutParams) iconImage.getLayoutParams();

        iconParams.height = height;
        iconParams.width = height*9/8;

        iconImage.setLayoutParams(iconParams);

        iconImage.setImageResource(imageId);
        if (listener != null)
            iconImage.setOnClickListener(listener);

    }
}
