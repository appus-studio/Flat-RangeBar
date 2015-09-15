package com.appus.rangebar.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.AccelerateInterpolator;

import com.appus.rangebar.RangeBar;

/**
 * Created by igor.malytsky on 9/2/15.
 */
public final class Thumb {
    private Paint mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final long ANIMATION_DURATION = 200;

    private Pin mPin;

    private int mThumbColor;

    private float mX;
    private float mY;

    private float mExpandedPinRadius;
    private float mCurrentPinRadius;

    private int mPinTextColor;

    private float mSideBarOffset;
    private float mTopBarOffset;

    private boolean isPressed;

    private float mTickRadius;

    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public Thumb(Context context,
                 float pinWidth, int pinColor, int pinTextColor,
                 int thumbColor,
                 float sideBarOffset, float topBarOffset,
                 float tickRadius,
                 float y) {

        this.mSideBarOffset = sideBarOffset;
        this.mTopBarOffset = topBarOffset;

        this.mPinTextColor = pinTextColor;

        this.mExpandedPinRadius = pinWidth / 2;

        this.mThumbColor = thumbColor;

        this.mTickRadius = tickRadius;

        this.mY = y;

        mPin = new Pin(context);
        mPin.init(0, pinColor, mTickRadius, mPinTextColor, sideBarOffset);
        mPin.setY(mExpandedPinRadius);

        mThumbPaint.setColor(mThumbColor);
        mThumbPaint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(mX + mTickRadius, mY, mTickRadius, mThumbPaint);
        mPin.draw(canvas);
    }

    public void showPin(RangeBar rangeBar) {
        ValueAnimator fadeInAnimation = getFadeInAnimation(rangeBar, ANIMATION_DURATION);

        mAnimatorSet.play(fadeInAnimation);
        mAnimatorSet.start();
    }

    public void hidePin(RangeBar rangeBar) {
        if (mCurrentPinRadius != 0) {
            ValueAnimator fadeOutAnimation = getFadeOutAnimation(rangeBar, ANIMATION_DURATION);

            mAnimatorSet.play(fadeOutAnimation);
            mAnimatorSet.start();
        }
    }


    public void showPinOnFastMove(RangeBar rangeBar) {
        ValueAnimator fadeOutAnimation = getFadeOutAnimation(rangeBar, ANIMATION_DURATION);

        mAnimatorSet.play(fadeOutAnimation);
        mAnimatorSet.start();
    }

    private ValueAnimator getFadeInAnimation(final RangeBar rangeBar, long duration) {
        ValueAnimator fadeInAnimation = ValueAnimator.ofFloat(0, mExpandedPinRadius);
        fadeInAnimation.setInterpolator(new AccelerateInterpolator());
        fadeInAnimation.setDuration(duration);
        fadeInAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPinRadius = (Float) (animation.getAnimatedValue());
                mPin.setPinWidth(mCurrentPinRadius);
                rangeBar.invalidate();
            }
        });

        return fadeInAnimation;
    }

    private ValueAnimator getFadeOutAnimation(final RangeBar rangeBar, long duration) {
        ValueAnimator fadeOutAnimation = ValueAnimator.ofFloat(mExpandedPinRadius, 0);
        fadeOutAnimation.setInterpolator(new AccelerateInterpolator());
        fadeOutAnimation.setDuration(duration);
        fadeOutAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPinRadius = (Float) (animation.getAnimatedValue());
                mPin.setPinWidth(mCurrentPinRadius);
                rangeBar.invalidate();
            }
        });

        return fadeOutAnimation;
    }

    public boolean isInTargetZone(float connectingLineWidth, float x, float y) {
        return (Math.abs(x - mX) < connectingLineWidth / 2
                && Math.abs(y - mY) <= mExpandedPinRadius * 3);
    }


    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
        mPin.setX(x);
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getY() {
        return mY;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPinValue(String value) {
        mPin.setPinValue(value);
    }

    public String getPinValue() {
        return mPin.getPinValue();
    }

    /**
     * @param radius Tick's radius should be equal to the thumb's radius
     */
    public void setThumbRadius(float radius) {
        this.mTickRadius = radius;
        mPin.setTickRadius(mTickRadius);
    }

    public float getThumbRadius() {
        return mTickRadius;
    }

    public void setPinColor(int color) {
        mPin.setPinColor(color);
    }

    public int getPinColor() {
        return mPin.getPinColor();
    }

    public void setPinTextColor(int color) {
        mPinTextColor = color;
        mPin.setPinTextColor(mPinTextColor);
    }

    public int getPinTextColor() {
        return mPin.getPinTextColor();
    }

    public void setPinWidth(float width) {
        this.mExpandedPinRadius = width / 2;
        mPin.setY(mExpandedPinRadius);
    }

    public void setThumbColor(int color) {
        mThumbColor = color;
        mThumbPaint.setColor(mThumbColor);
    }

    public int getThumbColor() {
        return mThumbColor;
    }
}
