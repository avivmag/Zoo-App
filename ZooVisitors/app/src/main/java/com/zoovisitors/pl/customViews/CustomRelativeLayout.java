package com.zoovisitors.pl.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.callbacks.GetObjectInterface;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

public class CustomRelativeLayout extends RelativeLayout {
    private String cardImageUrl;
    private String cardText;
    private LayoutInflater mInflater;
    private int size;
    private Bitmap image;

    //audio entities
    private String audioUrl;
    private boolean isPlaying;
    private ImageView audioImage;
    private MediaPlayer mp;

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

    public  CustomRelativeLayout(Context context, String cardImageUrl, String cardText, String audioUrl, int size){
        super(context);
        mInflater = LayoutInflater.from(context);

        this.cardImageUrl = cardImageUrl;
        this.cardText = cardText;
        this.size = size;
        this.audioUrl = audioUrl;
    }

    public  CustomRelativeLayout(Context context, Bitmap image, String cardText, int size){
        super(context);
        mInflater = LayoutInflater.from(context);

        this.image = image;
        this.cardText = cardText;
        this.size = size;
    }

    public void init(){
        //card initiate
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = size-6;
        params.setMargins(2,2,2,2);

        View v = mInflater.inflate(R.layout.custom_relative_layout, this, true);
        v.setLayoutParams(params);
        v.setPadding(2,4,2,4);

        //card image
        params.height = size;
        ImageView iv = v.findViewById(R.id.custom_animal_personal_story_image);
        iv.setLayoutParams(params);

        //place holder
        TextView placeHolder = v.findViewById(R.id.place_holder);

        //card name
        TextView tv = v.findViewById(R.id.custom_animal_personal_story_name);
        LayoutParams lp = (LayoutParams) tv.getLayoutParams();
        lp.addRule(RelativeLayout.ABOVE, placeHolder.getId());

        tv.setText(cardText);

        //audio
        audioImage = findViewById(R.id.custom_audio_image);

        LayoutParams imageParams = (LayoutParams)audioImage.getLayoutParams();
        imageParams.setMargins(5,5,5,5);
        imageParams.width = size/6;
        imageParams.height = size/6;
        imageParams.addRule(RelativeLayout.ABOVE, tv.getId());

        audioImage.setLayoutParams(imageParams);
        audioImage.setImageResource(R.mipmap.audio);

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

        if (audioUrl != null){
            GlobalVariables.bl.getAudio(audioUrl, new GetObjectInterface() {

                @Override
                public void onSuccess(Object response) {
                    mp = (MediaPlayer) response;
                    setOnClickListener(v -> {
                        audioClick();
                    });
                }
                @Override
                public void onFailure(Object response) {
                    audioImage.setImageDrawable(null);
                }
            });
        }
        else{
            audioImage.setImageDrawable(null);
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

    private void audioClick() {
        if (!isPlaying && audioImage != null) {
            AudioManager am = (AudioManager) GlobalVariables.appCompatActivity.getSystemService(AUDIO_SERVICE);

            int volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);

            if (volume_level < 9)
                Toast.makeText(GlobalVariables.appCompatActivity, getResources().getString(R.string.turn_the_volume), Toast.LENGTH_LONG).show();
            isPlaying = true;
            audioImage.setImageResource(R.mipmap.no_audio);
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        } else {
            if (audioImage != null) {
                isPlaying = false;
                mp.stop();
                if (audioImage != null)
                    audioImage.setImageResource(R.mipmap.audio);
            }
        }
    }

    public void stopAudio() {
        if (isPlaying && audioImage != null){
            mp.stop();
        }
    }
}
