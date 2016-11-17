package com.greencode.enticement_android.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by jasonnguyen on 11/16/16.
 * Credits goes to InstaMaterial
 */

public class RevealBackground extends View {
    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_FILL_STARTED = 1;
    public static final int STATE_FINISHED = 2;

    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static final int FILL_TIME = 400;

    private int currentState = STATE_NOT_STARTED;

    private Paint mPainter;
    private int currentRadius;
    ObjectAnimator revealAnimator;

    private int startLocationX;
    private int startLocationY;


    private OnStateChangeListener onStateChangeListener;

    public RevealBackground(Context context) {
        super(context);
        initBackground();
    }

    public RevealBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public RevealBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RevealBackground(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBackground();
    }

    private void initBackground() {
        mPainter = new Paint();
        mPainter.setStyle(Paint.Style.FILL);
        mPainter.setColor(Color.WHITE);
    }

    public void setFillPaintColor(int color) {
        mPainter.setColor(color);
    }

    public void startFromLocation(int[] tapLocationOnScreen) {
        changeState(STATE_FILL_STARTED);
        startLocationX = tapLocationOnScreen[0];
        startLocationY = tapLocationOnScreen[1];
        revealAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0, getWidth() + getHeight()).setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        revealAnimator.start();
    }

    public void setToFinishedFrame() {
        changeState(STATE_FINISHED);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentState == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPainter);
        } else {
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, mPainter);
        }
    }

    private void changeState(int state) {
        if (this.currentState == state) {
            return;
        }

        this.currentState = state;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(state);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setCurrentRadius(int radius) {
        this.currentRadius = radius;
        invalidate();
    }

    public static interface OnStateChangeListener {
        void onStateChange(int state);
    }
}
