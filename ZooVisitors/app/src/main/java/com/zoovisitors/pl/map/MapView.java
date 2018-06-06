package com.zoovisitors.pl.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.pl.map.icons.Icon;
import com.zoovisitors.pl.map.icons.MiscIcon;
import com.zoovisitors.pl.map.icons.VisitorIcon;
import com.zoovisitors.pl.map.icons.ZooMapIcon;
import com.zoovisitors.pl.map.icons.handlers.EnclosureIconsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zoovisitors.bl.RecurringEventsHandler.getTimeAdjustedToWeekTime;

/**
 * Created by aviv on 12-Jan-18.
 */
public class MapView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;
    private static final long RECURRING_EVENT_SLOW_TIMER_BETWEEN_CALLS_TIME = 250;
    private static final long RECURRING_EVENT_FAST_TIMER_BETWEEN_CALLS_TIME = 50;
    private static final int FOCUS_ON_LOCATION_ANIMATION_TIME = 2000;
    private static final int RATTLE_ANIMATION_HALF_TIME = 100;
    private int primaryImageMargin;

    private int enclosureIdToFocus;
    private Runnable onTouchEvent;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    /**
     * it represents the approximate factor to make all devices show as the same.
     */
    private float equatorScaleFactor = 0f;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 0f;
    private float mLastScaleFactor = 0f;
    private float maxScaleFactor;
    private float minScaleFactor;

    private Timer timer;
    private List<Runnable> timerFastRunnables;
    private List<Runnable> timerSlowRunnables;

    private List<EnclosureIconsHandler> enclosureIconsHandlers;
    private List<MiscIcon> miscIcons;
    private VisitorIcon visitorIcon;
    private ZooMapIcon zooMapIcon;
    private int screenWidth;
    private int screenHeight;

    public void SetInitialValues(Bitmap zooMapBitmap, Enclosure[] enclosures, Misc[] miscs, int
            enclosureIdToFocus, Runnable onTouchEvent) {
        this.enclosureIdToFocus = enclosureIdToFocus;
        this.onTouchEvent = onTouchEvent;
        zooMapIcon = new ZooMapIcon(this, new Object[]{zooMapBitmap});
        visitorIcon = new VisitorIcon(this);
        for (int i = 0; i < enclosures.length; i++)
            if (enclosures[i].getMarkerBitmap() != null) {
                addEnclosure(enclosures[i], i);
            }
        for (Misc misc :
                miscs) {
            if (misc.getMarkerBitmap() != null) {
                addMiscIcon(misc);
            }
        }
    }

    public void SetInitialParameters(int primaryImageWidth, int primaryImageHeight) {
        equatorScaleFactor = ((float) screenWidth / primaryImageWidth + (float) screenHeight /
                primaryImageHeight) / 2;
        maxScaleFactor = equatorScaleFactor * 4;
        minScaleFactor =
                Math.min(
                        (float) screenWidth / (primaryImageWidth + 2 * primaryImageMargin),
                        (float) screenHeight / (primaryImageHeight + 2 * primaryImageMargin)
                );
        mScaleFactor = mLastScaleFactor =
                Math.max(
                        (float) screenWidth / primaryImageWidth,
                        (float) screenHeight / primaryImageHeight
                );
        mPosX = screenWidth / 2 - getmScaleFactor() * zooMapIcon.width / 2;
        mPosY = screenHeight / 2 - getmScaleFactor() * zooMapIcon.height / 2;

        if (enclosureIdToFocus != -1)
            focusOnIconAndRattle(enclosureIdToFocus);
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
        int mTouchSlop = 5;
        return MotionEvent.ACTION_MOVE == ev.getAction() &&
                (Math.abs(ev.getX() - mLastTouchX) > mTouchSlop ||
                        Math.abs(ev.getY() - mLastTouchY) > mTouchSlop);

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    final AtomicBoolean movementInProgress = new AtomicBoolean(false);

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        animationInterrupt = true;
        onTouchEvent.run();
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

                setMPos(x, y);
                mLastScaleFactor = getmScaleFactor();

                updateViewsPosition();

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

    private void setMPos(float deltaX, float deltaY) {
        primaryImageMargin = Math.max(50,
                Math.max(
                        (int) ((screenHeight / getmScaleFactor() - zooMapIcon.height) / 2),
                        (int) ((screenWidth / getmScaleFactor() - zooMapIcon.width) / 2)
                )
        );

        mPosX =
                Math.max(
                        Math.min(
                                (getmPosX() - mLastTouchX) * getmScaleFactor() / mLastScaleFactor
                                        + deltaX,
                                getmScaleFactor() * (zooMapIcon.left - zooMapIcon.width / 2 +
                                        primaryImageMargin)
                        ),
                        screenWidth - getmScaleFactor() * (zooMapIcon.width + primaryImageMargin)
                );
        mPosY =
                Math.max(
                        Math.min(
                                (getmPosY() - mLastTouchY) * getmScaleFactor() / mLastScaleFactor
                                        + deltaY,
                                getmScaleFactor() * (zooMapIcon.top - zooMapIcon.height / 2 +
                                        primaryImageMargin)
                        ),
                        screenHeight - getmScaleFactor() * (zooMapIcon.height + primaryImageMargin)
                );

    }

    public float getmPosX() {
        return mPosX;
    }

    public float getmPosY() {
        return mPosY;
    }

    public float getmScaleFactor() {
        return mScaleFactor;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = Math.min(
                    Math.max(
                            getmScaleFactor() * detector.getScaleFactor(),
                            minScaleFactor),
                    maxScaleFactor
            );
            return true;
        }
    }

    public void addEnclosure(Enclosure enclosure, int encIndex) {
        enclosureIconsHandlers.add(new EnclosureIconsHandler(this, enclosure, timer,
                timerFastRunnables, timerSlowRunnables, encIndex));
    }

    public void addMiscIcon(Misc misc) {
        miscIcons.add(new MiscIcon(this, misc.getMarkerBitmap(), misc.getMarkerX(), misc
                .getMarkerY()));
    }

    private Animation lastVisitorAnimation;
    private long lastTimeStarted;

    public void UpdateVisitorLocation(int x, int y, boolean shouldAnimate) {
        if (x != visitorIcon.left && y != visitorIcon.top) {
            visitorIcon.UpdateVisitorLocation(x, y);
            if (shouldAnimate &&
                    (lastVisitorAnimation == null ||
                            System.currentTimeMillis() - lastTimeStarted >=
                                    lastVisitorAnimation.getDuration())) {
                if (lastVisitorAnimation != null)
                    lastVisitorAnimation.cancel();
                lastTimeStarted = System.currentTimeMillis();
                lastVisitorAnimation = focusOnLocationAnimation(x, y);
            }
        }
    }

    public void HideVisitorIcon() {
        visitorIcon.Hide();
    }

    public void focusOnIconAndRattle(int enclosureId) {
        Icon icon = null;
        for (EnclosureIconsHandler handler :
                enclosureIconsHandlers) {
            if (handler.getEnclosureId() == enclosureId) {
                icon = handler.getEnclosureIcon();
                break;
            }
        }
        if (icon == null)
            return;

        int numberOfRattleTimes = 2;
        focusOnLocationAnimation(icon.left, icon.top, FOCUS_ON_LOCATION_ANIMATION_TIME);
        Icon finalIcon = icon;
        new Handler().postDelayed(() -> {
            rattleViewAnimation(finalIcon.view, numberOfRattleTimes);
        }, FOCUS_ON_LOCATION_ANIMATION_TIME - numberOfRattleTimes * RATTLE_ANIMATION_HALF_TIME);
    }

    public boolean animationInterrupt = false;

    private Animation focusOnLocationAnimation(int x, int y, long duration) {
        float initialMScaleFactor = mScaleFactor;
        float initialMPosX = mPosX;
        float initialMPosY = mPosY;

        animationInterrupt = false;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (animationInterrupt)
                    this.cancel();

                mScaleFactor = mLastScaleFactor = initialMScaleFactor + (maxScaleFactor -
                        initialMScaleFactor) * interpolatedTime;
                mPosX =
                        Math.max(
                                Math.min(
                                        initialMPosX + (screenWidth / 2 - x * maxScaleFactor -
                                                initialMPosX) * interpolatedTime,
                                        mScaleFactor * (zooMapIcon.left - zooMapIcon.width / 2 +
                                                primaryImageMargin)
                                ),
                                screenWidth - mScaleFactor * (zooMapIcon.width + primaryImageMargin)
                        );
                mPosY =
                        Math.max(
                                Math.min(
                                        initialMPosY + (screenHeight / 2 - y * maxScaleFactor -
                                                initialMPosY) * interpolatedTime,
                                        mScaleFactor * (zooMapIcon.top - zooMapIcon.height / 2 +
                                                primaryImageMargin)
                                ),
                                screenHeight - mScaleFactor * (zooMapIcon.height +
                                        primaryImageMargin)
                        );
                updateViewsPosition();
            }
        };
        a.setDuration(duration);
        startAnimation(a);
        return a;
    }

    private Animation focusOnLocationAnimation(int x, int y) {
        // the ratio is 1/currentZoomLevel mil/pixel
        return focusOnLocationAnimation(x, y,
                (long) (Math.sqrt((mPosX - x) * (mPosX - x) + (mPosY - y) * (mPosY - y))
                        / mScaleFactor * equatorScaleFactor));
    }

    private void rattleViewAnimation(View view, int times) {
        Animation biggerAnimation = new ScaleAnimation(
                1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        biggerAnimation.setDuration(RATTLE_ANIMATION_HALF_TIME);
        view.startAnimation(biggerAnimation);

        Animation smallerAnimation = new ScaleAnimation(
                1.1f, 1f, 1.1f, 1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        smallerAnimation.setDuration(RATTLE_ANIMATION_HALF_TIME);
        new Handler().postDelayed(() -> {
            view.startAnimation(smallerAnimation);
        }, RATTLE_ANIMATION_HALF_TIME);
        if (times > 1) {
            new Handler().postDelayed(() -> {
                rattleViewAnimation(view, times - 1);
            }, RATTLE_ANIMATION_HALF_TIME * 2);
        }
    }

    private void updateViewsPosition() {
        zooMapIcon.updateIconPosition();
        for (EnclosureIconsHandler enclosureIconsHandler :
                enclosureIconsHandlers) {
            enclosureIconsHandler.updateIconPositionWithSize();
        }
        for (MiscIcon miscIcon :
                miscIcons) {
            miscIcon.updateIconPosition();
        }
        if (visitorIcon.view.getVisibility() == VISIBLE) {
            visitorIcon.updateIconPosition();
        }
    }
}