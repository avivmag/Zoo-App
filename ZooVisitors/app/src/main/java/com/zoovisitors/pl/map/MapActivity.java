package com.zoovisitors.pl.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.zoovisitors.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    private RelativeLayout map_frame;
    private List<OnMapImageView> onMapImageViews;
//    private LinearLayout holder;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        holder = findViewById(R.id.map_layout);
        map_frame = findViewById(R.id.map_frame);
        seekBar = findViewById(R.id.map_zoom_seek_bar);

        onMapImageViews = new ArrayList<OnMapImageView>(){{
            add(new OnMapImageView(map_frame, 0, 0, "zoo_background","", false));
            for(int i = 0; i<5;i++)
            {
                add(new OnMapImageView(map_frame,150 + 150*i, 200 + (int)(Math.random()*100), "animal_" + i,"", true));
            }
        }};
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                for (OnMapImageView onMapImageView :onMapImageViews) {
                    onMapImageView.dX = onMapImageView.view.getX() - event.getRawX();
                    onMapImageView.dY = onMapImageView.view.getY() - event.getRawY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for (OnMapImageView onMapImageView :onMapImageViews) {
                    onMapImageView.view.animate()
                            .x(event.getRawX() + onMapImageView.dX)
                            .y(event.getRawY() + onMapImageView.dY)
                            .setDuration(0)
                            .start();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        for (OnMapImageView onMapImageView : onMapImageViews) {
            if(onMapImageView.initialWidth != 0 && onMapImageView.initialHeight != 0) {
//                android.view.ViewGroup.LayoutParams layoutParams = onMapImageView.view.getLayoutParams();
//                layoutParams.width = (100 + progress) * onMapImageView.initialWidth/100;
//                layoutParams.height = (100 + progress) * onMapImageView.initialHeight/100;
//                onMapImageView.view.setLayoutParams(layoutParams);
//                onMapImageView.view.setLayoutParams(new RelativeLayout.LayoutParams(
//                        (100 + progress) * onMapImageView.initialWidth/100,
//                        (100 + progress) * onMapImageView.initialHeight/100));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        for (OnMapImageView onMapImageView : onMapImageViews) {
            if(onMapImageView.initialWidth == 0 || onMapImageView.initialHeight == 0) {
                onMapImageView.initialWidth = onMapImageView.view.getMeasuredWidth();
                onMapImageView.initialHeight = onMapImageView.view.getMeasuredHeight();
            }
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private class OnMapImageView {
        String resource;
        String url;
        ImageView view;
        float dX, dY;
        int initialWidth, initialHeight;

        public OnMapImageView(RelativeLayout parent, int x, int y, String resource, String url, boolean isButton) {
            if(isButton) {
                view = new ImageButton(MapActivity.this);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("AVIV", "Click");
                    }
                });
            }
            else
                view = new ImageView(MapActivity.this);
            view.setImageResource(getResources().getIdentifier(resource, "mipmap", getPackageName()));
            view.setBackgroundColor(Color.TRANSPARENT);
            this.resource = resource;
            this.url = url;

            view.setOnTouchListener(MapActivity.this);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = x;
            layoutParams.topMargin = y;
//            layoutParams.bottomMargin = -250;
//            layoutParams.rightMargin = -250;
            view.setLayoutParams(layoutParams);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            parent.addView(view, layoutParams);
        }
    }

}

    /*
    private RelativeLayout map_frame;
    private List<OnMapImageView> onMapImageViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map_frame = findViewById(R.id.map_frame);
        onMapImageViews = new ArrayList<OnMapImageView>(){{
            add(new OnMapImageView(map_frame, 0, 0, "zoo_background","", false));
            for(int i = 0; i<5;i++)
            {
                add(new OnMapImageView(map_frame,0, 200*(i), "animal_" + i,"", true));
            }
//            add(new OnMapImageView(map_frame, 0,0, "transparent_pixel","", false));
        }};

//        onMapImageViews.get(onMapImageViews.size() - 1).view.setOnTouchListener(this);
    }



    private class OnMapImageView {
        // these matrices will be used to move and zoom image
//        private Matrix matrix = new Matrix();
//        private Matrix savedMatrix = new Matrix();
//        // remember some things for zooming
//        private PointF start = new PointF();
//        private PointF mid = new PointF();
//        private float oldDist = 1f;
//        private float[] lastEvent = null;

        String resource;
        String url;
        ImageView view;

        public OnMapImageView(RelativeLayout parent, int x, int y, String resource, String url, boolean isButton) {
            if(isButton) {
                view = new ImageButton(MapActivity.this);
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("AVIV", "Clicked!!!");
                        return false;
                    }
                });
            }
            else
                view = new ImageView(MapActivity.this);
            view.setImageResource(getResources().getIdentifier(resource, "mipmap", getPackageName()));
            view.setBackgroundColor(Color.TRANSPARENT);
            this.resource = resource;
            this.url = url;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            parent.addView(view, params);


//            view.setScaleType(ImageView.ScaleType.MATRIX);
//            params.leftMargin = x;
//            params.topMargin = y;
//            matrix.set(savedMatrix);
//            float dx = x - start.x;
//            float dy = y - start.y;
//            matrix.postTranslate(x, y);
//            view.setImageMatrix(matrix);
//            parent.addView(view);

        }
    }


//    // we can be in one of these 3 states
//    private static final int NONE = 0;
//    private static final int DRAG = 1;
//    private static final int ZOOM = 2;
//    private int mode = NONE;
//    public boolean onTouch(View v, MotionEvent event) {
//        // handle touch events here
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                for (int i = 0; i < onMapImageViews.size(); i++) {
//                    onMapImageViews.get(i).savedMatrix.set(onMapImageViews.get(i).matrix);
//                    onMapImageViews.get(i).start.set(event.getX(), event.getY());
//                    onMapImageViews.get(i).lastEvent = null;
//                }
//                mode = DRAG;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                for (int i = 0; i < onMapImageViews.size(); i++) {
//                    onMapImageViews.get(i).oldDist = spacing(event);
//                    if (onMapImageViews.get(i).oldDist > 10f) {
//                        onMapImageViews.get(i).savedMatrix.set(onMapImageViews.get(i).matrix);
//                        midPoint(onMapImageViews.get(i).mid, event);
//                    }
//                    onMapImageViews.get(i).lastEvent = new float[4];
//                    onMapImageViews.get(i).lastEvent[0] = event.getX(0);
//                    onMapImageViews.get(i).lastEvent[1] = event.getX(1);
//                    onMapImageViews.get(i).lastEvent[2] = event.getY(0);
//                    onMapImageViews.get(i).lastEvent[3] = event.getY(1);
//                }
//                if (onMapImageViews.get(0).oldDist > 10f)
//                    mode = ZOOM;
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                mode = NONE;
//                for (int i = 0; i < onMapImageViews.size(); i++)
//                    onMapImageViews.get(i).lastEvent = null;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                for (int i = 0; i < onMapImageViews.size(); i++) {
//                    if (mode == DRAG) {
//                        onMapImageViews.get(i).matrix.set(onMapImageViews.get(i).savedMatrix);
//                        float dx = event.getX() - onMapImageViews.get(i).start.x;
//                        float dy = event.getY() - onMapImageViews.get(i).start.y;
//                        onMapImageViews.get(i).matrix.postTranslate(dx, dy);
//
//                    } else if (mode == ZOOM) {
//                        float newDist = spacing(event);
//                        if (newDist > 10f) {
//
//                            onMapImageViews.get(i).matrix.set(onMapImageViews.get(i).savedMatrix);
//                            float scale = (newDist / onMapImageViews.get(i).oldDist);
//                            onMapImageViews.get(i).matrix.postScale(scale, scale, onMapImageViews.get(i).mid.x, onMapImageViews.get(i).mid.y);
//                            onMapImageViews.get(i).matrix.postTranslate(1, 1);
////                            (scale - 1)*
////                            Log.e("AVIV", "" + scale);
//                        }
//                    }
//                }
//                break;
//        }
//        for (int i = 0; i < onMapImageViews.size(); i++) {
//            onMapImageViews.get(i).view.setImageMatrix(onMapImageViews.get(i).matrix);
//        }
//        return true;
//    }
//
//    /**
//     * Determine the space between the first two fingers
//     */
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    /**
//     * Calculate the mid point of the first two fingers
//     */
//    private void midPoint(PointF point, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//}