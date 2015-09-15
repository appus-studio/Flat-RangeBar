package com.appus.rangebar.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.appus.rangebar.common.Constants;

/**
 * Created by igor.malytsky on 9/2/15.
 */
public final class Bar {
    private final Paint mBarPaint;
    private final Paint mTickPaint;

    private Canvas mLastCanvas;

    private float mTickRadius = Constants.DEFAULT_TICK_RADIUS;
    private int mTickColor = Constants.DEFAULT_TICK_COLOR;
    private int mSelectedTickColor = Constants.DEFAULT_TICK_COLOR;

    private int mTickStartValue= Constants.DEFAULT_TICK_START_VALUE;
    private int mTickEndValue= Constants.DEFAULT_TICK_END_VALUE;
    private float mTickInterval = Constants.DEFAULT_TICK_INTERVAL;

    private float mSideBarOffset = 0;
    private float mTopBarOffset = 0;

    private float mStartX;
    private float mEndX;
    private float mY;

    private float mLeftThumbCoordinate = 0;
    private float mRightThumbCoordinate = 0;

    private float mBarStrokeWidth = Constants.DEFAULT_BAR_STROKE_WIDTH;
    private int mBarColor= Constants.DEFAULT_BAR_COLOR;

    /**
     * Bar is a line on which all views (ticks, thumbs, pins) are drawn.
     *
     * @param tickRadius        Radius of the tick which is used for marking segments
     * @param tickStartValue    Value which will be displayed for first tick
     * @param tickEndValue      Value which will be displayed for last tick
     * @param tickInterval      Interval between displayed ticks' values
     * @param selectedTickColor Color of the tick in selected interval
     * @param barStrokeWidth    Thickness of the bar
     * @param barColor          Color of the bar
     * @param sideBarOffset     Left/right offset of the bar
     * @param topBarOffset      Top offset of the bar
     */
    public Bar(float tickRadius, int tickStartValue, int tickEndValue, float tickInterval, int tickColor, int selectedTickColor,
               float barStrokeWidth, int barColor,
               float sideBarOffset, float topBarOffset) {

        this.mTickRadius = tickRadius;
        this.mTickStartValue = tickStartValue;
        this.mTickEndValue = tickEndValue;
        this.mTickInterval = tickInterval;

        this.mBarStrokeWidth = barStrokeWidth;
        this.mBarColor = barColor;

        this.mSideBarOffset = sideBarOffset;
        this.mTopBarOffset = topBarOffset;

        this.mTickColor = tickColor;
        this.mSelectedTickColor = selectedTickColor;

        mBarPaint = new Paint();
        mBarPaint.setColor(barColor);
        mBarPaint.setStrokeWidth(barStrokeWidth);
        mBarPaint.setAntiAlias(true);

        mTickPaint = new Paint();
        mTickPaint.setColor(tickColor);
        mTickPaint.setAntiAlias(true);
    }

    /**
     * @param canvas Canvas where your bar should be drawn
     */
    public void drawBar(Canvas canvas) {
        mLastCanvas = canvas;

        mStartX = getBarStartX();
        mEndX = getBarEndX() + mTickRadius;
        mY = mTopBarOffset;

        mBarPaint.setColor(mBarColor);

        canvas.drawLine(mStartX, mY, mEndX, mY, mBarPaint);

        drawTicks(canvas);
    }

    /**
     * @return X coordinate of bar start
     */
    public float getBarStartX() {
        return mSideBarOffset;
    }

    /**
     * @return X coordinate of bar end
     */
    public float getBarEndX() {
        return mLastCanvas.getWidth() - mSideBarOffset - mTickRadius;
    }

    /**
     * @return Width of the bar in "px"
     */
    public float getBarWidth() {
        int canvasWidth = mLastCanvas!=null ? mLastCanvas.getWidth() : 3;

        float leftAndRightOffset = mSideBarOffset * 2;
        return canvasWidth - leftAndRightOffset;
    }

    /**
     * @return Width of the segment in "px"
     */
    public float getSegmentWidth() {
        return getBarWidth() / getSegmentsAmount(mTickStartValue, mTickEndValue);
    }

    /**
     * This method is responsible for drawing ticks on your bar.
     *
     * @param canvas Canvas where your bar was drawn
     */
    private void drawTicks(Canvas canvas) {

        /**
         * We ignore drawing of first and last tick to draw it separately for avoiding cropping of ticks
         * */
        float tickX = getSegmentWidth() + mSideBarOffset;
        for (int i = 1; i <= getSegmentsAmount(mTickStartValue, mTickEndValue); i++) {
            if (tickX - mTickRadius >= mLeftThumbCoordinate && tickX - mTickRadius <= mRightThumbCoordinate) {
                mTickPaint.setColor(mSelectedTickColor);
            } else {
                mTickPaint.setColor(mTickColor);
            }

            canvas.drawCircle(tickX, mY, mTickRadius, mTickPaint);
            tickX = getSegmentWidth() * i + mSideBarOffset;
        }

        /**
         * Drawing of first and last tick. Is used to avoid cropping of ticks
         * */
        mTickPaint.setColor(mTickColor);
        canvas.drawCircle(mTickRadius, mY, mTickRadius, mTickPaint);
        canvas.drawCircle(getBarWidth() + mTickRadius, mY, mTickRadius, mTickPaint);
    }

    /**
     * @param thumb Thumb for which we should find the nearest tick index
     * @return Index of the nearest tick for thumb.
     */
    public int getNearestTickIndex(Thumb thumb) {
        return (int) ((thumb.getX() - getBarStartX() + getSegmentWidth() / 2f) / getSegmentWidth());
    }

    /**
     * @param x X coordinate of the thumb for which we should find the nearest tick index
     * @return Index of the nearest tick for thumb.
     */
    public int getNearestTickIndex(float x) {
        return (int) ((x - getBarStartX() + getSegmentWidth() / 2f) / getSegmentWidth());
    }

    /**
     * @param thumb Thumb for which we should find the nearest tick coordinate
     * @return X coordinate of the nearest tick for thumb.
     */
    public float getNearestTickCoordinate(Thumb thumb) {
        final int nearestTickIndex = getNearestTickIndex(thumb);
        return getBarStartX() + (nearestTickIndex * getSegmentWidth()) - mTickRadius;
    }

    /**
     * @param index Index for which we should find the coordinate
     * @return X coordinate of the index
     */
    public float getTickCoordinate(int index) {
        return getBarStartX() + (index * getSegmentWidth()) - mTickRadius;
    }

    /**
     * @param index Index for which we should find the value
     * @return Value for the index
     */
    public int getTickValue(int index) {
        return (int) (index * mTickInterval + mTickStartValue);
    }

    /**
     * @param thumb Thumb for which we should find the nearest value
     * @return Nearest value for the thumb
     */
    public int getNearestTickValue(Thumb thumb) {
        return (int) (getNearestTickIndex(thumb) * mTickInterval + mTickStartValue);
    }

    public void setLeftThumbX(float x) {
        this.mLeftThumbCoordinate = x;
    }

    public void setRightThumbX(float x) {
        this.mRightThumbCoordinate = x;
    }

    /**
     * This method calculates amount of the segments for bar with specified startValue, endValue and interval.
     *
     * @param startValue Min. displayed value of the bar
     * @param endValue   Max. displayed value of the bar
     * @return Amount of segments
     */
    public int getSegmentsAmount(float startValue, float endValue) {
        return (int) ((endValue - startValue) / mTickInterval);
    }

    public void setTickRadius(float radius) {
        this.mTickRadius = radius;
    }

    public float getTickRadius() {
        return mTickRadius;
    }

    public void setTickInterval(float interval) {
        this.mTickInterval = interval;
    }

    public float getTickInterval() {
        return mTickInterval;
    }

    public void setTickStartValue(int startValue) {
        this.mTickStartValue = startValue;
    }

    public int getTickStartValue() {
        return mTickStartValue;
    }

    public void setTickEndValue(int endValue) {
        this.mTickEndValue = endValue;
    }

    public int getTickEndValue() {
        return mTickEndValue;
    }

    public void setTickColor(int color) {
        this.mTickColor = color;
    }

    public int getTickColor() {
        return mTickColor;
    }

    public void setBarStrokeWidth(float width) {
        this.mBarStrokeWidth = width;
        mBarPaint.setStrokeWidth(mBarStrokeWidth);
    }

    public float getBarStrokeWidth() {
        return mBarStrokeWidth;
    }

    public void setBarColor(int color) {
        this.mBarColor = color;
        mBarPaint.setColor(mBarColor);
    }

    public int getBarColor() {
        return mBarColor;
    }

    public void setSelectedTickColor(int color) {
        this.mSelectedTickColor = color;
    }

    public int getSelectedTickColor() {
        return mSelectedTickColor;
    }
}
