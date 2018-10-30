package com.zoovisitors.pl.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
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

import static com.zoovisitors.pl.map.MapActivity.OPEN_DOORS_ANIMATION_DURATION;

/**
 * Created by aviv on 12-Jan-18.
 */
public class MapView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;
    private static final long RECURRING_EVENT_SLOW_TIMER_BETWEEN_CALLS_TIME = 250;
    private static final long RECURRING_EVENT_FAST_TIMER_BETWEEN_CALLS_TIME = 50;
    private static final int FOCUS_ON_LOCATION_ANIMATION_TIME = 2000;
    private static final int RATTLE_ANIMATION_HALF_TIME = 100;

    private float mPosX;
    private float mPosY;
    private float mScaleFactor = 0f;
    /**
     * it represents the approximate factor to make all devices show as the same.
     */
    private float equatorScaleFactor = 0f;
    private ScaleGestureDetector mScaleDetector;
    private float mLastScaleFactor = 0f;
    private float maxScaleFactor;
    private float minScaleFactor;
    private int mapImageMargin;
    private int enclosureIdToFocus;
    private Runnable onTouchEvent;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private Timer timer;
    private List<Runnable> timerFastRunnables;
    private List<Runnable> timerSlowRunnables;
    private List<EnclosureIconsHandler> enclosureIconsHandlers;
    private List<MiscIcon> miscIcons;
    private VisitorIcon visitorIcon;
    private ZooMapIcon zooMapIcon;
    private int screenWidth;
    private int screenHeight;
    private final AtomicBoolean movementInProgress = new AtomicBoolean(false);
    private Animation lastVisitorAnimation;
    private long lastTimeStarted;
    private boolean animationInterrupt = false;

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
        screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        //                                                                  Title bar size
        screenHeight = this.getResources().getDisplayMetrics().heightPixels - 72;
        initiateRecurringEventTimers();
    }

    private void initiateRecurringEventTimers() {
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
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
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
    public boolean onTouchEvent(MotionEvent ev) {
        setAnimationInterrupted(true);
        onTouchEvent.run();
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                handleMotionMove(ev);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                handleMotionPointerUp(ev);
                break;
            }
        }

        return true;
    }
    private void handleMotionMove(MotionEvent ev) {
        synchronized (movementInProgress) {
            if (movementInProgress.get())
                return;
            movementInProgress.set(true);
        }

        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex == INVALID_POINTER_ID) {
            movementInProgress.set(false);
            return;
        }
        final float x = ev.getX(pointerIndex);
        final float y = ev.getY(pointerIndex);
        updateMPos(x, y);
        mLastScaleFactor = mScaleFactor;
        updateViewsPosition();

        mLastTouchX = x;
        mLastTouchY = y;

        movementInProgress.set(false);
    }

    private void updateMPos(float deltaX, float deltaY) {
        setMapImageMargin(mScaleFactor);
        mPosX =
                Math.max(
                        Math.min(
                                (mPosX - mLastTouchX) * mScaleFactor / mLastScaleFactor + deltaX,
                                mScaleFactor * (zooMapIcon.left - zooMapIcon.width / 2 + mapImageMargin)
                        ),
                        screenWidth - mScaleFactor * (zooMapIcon.width + mapImageMargin)
                );
        mPosY =
                Math.max(
                        Math.min(
                                (mPosY - mLastTouchY) * mScaleFactor / mLastScaleFactor + deltaY,
                                mScaleFactor * (zooMapIcon.top - zooMapIcon.height / 2 + mapImageMargin)
                        ),
                        screenHeight - mScaleFactor * (zooMapIcon.height + mapImageMargin)
                );

    }

    private void handleMotionPointerUp(MotionEvent ev) {
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
    }

    /**
     * @param zooMapBitmap
     * @param enclosures
     * @param miscs
     * @param enclosureIdToFocus
     * @param parentOnTouch         Callback that notifies the parent that the map view has been
     *                              touched.
     * @param onEnclosureClick      Callback that notifies the parent that an enclosure has been
     *                              clicked.
     * @param enclosureDelayToClick Specifies how much time the view should wait before handling
     *                              click on enclosure.
     */
    public void initiateVariables(Bitmap zooMapBitmap, Pair<Integer, Enclosure>[] enclosures,
                                  Misc[] miscs, int enclosureIdToFocus, Runnable parentOnTouch,
                                  Runnable onEnclosureClick, long enclosureDelayToClick) {
        this.enclosureIdToFocus = enclosureIdToFocus;
        this.onTouchEvent = parentOnTouch;
        zooMapIcon = new ZooMapIcon(this, new Object[]{zooMapBitmap});
        visitorIcon = new VisitorIcon(this);
        for (Pair<Integer, Enclosure> pair : enclosures) {
            addEnclosure(pair.second, pair.first,
                    onEnclosureClick,
                    enclosureDelayToClick);
        }
        for (Misc misc : miscs) {
            if (misc.getMarkerBitmap() != null) {
                addMiscIcon(misc);
            }
        }
    }

    /**
     * Setting important variables after the map has been initialized
     *
     * @param primaryImageWidth
     * @param primaryImageHeight
     */
    public void initiateVariablesWithSize(int primaryImageWidth, int primaryImageHeight) {
        initiateScaleFactors(primaryImageWidth, primaryImageHeight);
        setMapImageMargin(mScaleFactor);
        initiateMPos();
        initiateEnterMapAnimation(primaryImageWidth, primaryImageHeight);
    }

    private void initiateScaleFactors(int primaryImageWidth, int primaryImageHeight) {
        equatorScaleFactor = ((float) screenWidth / primaryImageWidth + (float) screenHeight /
                primaryImageHeight) / 2;
        maxScaleFactor = equatorScaleFactor * 4;
        minScaleFactor =
                Math.min(
                        (float) screenWidth / (primaryImageWidth + 2 * mapImageMargin),
                        (float) screenHeight / (primaryImageHeight + 2 * mapImageMargin)
                );
        mScaleFactor = mLastScaleFactor = minScaleFactor;
    }

    private void setMapImageMargin(float scaleFactor) {
        mapImageMargin = Math.max(50,
                Math.max(
                        (int) ((screenHeight / scaleFactor - zooMapIcon.height) / 2),
                        (int) ((screenWidth / scaleFactor - zooMapIcon.width) / 2)
                )
        );
    }

    private void initiateMPos() {
        mPosX = Math.min(
                screenWidth / 2 - zooMapIcon.width / 2 * mScaleFactor,
                mScaleFactor * (zooMapIcon.left - zooMapIcon.width / 2 + mapImageMargin)
        );
        mPosY =
                Math.min(
                        screenHeight / 2 - mScaleFactor * zooMapIcon.height / 2,
                        mScaleFactor * (zooMapIcon.top - zooMapIcon.height / 2 + mapImageMargin)
                );
    }

    private void initiateEnterMapAnimation(int primaryImageWidth, int primaryImageHeight) {
        new Handler().postDelayed(() -> {
            if (enclosureIdToFocus != -1) {
                focusOnIconAndRattle(enclosureIdToFocus);
            } else {
                focusOnLocationAnimation(
                        zooMapIcon.width / 2,
                        zooMapIcon.height / 2,
                        Math.max(
                                (float) screenWidth / primaryImageWidth,
                                (float) screenHeight / primaryImageHeight
                        ),
                        OPEN_DOORS_ANIMATION_DURATION);
            }
        }, OPEN_DOORS_ANIMATION_DURATION);
    }

    public void InitiateExitMapAnimation() {
        setMapImageMargin(minScaleFactor);

        focusOnLocationAnimation(
                zooMapIcon.width / 2,
                zooMapIcon.height / 2,
                minScaleFactor,
                OPEN_DOORS_ANIMATION_DURATION);

    }

    public float getmPosX() { return mPosX; }
    public float getmPosY() { return mPosY; }
    public float getmScaleFactor() { return mScaleFactor; }
    public boolean isAnimationInterrupt() { return animationInterrupt; }
    public void setAnimationInterrupted(boolean animationInterrupted) { this.animationInterrupt = animationInterrupted; }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = Math.min(
                    Math.max(
                            mScaleFactor * detector.getScaleFactor(),
                            minScaleFactor),
                    maxScaleFactor
            );
            return true;
        }
    }

    private void addEnclosure(Enclosure enclosure, int encIndex, Runnable onEnclosureClick, long
            delayToClick) {
        enclosureIconsHandlers.add(new EnclosureIconsHandler(this, enclosure, timer,
                timerFastRunnables, timerSlowRunnables, encIndex,
                () -> {
                    onEnclosureClick.run();
                    InitiateExitMapAnimation();
                },
                delayToClick));
    }

    private void addMiscIcon(Misc misc) {
        miscIcons.add(new MiscIcon(this, misc.getMarkerBitmap(), misc.getMarkerX(), misc
                .getMarkerY()));
    }

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

                setMapImageMargin(maxScaleFactor);
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
        setMapImageMargin(mScaleFactor);
        focusOnLocationAnimation(icon.left, icon.top, maxScaleFactor,
                FOCUS_ON_LOCATION_ANIMATION_TIME);
        Icon finalIcon = icon;
        new Handler().postDelayed(() -> {
            rattleViewAnimation(finalIcon.view, numberOfRattleTimes);
        }, FOCUS_ON_LOCATION_ANIMATION_TIME - numberOfRattleTimes * RATTLE_ANIMATION_HALF_TIME);
    }

    private Animation focusOnLocationAnimation(int focusX, int focusY, float finalScale, long duration) {
        float initialMScaleFactor = mScaleFactor;
        float initialMPosX = mPosX;
        float initialMPosY = mPosY;

        setAnimationInterrupted(false);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (isAnimationInterrupt())
                    this.cancel();

                mScaleFactor = mLastScaleFactor = initialMScaleFactor + (finalScale -
                        initialMScaleFactor) * interpolatedTime;
                animateMPos(interpolatedTime, initialMPosX, focusX, finalScale, initialMPosY, focusY);
                updateViewsPosition();
            }
        };
        a.setDuration(duration);
        startAnimation(a);
        return a;
    }

    /**
     *
     * @param animationFraction Must be between 0 and 1
     * @param initialMPosX
     * @param focusX
     * @param finalScale
     * @param initialMPosY
     * @param focusY
     */
    private void animateMPos(float animationFraction, float initialMPosX, int focusX, float finalScale,
                             float initialMPosY, int focusY) {
        mPosX =
                Math.max(
                        Math.min(
                                initialMPosX + (screenWidth / 2 - focusX * finalScale -
                                        initialMPosX) * animationFraction,
                                mScaleFactor * (zooMapIcon.left - zooMapIcon.width / 2 +
                                        mapImageMargin)
                        ),
                        screenWidth - mScaleFactor * (zooMapIcon.width + mapImageMargin)
                );
        mPosY =
                Math.max(
                        Math.min(
                                initialMPosY + (screenHeight / 2 - focusY * finalScale -
                                        initialMPosY) * animationFraction,
                                mScaleFactor * (zooMapIcon.top - zooMapIcon.height / 2 +
                                        mapImageMargin)
                        ),
                        screenHeight - mScaleFactor * (zooMapIcon.height +
                                mapImageMargin)
                );
    }

    private Animation focusOnLocationAnimation(int x, int y) {
        return focusOnLocationAnimation(x, y, maxScaleFactor,
                (long) (Math.sqrt((mPosX - x) * (mPosX - x) + (mPosY - y) * (mPosY - y))
                        / mScaleFactor * equatorScaleFactor));
    }

    /**
     *
     * @param view
     * @param times How many more times this animation need to happen
     */
    private void rattleViewAnimation(View view, int times) {
        Animation expandingAnimation = new ScaleAnimation(
                1f, 1.1f, 1f, 1.1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        expandingAnimation.setDuration(RATTLE_ANIMATION_HALF_TIME);
        view.startAnimation(expandingAnimation);

        Animation shrinkingAnimation = new ScaleAnimation(
                1.1f, 1f, 1.1f, 1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        shrinkingAnimation.setDuration(RATTLE_ANIMATION_HALF_TIME);
        new Handler().postDelayed(() -> { view.startAnimation(shrinkingAnimation); }, RATTLE_ANIMATION_HALF_TIME);
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