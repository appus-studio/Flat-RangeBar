package com.appus.rangebar.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by igor.malytsky on 9/8/15.
 */
public final class ConnectingLine {
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mLeftX;
    private float mRightX;

    private float mY;

    private float mStrokeWidth;

    private int mLineColor;


    public ConnectingLine(int color, float strokeWidth, float y) {
        this.mY = y;

        this.mLineColor = color;

        this.mStrokeWidth = strokeWidth;

        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(strokeWidth);
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(mLeftX, mY, mRightX, mY, mLinePaint);
    }

    public void setLeftX(float leftX) {
        this.mLeftX = leftX;
    }

    public float getLeftX() {
        return mLeftX;
    }

    public void setRightX(float rightX) {
        this.mRightX = rightX;
    }

    public float getRightX() {
        return mRightX;
    }

    public float getLineWidth(float offset) {
        return mRightX - mLeftX - offset;
    }

    public void setStrokeWidth(float width) {
        this.mStrokeWidth = width;
        mLinePaint.setStrokeWidth(mStrokeWidth);
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setLineColor(int color) {
        this.mLineColor = color;

        mLinePaint.setColor(mLineColor);
    }

    public int getLineColor() {
        return mLineColor;
    }
}
