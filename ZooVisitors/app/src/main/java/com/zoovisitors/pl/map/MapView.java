package com.zoovisitors.pl.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Enclosure;
import com.zoovisitors.backend.Misc;
import com.zoovisitors.pl.enclosures.EnclosureActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aviv on 12-Jan-18.
 */

public class MapView extends RelativeLayout {
    private static final int INVALID_POINTER_ID = -1;
    public static final int SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;
    public static final int THREE_DAYS = 3 * 24 * 60 * 60 * 1000;
    public static final float ALPHA_CHANGE = 0.1f;
    public static final float MINIMAL_IMAGE_ALPHA = 0.2f;
    public static final float MAXIMAL_IMAGE_ALPHA = 1f;
    private static long RECURRING_EVENT_TIMER_ELAPSE_TIME = 30*60*1000;
    private static long RECURRING_EVENT_TEXT_TIMER_BETWEEN_CALLS_TIME = 250;
    private static long RECURRING_EVENT_IMAGE_TIMER_BETWEEN_CALLS_TIME = 50;
    private static enum RecurringEventTimerElapseTimeTextLengthEnum {HOURS, MINUTES, SECONDS};
    private static RecurringEventTimerElapseTimeTextLengthEnum
            RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT =
            RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.SECONDS :
                    RECURRING_EVENT_TIMER_ELAPSE_TIME < 60 * 60 * 1000 ? RecurringEventTimerElapseTimeTextLengthEnum.MINUTES :
                            RecurringEventTimerElapseTimeTextLengthEnum.HOURS;

    private static String getSeconds(long time) {
        return String.format("%02d",(int) (time*0.001%60));
    }
    private static String getMinutes(long time) {
        return String.format("%02d",(int) (time*0.001/60%60));
    }
    private static String getHours(long time) {
        return String.format("%02d",(int) Math.min(time*0.001/60/60, 99));
    }
    public static final String ZOO_MAP = "zoo_map";
    public static final String VISITOR_ICON = "visitor_icon";
    public static final String RECURRING_EVENT_STARTED_ICON = "recurring_event_started_icon";
    private static final int TEXT_VIEW_TEXT_SIZE = 10;

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
        enclosureIcons = new ArrayList<>();
        miscIcons = new ArrayList<>();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(MotionEvent.ACTION_DOWN == ev.getAction()){
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

                updateIconPositionWithoutSize(zooMapIcon);
                for (EnclosureIcon enclosureIcon :
                        enclosureIcons) {
                    updateIconPositionWithSize(enclosureIcon);
                    updateIconPositionWithSize(enclosureIcon.recurringEvent.recurringEventImageViewIcon);
                    updateIconPositionWithSize(enclosureIcon.recurringEvent.recurringEventTextViewIcon);
                }
                for(MiscIcon miscIcon:
                        miscIcons) {
                    updateIconPositionWithSize(miscIcon);
                }
                if(visitorIcon.view.getVisibility() == VISIBLE)
                    updateIconPositionWithSize(visitorIcon);
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

    /**
     * Updates the icon position so the middle of the image will be at the point
     * @param icon
     */
    private void updateIconPositionWithSize(ImageIcon icon) {
        if(icon.width != 0 && icon.height != 0) {
            LayoutParams params =
                    new LayoutParams((int) (icon.width * mScaleFactor), (int) (icon.height * mScaleFactor));

            params.setMargins(
                    (int) ((icon.left - icon.width/2) * mScaleFactor + mPosX),
                    (int) ((icon.top - icon.height/2) * mScaleFactor + mPosY),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            icon.view.setLayoutParams(params);
        }
    }

    private void updateIconPositionWithSize(TextIcon icon) {
        if(icon.width != 0 && icon.height != 0) {
            LayoutParams params =
                    new LayoutParams((int) (icon.width * mScaleFactor), (int) (icon.height * mScaleFactor));
            ((ViewGroup.MarginLayoutParams) icon.textView.getLayoutParams()).setMargins(
                    (int) ((icon.left - icon.width/2) * mScaleFactor + mPosX),
                    (int) ((icon.top - icon.height/2) * mScaleFactor + mPosY),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            icon.textView.setTextSize(TEXT_VIEW_TEXT_SIZE * mScaleFactor);
        }
    }

    /**
     * Updates the icon position so the left top of the image will be at the point
     * @param icon
     */
    private void updateIconPositionWithoutSize(ImageIcon icon) {
        if(icon.width != 0 && icon.height != 0) {
            LayoutParams params =
                    new LayoutParams((int) (icon.width * mScaleFactor), (int) (icon.height * mScaleFactor));

            params.setMargins(
                    (int) (icon.left * mScaleFactor + mPosX),
                    (int) (icon.top * mScaleFactor + mPosY),
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            icon.view.setLayoutParams(params);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            return true;
        }
    }

    private List<EnclosureIcon> enclosureIcons;
    private List<MiscIcon> miscIcons;
    private ImageIcon visitorIcon;
    private ImageIcon zooMapIcon;
    public void addEnclosureIcon(Drawable resource, Enclosure enclosure, int left, int top)
    {
        enclosureIcons.add(new EnclosureIcon(resource, enclosure, left, top));
    }
    public void addMiscIcon(Drawable resource, Misc misc, int left, int top)
    {
        miscIcons.add(new MiscIcon(resource, misc, left, top));
    }
    public void AddVisitorIcon()
    {
        visitorIcon = new ImageIcon(null, 0, 0, true, false) {
            @Override
            void setView() {
                ImageView view = new ImageView(getContext());
                int resourceId = getResources().getIdentifier(VISITOR_ICON, "mipmap", getContext().getPackageName());
                view.setImageResource(resourceId);
                this.view = view;
            }
        };
    }
    public void UpdateVisitorLocation(int left, int top)
    {
        visitorIcon.left = left;
        visitorIcon.top = top;
        updateIconPositionWithSize(visitorIcon);
    }
    public void ShowVisitorIcon()
    {
        visitorIcon.view.setVisibility(VISIBLE);
    }
    public void HideVisitorIcon()
    {
        visitorIcon.view.setVisibility(INVISIBLE);
    }
    public void addZooMapIcon(int left, int top)
    {
        zooMapIcon = new ImageIcon(null, left, top, false, true) {
            @Override
            void setView() {
                ImageView view = new ImageView(getContext());
                int resourceId = getResources().getIdentifier(ZOO_MAP, "mipmap", getContext().getPackageName());
                view.setImageResource(resourceId);
                this.view = view;
            }
        };
    }

    private abstract class Icon {
        protected Object[] additionalData;
        protected int left;
        protected int top;
        protected int width;
        protected int height;

        abstract void setView();
//        abstract void UpdateView(boolean shouldBeCentered, boolean isVisible);

        Icon(Object[] additionalData, int left, int top) {
            this.additionalData = additionalData;
            this.left = left;
            this.top = top;
            setView();
        }
    }

    private abstract class ImageIcon extends Icon {
        protected View view;

        ImageIcon(Object[] additionalData, int left, int top, boolean shouldBeCentered, boolean isVisible) {
            super(additionalData, left, top);
            UpdateView(shouldBeCentered, isVisible);
        }

        public void UpdateView(boolean shouldBeCentered, boolean isVisible) {

            view.setBackgroundColor(Color.TRANSPARENT);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            view.setLayoutParams(layoutParams);

            view.post(new Runnable() {
                @Override
                public void run() {
                    width =  view.getMeasuredWidth();
                    height = view.getMeasuredHeight();
                    if(shouldBeCentered)
                        updateIconPositionWithSize(ImageIcon.this);
                }
            });
            view.setVisibility(isVisible ? VISIBLE : INVISIBLE);
            addView(view);
        }
    }

    private class MiscIcon extends ImageIcon {
        private Misc misc;
        MiscIcon(Drawable resource, Misc misc, int left, int top) {
            super(new Object[]{resource}, left, top, true, true);
            this.misc = misc;
        }

        @Override
        void setView() {
            ImageView view = new ImageView(getContext());
            view.setImageDrawable((Drawable) additionalData[0]);
            this.view = view;
        }
    }

    private class EnclosureIcon extends ImageIcon {
        private Enclosure enclosure;
        private RecurringEventScheduler recurringEvent;

        EnclosureIcon(Drawable resource, Enclosure enclosure, int left, int top) {
            super(new Object[] {resource}, left, top, true, true);
            this.enclosure = enclosure;

            OnTouchListener onTouchListener = new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_UP:
                            Intent intent = new Intent(GlobalVariables.appCompatActivity, EnclosureActivity.class);
                            Bundle clickedEnclosure = new Bundle();

                            clickedEnclosure.putSerializable("enc", enclosure);
                            intent.putExtras(clickedEnclosure); //Put your id to your next Intent
                            GlobalVariables.appCompatActivity.startActivity(intent);

                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                    }

                    return true;
                }
            };

            view.setOnTouchListener(onTouchListener);
            // should be ran after the view was added to front and the sizes are known, cool trick..
            view.post(new Runnable() {
                @Override
                public void run() {
                    recurringEvent = new RecurringEventScheduler(
                            left,
                            (int) (top - height),
                            onTouchListener,
                            enclosure.getRecurringEvents()
                            );
                }
            });
        }

        @Override
        void setView() {
            ImageView view = new ImageView(getContext());
            view.setImageDrawable((Drawable) additionalData[0]);
            this.view = view;
        }
    }

    private class RecurringEventScheduler {
        private Timer textTimer, imageTimer;
        private TextIcon recurringEventTextViewIcon;
        private RecurringEventImageViewIcon recurringEventImageViewIcon;
        private Enclosure.RecurringEvent[] recurringEvents;

        public RecurringEventScheduler(int left, int top, OnTouchListener onTouchListener, Enclosure.RecurringEvent[] recurringEvents) {
            imageTimer = new Timer();
            this.recurringEvents = recurringEvents;
            recurringEventTextViewIcon = new TextIcon(
                    onTouchListener,
                    left,
                    top);
            recurringEventImageViewIcon = new RecurringEventImageViewIcon(
                    onTouchListener,
                    left,
                    top);
            startTimers();
        }

        public void startTimers() {
            long currentTime = getTimeAdjustedToWeekTime();
            if(recurringEvents.length == 0)
                return;
            Enclosure.RecurringEvent recurringEvent = getUpcomingRecurringEvent();
            // means the text should not be shown yet
            if (recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME > currentTime) {
                scheduleTextTimer(recurringEvent.getStartTime(), recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME - currentTime);
            } else if (recurringEvent.getStartTime() > currentTime) { // means we should be at the middle of the text timer ticking
                scheduleTextTimer(recurringEvent.getStartTime(), 0);
            } else if (currentTime >= recurringEvent.getEndTime()) { // means we are after the event time, we delay the time until the week will pass and to the starting minus Wait interval
                scheduleTextTimer(recurringEvent.getStartTime(), recurringEvent.getStartTime() - RECURRING_EVENT_TIMER_ELAPSE_TIME + SEVEN_DAYS - currentTime);
            }

            // means that we shouldn't show the image yet
            if (recurringEvent.getStartTime() > currentTime) {
                scheduleImageTimer(recurringEvent.getStartTime(), recurringEvent.getEndTime(), recurringEvent.getStartTime() - currentTime);
            } else if (recurringEvent.getEndTime() > currentTime && currentTime >= recurringEvent.getStartTime()) { // means the event is on
                scheduleImageTimer(recurringEvent.getStartTime(), recurringEvent.getEndTime(), 0);
            } else { // really low percent it will happen, only if between the time it was called and until it started, the event was finished.
                scheduleImageTimer(recurringEvent.getStartTime(), recurringEvent.getEndTime(), recurringEvent.getStartTime() + SEVEN_DAYS - currentTime);
            }
        }

        private void scheduleImageTimer(long startTime, long endTime, long delayTime) {
            imageTimer = new Timer();
            imageTimer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            long currentTime = getTimeAdjustedToWeekTime();
                            if(currentTime < endTime){
                                recurringEventImageViewIcon.updateOpacity();
                            } else {
                                recurringEventImageViewIcon.hide();
                                imageTimer.cancel();
                                startTimers();
                            }
                        }
                    },
                    delayTime,
                    RECURRING_EVENT_IMAGE_TIMER_BETWEEN_CALLS_TIME);
        }

        private void scheduleTextTimer(long startTime, long delayTime) {
            textTimer = new Timer();
            textTimer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {
                            long currentTime = getTimeAdjustedToWeekTime();
                            if(currentTime < startTime){
                                recurringEventTextViewIcon.setTime(startTime - currentTime);
                            } else {
                                recurringEventTextViewIcon.hide();
                                textTimer.cancel();
                            }
                        }
                    },
                    delayTime,
                    RECURRING_EVENT_TEXT_TIMER_BETWEEN_CALLS_TIME);
        }

        private long getTimeAdjustedToWeekTime() {
            return (Calendar.getInstance().getTimeInMillis() + SEVEN_DAYS - THREE_DAYS) % SEVEN_DAYS;
        }

        private int lastRecurringEventIndex = -1;
        private Enclosure.RecurringEvent getUpcomingRecurringEvent() {
            long currentTime = getTimeAdjustedToWeekTime();
            if(lastRecurringEventIndex == -1) {
                if(recurringEvents[recurringEvents.length - 1].getEndTime() <= currentTime ||
                        currentTime < recurringEvents[0].getEndTime()) {
                    lastRecurringEventIndex = 0;
                    return recurringEvents[0];
                }
                // fast search
                int top = recurringEvents.length, bottom = 0, mid;
                lastRecurringEventIndex = (top + bottom) / 2;
                while(top - bottom > 1){
                    mid = (top+bottom)/2;
                    if(recurringEvents[mid].getEndTime() <= currentTime) {
                        bottom = mid;
                    } else {
                        top = mid;
                    }
                }
                lastRecurringEventIndex = top;
                return recurringEvents[lastRecurringEventIndex];
            } else {
                lastRecurringEventIndex = (++lastRecurringEventIndex) % recurringEvents.length;
                Log.e("AVIV", "lastRecurringEventIndex " + lastRecurringEventIndex);
                return recurringEvents[lastRecurringEventIndex];
            }
        }
    }

    //RecurringEventTextViewIcon
    private class TextIcon extends Icon {
        protected TextView textView;

        TextIcon(OnTouchListener onTouchListener, int left, int top) {
            super(new Object[] {onTouchListener}, left, top);
        }

        @Override
        void setView() {
            textView = new TextView(getContext());
            textView.setTextSize(TEXT_VIEW_TEXT_SIZE);
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(ContextCompat.getColor(getContext(), R.color.orangeNegev));
            gd.setCornerRadius(5);
            gd.setStroke(5, ContextCompat.getColor(getContext(), R.color.orangeNegev));
            textView.setOnTouchListener((OnTouchListener) additionalData[0]);

            switch(RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT) {
                case HOURS:
                    textView.setText("00:00:00");
                    break;
                case MINUTES:
                    textView.setText("00:00");
                    break;
                case SECONDS:
                    textView.setText("00");
                    break;
            }

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(left, top, Integer.MAX_VALUE, Integer.MAX_VALUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            textView.setLayoutParams(layoutParams);
            textView.setVisibility(INVISIBLE);
            addView(textView);

            textView.post(() -> {
                textView.setBackground(gd);
                textView.setPadding(5,5,5,5);
                width =  textView.getMeasuredWidth();
                height = textView.getMeasuredHeight();
                updateIconPositionWithSize(TextIcon.this);
            });
        }

        /**
         *
         * @param time The time that needs to be shown (after all calculations except dividing to hours, minutes and seconds)
         */
        public void setTime(long time) {
            StringBuilder text = new StringBuilder();
            switch(RECURRING_EVENT_TIMER_ELAPSE_TIME_TEXT) {
                case HOURS:
                    text.append(getHours(time));
                    text.append(":");
                case MINUTES:
                    text.append(getMinutes(time));
                    text.append(":");
                case SECONDS:
                    text.append(getSeconds(time));
                    break;
            }
            textView.post(() -> {
                textView.setText(text.toString());
                textView.setVisibility(VISIBLE);
            });
        }
        public void hide()
        {
            textView.post(() -> {
                textView.setVisibility(INVISIBLE);
            });
        }
    }

    private class RecurringEventImageViewIcon extends ImageIcon {
        RecurringEventImageViewIcon(OnTouchListener onTouchListener, int left, int top) {
            super(new Object[] {onTouchListener}, left, top, true, false);
        }

        @Override
        void setView() {
            ImageView view = new ImageView(getContext());
            int resourceId = getResources().getIdentifier(RECURRING_EVENT_STARTED_ICON, "mipmap", getContext().getPackageName());
            view.setImageResource(resourceId);
            view.setOnTouchListener((OnTouchListener) additionalData[0]);
            this.view = view;
        }

        float lastAlpha = MAXIMAL_IMAGE_ALPHA;
        boolean directionIsUp = false;
        public void updateOpacity()
        {

            if(lastAlpha <= MINIMAL_IMAGE_ALPHA)
                directionIsUp = true;
            else if(lastAlpha >= MAXIMAL_IMAGE_ALPHA)
                directionIsUp = false;

            lastAlpha += directionIsUp ? ALPHA_CHANGE : -ALPHA_CHANGE;
            view.post(() -> {
                view.setVisibility(VISIBLE);
                view.setAlpha(lastAlpha);
            });
        }

        public void hide() {
            view.post(() -> {
                view.setVisibility(INVISIBLE);
            });
        }
    }
}