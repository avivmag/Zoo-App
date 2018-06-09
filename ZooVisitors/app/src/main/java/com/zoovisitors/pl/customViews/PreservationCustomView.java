package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;

/**
 * Created by Gili on 08/06/2018.
 */

public class PreservationCustomView extends LinearLayout {
    private View rootView;
    private TextView preservationText;
    private ImageView preservationImage;


    public PreservationCustomView(Context context) {
        super(context);
        init(context, null);
    }

    public PreservationCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PreservationCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init (Context context, @Nullable AttributeSet set){
        setOrientation(VERTICAL);
        rootView = inflate(context, R.layout.preservation_layout, this);
        preservationText = rootView.findViewById(R.id.preservation_text);
        preservationText.setTextSize(10);
        preservationText.setTextColor(getResources().getColor(R.color.black));
        preservationImage = rootView.findViewById(R.id.preservation_image);
        preservationImage.setScaleType(ImageView.ScaleType.FIT_XY);
        int screenWidth = getResources().getDisplayMetrics().widthPixels / 8 - 6;
        LayoutParams imageLayoutParams = new LayoutParams(screenWidth, screenWidth + 3);
        imageLayoutParams.leftMargin = 2;
        preservationImage.setLayoutParams(imageLayoutParams);

        LayoutParams textLayoutParams = new LayoutParams(screenWidth, screenWidth - 2);

        preservationText.setLayoutParams(textLayoutParams);
    }

    public void setImageText(int imageId, int stringId){
        preservationImage.setImageResource(imageId);
        preservationText.setText(stringId);
    }

    public void choosePreservation(){
        preservationImage.setBackgroundColor(getResources().getColor(R.color.preservation_green));
        preservationText.setBackgroundColor(getResources().getColor(R.color.preservation_green));
    }
}
