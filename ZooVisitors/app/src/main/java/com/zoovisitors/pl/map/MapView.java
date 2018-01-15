package com.zoovisitors.pl.map;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aviv on 12-Jan-18.
 */

public class MapView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mTouchSlop = 5;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f, mLastScaleFactor = 1.f;

    public MapView(Context context) {
        this(context, null, 0);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        icons = new ArrayList<ImageIcon>();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(MotionEvent.ACTION_DOWN == ev.getAction()){
//            mPosX -= (ev.getX() - mLastTouchX);
//            mPosY -= (ev.getY() - mLastTouchY);
            mLastTouchX = ev.getX();
            mLastTouchY = ev.getY();
            mActivePointerId = ev.getPointerId(0);
        }
        if(MotionEvent.ACTION_MOVE == ev.getAction() &&
                (Math.abs(ev.getX() - mLastTouchX) > mTouchSlop ||
                Math.abs(ev.getY() - mLastTouchY) > mTouchSlop))
            return true;

        return false;
    }
Object lock = new Object();
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                final float dy = y - mLastTouchY;

                mPosX = (mPosX - mLastTouchX)  * mScaleFactor / mLastScaleFactor + x;
                mPosY = (mPosY - mLastTouchY)  * mScaleFactor / mLastScaleFactor + y;
                mLastScaleFactor = mScaleFactor;

                for (ImageIcon icon :
                        icons) {
                    if(icon.width != 0 && icon.height != 0) {
                        RelativeLayout.LayoutParams params =
                                new RelativeLayout.LayoutParams((int) (icon.width * mScaleFactor), (int) (icon.height * mScaleFactor));

                        params.setMargins(
                                (int) (icon.left * mScaleFactor + mPosX),
                                (int) (icon.top * mScaleFactor + mPosY),
                                Integer.MAX_VALUE,
                                Integer.MAX_VALUE);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                        icon.view.setLayoutParams(params);
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            return true;
        }
    }

    List<ImageIcon> icons;
    public void addImageIcon(final String resource, String url, int left, int top)
    {
        icons.add(new ImageIcon(resource, url, left, top));
    }
    public void addImageIcon(final String resource, int left, int top)
    {
        icons.add(new ImageIcon(resource, null, left, top));
    }

    private class ImageIcon {
        private ImageView view;
        private String url;
        private int left;
        private int top;
        private int width;
        private int height;

        ImageIcon(final String resource, String url, int left, int top) {
            this.url = url;

            int resourceId = getResources().getIdentifier(resource, "mipmap", getContext().getPackageName());
            this.left = left;
            this.top = top;
            view = new ImageView(getContext());
            view.setImageResource(resourceId);
            view.setBackgroundColor(Color.TRANSPARENT);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_UP:
                            Log.e("AVIV", resource + " Click");
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            Log.e("AVIV", resource + " Not a click");
                            break;
                    }

                    return true;
                }
            });

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            view.setLayoutParams(layoutParams);

            view.post(new Runnable() {
                @Override
                public void run() {
                    width =  view.getMeasuredWidth();
                    height = view.getMeasuredHeight();
                }
            });
            addView(view);

        }
    }
}
