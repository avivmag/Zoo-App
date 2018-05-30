package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

public class CustomRelativeLayout extends RelativeLayout {
    private String cardImageUrl;
    private String cardText;
    private LayoutInflater mInflater;
    private int size;
    private Bitmap image;

    public CustomRelativeLayout(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }

    public  CustomRelativeLayout(Context context, String cardImageUrl, String cardText, int size){
        super(context);
        mInflater = LayoutInflater.from(context);

        this.cardImageUrl = cardImageUrl;
        this.cardText = cardText;
        this.size = size;
    }

    public  CustomRelativeLayout(Context context, Bitmap image, String cardText, int size){
        super(context);
        mInflater = LayoutInflater.from(context);

        this.image = image;
        this.cardText = cardText;
        this.size = size;
    }

    public void init(){
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = size;
        params.setMargins(2,2,2,2);


        View v = mInflater.inflate(R.layout.custom_relative_layout, this, true);
        v.setLayoutParams(params);

        params.height = size;
        ImageView iv = v.findViewById(R.id.custom_animal_personal_story_image);
        iv.setLayoutParams(params);

        TextView placeHolder = v.findViewById(R.id.place_holder);

        TextView tv = v.findViewById(R.id.custom_animal_personal_story_name);
        LayoutParams lp = (LayoutParams) tv.getLayoutParams();
        lp.addRule(RelativeLayout.ABOVE, placeHolder.getId());

        tv.setText(cardText);


        if(image == null){
            GlobalVariables.bl.getImage(cardImageUrl, size, size, new GetObjectInterface() {
                @Override
                public void onSuccess(Object response) {
                    iv.setImageBitmap((Bitmap) response);

                    GlobalVariables.bl.insertStringandBitmap(cardImageUrl, (Bitmap) response);

                }

                @Override
                public void onFailure(Object response) {
                    iv.setImageResource(R.mipmap.no_image_available);
                    GlobalVariables.bl.insertStringandBitmap(cardImageUrl, BitmapFactory.decodeResource(
                            GlobalVariables.appCompatActivity.getResources(), R.mipmap.no_image_available));
                }
            });
        }
        else{
            iv.setImageBitmap(image);
            GlobalVariables.bl.insertStringandBitmap(this.cardText, image);
        }


    }

    public String getName(){
        return this.cardText;
    }

    public void setName(String name){
        this.cardText = name;
    }

    public void setImage(String imageUrl){
        this.cardImageUrl = imageUrl;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
