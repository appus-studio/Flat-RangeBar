package com.appus.rangebar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by igor.malytsky on 9/3/15.
 */
public final class Pin extends View {
    private static final int MIN_TEXT_SIZE = 0;
    private static final int MAX_TEXT_SIZE = 20;

    private final Paint mPinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPinTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mPinWidth;
    private int mPinColor;
    private float mPinTextSize;
    private int mPinTextColor;

    private float mTickRadius;

    private Rect mBounds = new Rect();

    private String mPinValue = "";

    private float mX;
    private float mY;

    public Pin(Context context) {
        super(context);
    }

    /**
     * The view is created empty. Use init to set all initial variables.
     * */
    public void init(float pinWidth, int pinColor, float tickRadius, int pinTextColor) {
        this.mPinColor = pinColor;
        this.mPinWidth = pinWidth;

        this.mTickRadius = tickRadius;

        this.mPinTextColor = pinTextColor;

        mPinPaint.setColor(mPinColor);
        mPinPaint.setStyle(Paint.Style.FILL);

        mPinTextSize = mPinWidth / 4;
        mPinTextPaint.setColor(pinTextColor);
        mPinTextPaint.setTextSize(mPinTextSize);
        mPinTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        switch (measureWidthMode) {
            default:
                width = Math.min((int) mPinWidth, measureWidth);
                break;
        }

        switch (measureHeightMode) {
            default:
                height = Math.min((int) mPinWidth, measureHeight);
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float pinRadius =  mPinWidth / 2;

        mPinTextPaint.getTextBounds(mPinValue, 0, mPinValue.length(), mBounds);


        // getting of text's half height for placing it in center of pin
        float halfTextHeight = mY + mBounds.height() / 2;

        canvas.drawCircle(mX + mTickRadius, mY, pinRadius, mPinPaint);
        canvas.drawText(mPinValue, mX + mTickRadius, halfTextHeight, mPinTextPaint);
    }

    /**
     * Responsible for resizing of text depending on its length and pin's width
     *
     * @param paint paint which is used for drawing of text
     * @param text text, which should be displayed on pin
     * @param min min text size
     * @param max max text size
     * @param boxWidth width of rectangle for text
     */
    private void calibrateTextSize(Paint paint, String text, float min, float max, float boxWidth) {
        paint.setTextSize(Math.max(Math.min((boxWidth / paint.measureText(text)) * 10, max), min));
    }

    @Override
    public void setX(float x) {
        mX = x;
    }

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public void setY(float y) {
        mY = y;
    }

    @Override
    public float getY() {
        return mY;
    }

    public void setPinWidth(float pinWidth) {
        mBounds.set((int) (mX - pinWidth),
                (int) (mY - mPinWidth),
                (int) (mX + pinWidth),
                (int) mY);

        calibrateTextSize(mPinTextPaint, mPinValue, MIN_TEXT_SIZE, MAX_TEXT_SIZE, mBounds.width());

        this.mPinWidth = pinWidth;
    }

    public float getPinWidth() {
        return mPinWidth;
    }

    public void setPinColor(int pinColor) {
        this.mPinColor = pinColor;
        mPinPaint.setColor(pinColor);
    }

    public int getPinColor() {
        return mPinColor;
    }

    public void setPinValue(String pinValue) {
        this.mPinValue = pinValue;
    }

    public String getPinValue() {
        return mPinValue;
    }

    public void setTickRadius(float radius) {
        this.mTickRadius = radius;
    }

    public float getTickRadius() {
        return mTickRadius;
    }

    public void setPinTextColor(int color) {
        this.mPinTextColor = color;
        mPinTextPaint.setColor(mPinColor);
    }

    public int getPinTextColor() {
        return mPinTextColor;
    }
}
