package com.zoovisitors.pl.map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.pl.map.icons.ImageIcon;
import com.zoovisitors.pl.map.icons.MiscIcon;
import com.zoovisitors.pl.map.icons.TextIcon;
import com.zoovisitors.pl.map.icons.VisitorIcon;
import com.zoovisitors.pl.map.icons.ZooMapIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by aviv on 12-Jan-18.
 */

public class MapView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;
    public static final float ALPHA_CHANGE = 0.1f;
    public static final float MINIMAL_IMAGE_ALPHA = 0.2f;
    public static final float MAXIMAL_IMAGE_ALPHA = 1f;
    public static long RECURRING_EVENT_TIMER_ELAPSE_TIME = 30 * 60 * 1000;
    private static long RECURRING_EVENT_SLOW_TIMER_BETWEEN_CALLS_TIME = 250;
    private static long RECURRING_EVENT_FAST_TIMER_BETWEEN_CALLS_TIME = 50;

    public static final int TEXT_VIEW_TEXT_SIZE = 10;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mTouchSlop = 5;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 0f, mLastScaleFactor = 0f, maxScaleFactor, minScaleFactor;

    private Timer timer;
    private List<Runnable> timerFastRunnables;
    private List<Runnable> timerSlowRunnables;

    private List<EnclosureIconsHandler> enclosureIconsHandlers;
    private List<MiscIcon> miscIcons;
    private VisitorIcon visitorIcon;
    private ZooMapIcon zooMapIcon;
    private int screenWidth;
    private int screenHeight;

    public int getIconsOffsetLeft(int left) {
        return (int) (zooMapIcon.left - zooMapIcon.width/2 + left);
//        return (int) ((left - zooMapIcon.width / 2) + screenWidth / getCurrentScaleFactor() / 2);
    }

    public int getIconsOffsetTop(int top) {
        return (int) (zooMapIcon.top - zooMapIcon.height/2 + top);
//        return (int) ((top - zooMapIcon.height / 2) + screenHeight / getCurrentScaleFactor() / 2);
    }

    public void SetInitialParameters(int primaryImageWidth, int primaryImageHeight) {
        mScaleFactor = mLastScaleFactor =
                ((float) screenWidth / primaryImageWidth + (float) screenHeight /
                        primaryImageHeight) / 2;
        maxScaleFactor = mScaleFactor * 2;
        minScaleFactor = mScaleFactor / 2;
    }

    public float getCurrentScaleFactor() {
        return mScaleFactor;
    }

    public MapView(Context context) {
        this(context, null, 0);
    }

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        timer = new Timer();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        enclosureIconsHandlers = new ArrayList<>();
        miscIcons = new ArrayList<>();
        timerFastRunnables = new CopyOnWriteArrayList<>();
        timerSlowRunnables = new CopyOnWriteArrayList<>();
        // screen size
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;

        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          for (Runnable runnable :
                                                  timerFastRunnables) {
                                              runnable.run();
                                          }
                                      }
                                  },
                0,
                RECURRING_EVENT_FAST_TIMER_BETWEEN_CALLS_TIME
        );
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          for (Runnable runnable :
                                                  timerSlowRunnables) {
                                              runnable.run();
                                          }
                                      }
                                  },
                0,
                RECURRING_EVENT_SLOW_TIMER_BETWEEN_CALLS_TIME
        );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            mLastTouchX = ev.getX();
            mLastTouchY = ev.getY();
            mActivePointerId = ev.getPointerId(0);
        }
        if (MotionEvent.ACTION_MOVE == ev.getAction() &&
                (Math.abs(ev.getX() - mLastTouchX) > mTouchSlop ||
                        Math.abs(ev.getY() - mLastTouchY) > mTouchSlop))
            return true;

        return false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    AtomicBoolean movementInProgress = new AtomicBoolean(false);

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                synchronized (movementInProgress) {
                    if (movementInProgress.get())
                        break;
                    movementInProgress.set(true);
                }

                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                Log.e("AVIV", "left " + zooMapIcon.left);
//                Log.e("AVIV", "left " + zooMapIcon.left);

                mPosX = //(mPosX - mLastTouchX) * mScaleFactor / mLastScaleFactor + x;
//                        Math.min(
//                                Math.max(
                        (-screenWidth + zooMapIcon.width)/2 * mScaleFactor;
                //(mPosX - mLastTouchX) * mScaleFactor / mLastScaleFactor + x;
//                                        -zooMapIcon.width / 3
//                                ),
//                                zooMapIcon.left +
//                                        (-screenWidth + zooMapIcon.width) / 2 * mScaleFactor
//                        );

                mPosY = (mPosY - mLastTouchY) * mScaleFactor / mLastScaleFactor + y;
                mLastScaleFactor = mScaleFactor;

                //updateIconPositionWithoutSize(zooMapIcon);
                updateIconPositionWithSize(zooMapIcon);

                for (EnclosureIconsHandler enclosureIconsHandler :
                        enclosureIconsHandlers) {
                    enclosureIconsHandler.updateIconPositionWithSize();
                }
                for (MiscIcon miscIcon :
                        miscIcons) {
                    updateIconPositionWithSize(miscIcon);
                }
                if (visitorIcon.view.getVisibility() == VISIBLE)
                    updateIconPositionWithSize(visitorIcon);

                mLastTouchX = x;
                mLastTouchY = y;

                movementInProgress.set(false);
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

    /**
     * Updates the icon position so the middle of the image will be at the point
     *
     * @param icon
     */
    public void updateIconPositionWithSize(ImageIcon icon) {
        if (icon.width != 0 && icon.height != 0) {
            LayoutParams params =
                    new LayoutParams((int) (icon.width * mScaleFactor), (int) (icon.height *
                            mScaleFactor));

            params.setMargins(
                    (int) ((icon.left - icon.width / 2) * mScaleFactor + mPosX),
                    (int) ((icon.top - icon.height / 2) * mScaleFactor + mPosY),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            icon.view.setLayoutParams(params);
        }
    }

    public void updateIconPositionWithSize(TextIcon icon) {
        if (icon.width != 0 && icon.height != 0) {
            ((ViewGroup.MarginLayoutParams) icon.textView.getLayoutParams()).setMargins(
                    (int) ((icon.left - icon.width / 2) * mScaleFactor + mPosX),
                    (int) ((icon.top - icon.height / 2) * mScaleFactor + mPosY),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            icon.textView.setTextSize(TEXT_VIEW_TEXT_SIZE * mScaleFactor);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = Math.min(
                    Math.max(
                            mScaleFactor * detector.getScaleFactor(),
                            minScaleFactor),
                    maxScaleFactor
            );
//            Log.e("AVIV", "mScaleFactor " + mScaleFactor);
//            mScaleFactor = mScaleFactor * detector.getScaleFactor();
            return true;
        }
    }

    public void addEnclosure(Enclosure enclosure, Drawable resource, int left, int top) {
        enclosureIconsHandlers.add(new EnclosureIconsHandler(this, enclosure, resource, left,
                top, timer, timerFastRunnables, timerSlowRunnables));
    }

    public void addMiscIcon(Drawable resource, Misc misc, int left, int top) {
        miscIcons.add(new MiscIcon(this, resource, misc, left, top));
    }

    public void SetZooMapIcon() {
        zooMapIcon = new ZooMapIcon(this, null, screenWidth / 2, screenHeight / 2);
    }

    public void SetVisitorIcon() {
        visitorIcon = new VisitorIcon(this, null);
    }

    public void ShowVisitorIcon() {
        visitorIcon.Show();
    }

    public void UpdateVisitorLocation(int x, int y) {
        visitorIcon.UpdateVisitorLocation(x, y);
    }

    public void HideVisitorIcon() {
        visitorIcon.Hide();
    }
}