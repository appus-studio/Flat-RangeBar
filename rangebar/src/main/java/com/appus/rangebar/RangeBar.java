package com.appus.rangebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.appus.rangebar.common.Constants;
import com.appus.rangebar.view.Bar;
import com.appus.rangebar.view.ConnectingLine;
import com.appus.rangebar.view.Thumb;

/**
 * Created by igor.malytsky on 9/2/15.
 */
public final class RangeBar extends View {

    private Bar mBar;
    private Thumb mLeftThumb;
    private Thumb mRightThumb;
    private ConnectingLine mConnectingLine;

    private int mDefaultViewHeight = Constants.DEFAULT_VIEW_HEIGHT;
    private int mDefaultViewWidth = Constants.DEFAULT_VIEW_WIDTH;

    private int mTickStartValue = Constants.DEFAULT_TICK_START_VALUE;
    private int mTickEndValue = Constants.DEFAULT_TICK_END_VALUE;
    private float mTickInterval = Constants.DEFAULT_TICK_INTERVAL;
    private float mTickRadius = Constants.DEFAULT_TICK_RADIUS;
    private int mTickColor = Constants.DEFAULT_TICK_COLOR;

    private int mThumbColor = Constants.DEFAULT_THUMB_COLOR;

    private float mPinWidth = Constants.DEFAULT_PIN_WIDTH;
    private int mPinColor = Constants.DEFAULT_PIN_COLOR;

    private float mBarStrokeWidth = Constants.DEFAULT_BAR_STROKE_WIDTH;
    private int mBarColor = Constants.DEFAULT_BAR_COLOR;

    private float mConnectingLineStrokeWidth = Constants.DEFAULT_CONNECTING_LINE_STROKE_WIDTH;
    private int mConnectingLineColor = Constants.DEFAULT_CONNECTING_LINE_COLOR;

    private int mTextColor = Constants.DEFAULT_TEXT_COLOR;

    private float mLastX;
    private float mLastY;

    private int mRestoredLeftIndex = Constants.DEFAULT_LEFT_THUMB_INDEX;
    private int mRestoredRightIndex = Constants.DEFAULT_RIGHT_THUMB_INDEX;

    private float mDifferenceX;
    private float mDifferenceY;

    private OnRangeBarChangeListener mOnRangeBarChangeListener;

    private boolean isAfterRestore = false;
    private boolean isAfterResize = false;

    public RangeBar(Context context) {
        super(context);
    }

    public RangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RangeBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundleViewState = new Bundle();

        bundleViewState.putParcelable(Constants.BUNDLE_INSTANCE_STATE, super.onSaveInstanceState());

        bundleViewState.putInt(Constants.BUNDLE_TICK_START_VALUE, mTickStartValue);
        bundleViewState.putInt(Constants.BUNDLE_TICK_END_VALUE, mTickEndValue);
        bundleViewState.putFloat(Constants.BUNDLE_TICK_INTERVAL, mTickInterval);
        bundleViewState.putFloat(Constants.BUNDLE_TICK_RADIUS, mTickRadius);
        bundleViewState.putInt(Constants.BUNDLE_TICK_COLOR, mTickColor);

        bundleViewState.putInt(Constants.BUNDLE_THUMB_COLOR, mThumbColor);
        bundleViewState.putInt(Constants.BUNDLE_LEFT_THUMB_INDEX, mRestoredLeftIndex);
        bundleViewState.putInt(Constants.BUNDLE_RIGHT_THUMB_INDEX, mRestoredRightIndex);


        bundleViewState.putFloat(Constants.BUNDLE_PIN_WIDTH, mPinWidth);
        bundleViewState.putInt(Constants.BUNDLE_PIN_COLOR, mPinColor);

        bundleViewState.putFloat(Constants.BUNDLE_BAR_STROKE_WIDTH, mBarStrokeWidth);
        bundleViewState.putInt(Constants.BUNDLE_BAR_COLOR, mBarColor);

        bundleViewState.putFloat(Constants.BUNDLE_CONNECTING_LINE_STROKE_WIDTH, mConnectingLineStrokeWidth);
        bundleViewState.putInt(Constants.BUNDLE_CONNECTING_LINE_COLOR, mConnectingLineColor);

        bundleViewState.putInt(Constants.BUNDLE_TEXT_COLOR, mTextColor);

        return bundleViewState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        isAfterRestore = true;
        if (state instanceof Bundle) {

            Bundle bundleViewState = (Bundle) state;

            mTickStartValue = bundleViewState.getInt(Constants.BUNDLE_TICK_START_VALUE);
            mTickEndValue = bundleViewState.getInt(Constants.BUNDLE_TICK_END_VALUE);
            mTickInterval = bundleViewState.getFloat(Constants.BUNDLE_TICK_INTERVAL);
            mTickRadius = bundleViewState.getFloat(Constants.BUNDLE_TICK_RADIUS);
            mTickColor = bundleViewState.getInt(Constants.BUNDLE_TICK_COLOR);

            mPinWidth = bundleViewState.getFloat(Constants.BUNDLE_PIN_WIDTH);
            mPinColor = bundleViewState.getInt(Constants.BUNDLE_PIN_COLOR);

            mThumbColor = bundleViewState.getInt(Constants.BUNDLE_THUMB_COLOR);

            mRestoredLeftIndex = bundleViewState.getInt(Constants.BUNDLE_LEFT_THUMB_INDEX);
            mRestoredRightIndex = bundleViewState.getInt(Constants.BUNDLE_RIGHT_THUMB_INDEX);

            mBarStrokeWidth = bundleViewState.getFloat(Constants.BUNDLE_BAR_STROKE_WIDTH);
            mBarColor = bundleViewState.getInt(Constants.BUNDLE_BAR_COLOR);

            mConnectingLineStrokeWidth = bundleViewState.getFloat(Constants.BUNDLE_CONNECTING_LINE_STROKE_WIDTH);
            mConnectingLineColor = bundleViewState.getInt(Constants.BUNDLE_CONNECTING_LINE_COLOR);

            mTextColor = bundleViewState.getInt(Constants.BUNDLE_TEXT_COLOR);

            state = bundleViewState.getParcelable(Constants.BUNDLE_INSTANCE_STATE);

            createBar();
        }

        super.onRestoreInstanceState(state);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RangeBar, 0, 0);

        try {
            mTickStartValue = typedArray.getInt(R.styleable.RangeBar_tickStartValue, Constants.DEFAULT_TICK_START_VALUE);
            mTickEndValue = typedArray.getInt(R.styleable.RangeBar_tickEndValue, Constants.DEFAULT_TICK_END_VALUE);

            if (!isTickStartValueValid(mTickStartValue)) {
                mTickStartValue = Constants.DEFAULT_TICK_START_VALUE;
            }

            if (!isTickStartValueValid(mTickStartValue)) {
                mTickStartValue = Constants.DEFAULT_TICK_START_VALUE;
            }

            mTickInterval = typedArray.getFloat(R.styleable.RangeBar_tickInterval, Constants.DEFAULT_TICK_INTERVAL);

            if (!isIntervalValid(mTickInterval, mTickStartValue, mTickEndValue)) {
                mTickInterval = Constants.DEFAULT_TICK_INTERVAL;
            }

            mTickRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.RangeBar_tickRadius, Constants.DEFAULT_TICK_RADIUS),
                    getResources().getDisplayMetrics());
            mTickColor = typedArray.getInt(R.styleable.RangeBar_tickColor, Constants.DEFAULT_TICK_COLOR);

            mPinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.RangeBar_pinWidth, Constants.DEFAULT_PIN_WIDTH),
                    getResources().getDisplayMetrics());
            mPinColor = typedArray.getInt(R.styleable.RangeBar_pinColor, Constants.DEFAULT_PIN_COLOR);

            mThumbColor = typedArray.getInt(R.styleable.RangeBar_thumbColor, Constants.DEFAULT_THUMB_COLOR);

            mRestoredLeftIndex = typedArray.getInt(R.styleable.RangeBar_thumbLeftIndex, Constants.DEFAULT_LEFT_THUMB_INDEX);
            mRestoredRightIndex = typedArray.getInt(R.styleable.RangeBar_thumbRightIndex, Constants.DEFAULT_RIGHT_THUMB_INDEX);

            mBarStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getDimension(R.styleable.RangeBar_barStrokeWidth, Constants.DEFAULT_BAR_STROKE_WIDTH),
                    getResources().getDisplayMetrics());
            mBarColor = typedArray.getInt(R.styleable.RangeBar_barColor, Constants.DEFAULT_BAR_COLOR);

            mConnectingLineStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    typedArray.getDimension(R.styleable.RangeBar_connectingLineStrokeWidth, Constants.DEFAULT_CONNECTING_LINE_STROKE_WIDTH),
                    getResources().getDisplayMetrics());
            mConnectingLineColor = typedArray.getInt(R.styleable.RangeBar_connectingLineColor, Constants.DEFAULT_CONNECTING_LINE_COLOR);

            mTextColor = typedArray.getInt(R.styleable.RangeBar_textColor, Constants.DEFAULT_TEXT_COLOR);
        } finally {
            typedArray.recycle();
        }
        createBar();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * Should be as large as possible.
         * */
        switch (measureWidthMode) {
            case MeasureSpec.AT_MOST:
                width = measureWidth;
                break;
            case MeasureSpec.EXACTLY:
                width = measureWidth;
                break;
            default:
                width = mDefaultViewWidth;
                break;
        }

        /**
         * Should be as small as possible.
         * */
        switch (measureHeightMode) {
            default:
                int pinRadius = (int) mPinWidth / 2;
                int tickWidth = (int) mTickRadius * 2;

                height = measureHeight + getBottomBarOffset() + pinRadius + tickWidth;
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        isAfterResize = true;
    }

    /**
     * Method is responsible for creating all RangeBar's components.
     */
    private void createBar() {

        mBar = new Bar(mTickRadius, mTickStartValue, mTickEndValue, mTickInterval, mTickColor, mConnectingLineColor,
                mBarStrokeWidth, mBarColor,
                getSideBarOffset(), getTopBarOffset(), mPinWidth);

        mLeftThumb = new Thumb(getContext(),
                mPinWidth, mPinColor, mTextColor,
                mThumbColor,
                getSideBarOffset(), getTopBarOffset(),
                mTickRadius,
                mBar.getY());


        mRightThumb = new Thumb(getContext(),
                mPinWidth, mPinColor, mTextColor,
                mThumbColor,
                getSideBarOffset(), getTopBarOffset(),
                mTickRadius,
                mBar.getY());

        mConnectingLine = new ConnectingLine(mConnectingLineColor, mConnectingLineStrokeWidth, mBar.getY());

        if (isAfterRestore) {
            invalidate();
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBar.drawBar(canvas);
        if(isInEditMode()) {
            preparePreview(canvas);
            mBar.drawBar(canvas);
        }
        mConnectingLine.draw(canvas);
        mLeftThumb.draw(canvas);
        mRightThumb.draw(canvas);

        if (isAfterRestore) {
            initViewsStateAfterRestore();
        }

        if (!isAfterRestore && isAfterResize) {
            initViewsStateAfterFirstCreate();
        }
    }

    private void preparePreview(Canvas canvas) {
        float leftX = mBar.getTickCoordinate(mRestoredLeftIndex);
        float rightX = mBar.getTickCoordinate(mRestoredRightIndex);

        mLeftThumb.setX(leftX);
        mLeftThumb.setPinValue(String.valueOf(mTickStartValue));

        mRightThumb.setX(rightX);
        mRightThumb.setPinValue(String.valueOf(mTickEndValue));

        mConnectingLine.draw(canvas);
        mLeftThumb.draw(canvas);
        mRightThumb.draw(canvas);

        mBar.setLeftThumbX(mLeftThumb.getX());
        mBar.setRightThumbX(mRightThumb.getX());

        moveConnectingLine(mLeftThumb.getX() + mTickRadius, mRightThumb.getX() + mTickRadius);
    }

    private void initViewsStateAfterFirstCreate() {
        if (!isLeftThumbIndexValid(mRestoredLeftIndex)) {
            mRestoredLeftIndex = 0;
        }

        if (!isRightThumbIndexValid(mRestoredRightIndex)) {
            mRestoredRightIndex = mBar.getSegmentsAmount(mTickStartValue, mTickEndValue);
        }

        float leftX = mBar.getTickCoordinate(mRestoredLeftIndex);
        float rightX = mBar.getTickCoordinate(mRestoredRightIndex);

        mLeftThumb.setX(leftX);
        mLeftThumb.setPinValue(String.valueOf(mTickStartValue));

        mRightThumb.setX(rightX);
        mRightThumb.setPinValue(String.valueOf(mTickEndValue));

        mBar.setLeftThumbX(mLeftThumb.getX());
        mBar.setRightThumbX(mRightThumb.getX());

        moveConnectingLine(mLeftThumb.getX() + mTickRadius, mRightThumb.getX() + mTickRadius);

        isAfterRestore = false;
        isAfterResize = false;

        invalidate();
    }

    private void initViewsStateAfterRestore() {
        int barSegmentsAmount = mBar.getSegmentsAmount(mTickStartValue, mTickEndValue);

        float restoredLeftCoordinate;
        if (mRestoredLeftIndex >= barSegmentsAmount - 1) {
            restoredLeftCoordinate = mBar.getTickCoordinate(barSegmentsAmount - 1);
        } else {
            restoredLeftCoordinate = mBar.getTickCoordinate(mRestoredLeftIndex);
        }

        float restoredRightCoordinate;
        if (mRestoredRightIndex <= barSegmentsAmount) {
            restoredRightCoordinate = mBar.getTickCoordinate(mRestoredRightIndex);
        } else {
            restoredRightCoordinate = mBar.getTickCoordinate(barSegmentsAmount);
        }

        mLeftThumb.setX(restoredLeftCoordinate);
        mRightThumb.setX(restoredRightCoordinate);

        mBar.setLeftThumbX(mLeftThumb.getX());
        mBar.setRightThumbX(mRightThumb.getX());


        final String leftValue = String.valueOf(mBar.getTickValue(mRestoredLeftIndex));
        mLeftThumb.setPinValue(leftValue);

        final String rightValue = String.valueOf(mBar.getTickValue(mRestoredRightIndex));
        mRightThumb.setPinValue(rightValue);

        moveConnectingLine(mLeftThumb.getX() + mTickRadius, mRightThumb.getX() + mTickRadius);

        isAfterRestore = false;
        isAfterResize = false;

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float currentX = event.getX();
        final float currentY = event.getY();

        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mDifferenceX = 0;
                mDifferenceY = 0;

                mLastX = currentX;
                mLastY = currentY;
                downAction(currentX, currentY);
                fastThumbMove(currentX);
            }
            break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                getParent().requestDisallowInterceptTouchEvent(false);
                upAction();
            }
            break;

            case MotionEvent.ACTION_MOVE: {
                moveAction(currentX);
                getParent().requestDisallowInterceptTouchEvent(true);

                mDifferenceX += Math.abs(currentX - mLastX);
                mDifferenceY += Math.abs(currentY - mLastY);
                mLastX = currentX;
                mLastY = currentY;

                if (mDifferenceX < mDifferenceY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                }
            }
            break;
        }
        return true;
    }

    /**
     * This method contains logic, which is responsible for fast move of thumbs, when click outside
     * of selected values.
     *
     * @param x X coordinate of touched place on the screen
     */
    private void fastThumbMove(float x) {
        int leftThumbIndex = mBar.getNearestTickIndex(mLeftThumb);
        int rightThumbIndex = mBar.getNearestTickIndex(mRightThumb);

        int nearestIndex = mBar.getNearestTickIndex(x);
        float nearestIndexCoordinate = mBar.getTickCoordinate(nearestIndex);

        if (nearestIndex < leftThumbIndex && nearestIndex < rightThumbIndex) {
            moveThumb(mLeftThumb, true, nearestIndexCoordinate, true);
            mLeftThumb.showPinOnFastMove(this);
        }

        if (nearestIndex > leftThumbIndex && nearestIndex > rightThumbIndex) {
            moveThumb(mRightThumb, false, nearestIndexCoordinate, true);
            mRightThumb.showPinOnFastMove(this);
        }
    }

    /**
     * This method contains logic, which is responsible for setting values to
     * thumbs on press action.
     *
     * @param x X coordinate of touched place on the screen
     * @param y Y coordinate of touched place on the screen
     */
    private void downAction(float x, float y) {
        boolean inLeftTargetZone = mLeftThumb.isInTargetZone(mConnectingLine.getLineWidth(getSideBarOffset()), x, y);
        if (inLeftTargetZone && x > getSideBarOffset()) {
            mLeftThumb.showPin(this);
            mLeftThumb.setIsPressed(true);
        }

        boolean inRightTargetZone = mRightThumb.isInTargetZone(mConnectingLine.getLineWidth(getSideBarOffset()), x, y);
        if (inRightTargetZone && x > getSideBarOffset()) {
            mRightThumb.showPin(this);
            mRightThumb.setIsPressed(true);
        }
    }

    /**
     * This method contains logic, which is responsible for calculating and setting values to
     * thumbs and connecting line, when the press was released.
     */
    private void upAction() {

        if (mLeftThumb.getX() + mBar.getSegmentWidth() <= mRightThumb.getX()) {
            final float leftNearestCoordinate = mBar.getNearestTickCoordinate(mLeftThumb);
            final float rightNearestCoordinate = mBar.getNearestTickCoordinate(mRightThumb);

            String leftValue = String.valueOf(mBar.getNearestTickValue(mLeftThumb));
            String rightValue = String.valueOf(mBar.getNearestTickValue(mRightThumb));

            setThumbAndLineCoordinates(leftNearestCoordinate, rightNearestCoordinate, leftValue, rightValue);
        } else {
            final int lastLeftIndex = mBar.getNearestTickIndex(mRightThumb) - 1;
            final float lastLeftIndexCoordinate = mBar.getTickCoordinate(lastLeftIndex);

            final int lastRightIndex = mBar.getNearestTickIndex(mLeftThumb) + 1;
            final float lastRightIndexCoordinate = mBar.getTickCoordinate(lastRightIndex);

            String leftValue = String.valueOf(mBar.getTickValue(lastLeftIndex));
            String rightValue = String.valueOf(mBar.getTickValue(lastRightIndex));

            setThumbAndLineCoordinates(lastLeftIndexCoordinate, lastRightIndexCoordinate, leftValue, rightValue);
        }

        mRestoredLeftIndex = mBar.getNearestTickIndex(mLeftThumb);
        mRestoredRightIndex = mBar.getNearestTickIndex(mRightThumb);

        mLeftThumb.hidePin(this);
        mRightThumb.hidePin(this);

        mLeftThumb.setIsPressed(false);
        mRightThumb.setIsPressed(false);

        sendCallbackWithValues();
    }

    /**
     * Set coordinates to thumbs and to both sides of connecting line
     *
     * @param leftXCoordinate  Defined X coordinate of the left thumb
     * @param rightXCoordinate Defined X coordinate of the right thumb
     * @param leftValue        Value of the left thumb
     * @param rightValue       Value of the right thumb
     */
    private void setThumbAndLineCoordinates(float leftXCoordinate, float rightXCoordinate, String leftValue, String rightValue) {
        if (mLeftThumb.isPressed()) {
            mLeftThumb.setX(leftXCoordinate);
            mLeftThumb.setPinValue(leftValue);
        }

        if (mRightThumb.isPressed()) {
            mRightThumb.setX(rightXCoordinate);
            mRightThumb.setPinValue(rightValue);
        }

        moveConnectingLine(mLeftThumb.getX() + mTickRadius, mRightThumb.getX() + mTickRadius);

        invalidate();
    }

    /**
     * This method contains logic for moving of thumb
     *
     * @param currentX X coordinate of touched place on the screen
     */
    private void moveAction(float currentX) {
        if (mLeftThumb.isPressed()) {
            moveThumb(mLeftThumb, true, currentX, false);
        }

        if (mRightThumb.isPressed()) {
            moveThumb(mRightThumb, false, currentX, false);
        }

        if (mLeftThumb.getX() >= mRightThumb.getX()) {
            getPressedThumb().hidePin(this);
        }
    }

    /**
     * Moves thumb to the defined position.
     *
     * @param thumb       Thumb, which should be moved
     * @param isLeftThumb Boolean that describes what kind of thumb was moved (left or right)
     * @param currentX    X coordinate, which describes where we should move the thumb
     */
    private void moveThumb(Thumb thumb, boolean isLeftThumb, float currentX, boolean isFastMove) {
        if ((currentX >= mBar.getBarStartX() - mTickRadius && currentX <= mBar.getBarEndX()) || isFastMove) {
            thumb.setX(currentX);
            thumb.setIsPressed(true);

            final String pinValue = String.valueOf(mBar.getNearestTickValue(thumb));
            thumb.setPinValue(pinValue);

            moveOneSideConnectingLine(isLeftThumb, currentX);

            invalidate();
        }

        mBar.setLeftThumbX(mLeftThumb.getX());
        mBar.setRightThumbX(mRightThumb.getX());
    }

    /**
     * Moves line that connects the thumbs on the one side, depending on thumb, which was moved.
     *
     * @param isLeftThumb Boolean that describes what thumb was moved (left or right)
     * @param currentX    X coordinate of the moved thumb
     */
    private void moveOneSideConnectingLine(boolean isLeftThumb, float currentX) {
        if (isLeftThumb) {
            mConnectingLine.setLeftX(currentX + mTickRadius);
        } else {
            mConnectingLine.setRightX(currentX + mTickRadius);
        }
    }

    /**
     * Moves line that connects the thumbs on both sides.
     *
     * @param leftX  X coordinate of the left thumb
     * @param rightX X coordinate of the right thumb
     */
    private void moveConnectingLine(float leftX, float rightX) {
        mConnectingLine.setLeftX(leftX);
        mConnectingLine.setRightX(rightX);
    }

    /**
     * @return Thumb, which was pressed
     */
    private Thumb getPressedThumb() {
        return mLeftThumb.isPressed() ? mLeftThumb : mRightThumb;
    }

    /**
     * Sends callback to all subscribers of RangeBar, when values were changed.
     */
    private void sendCallbackWithValues() {
        final int leftIndex = mBar.getNearestTickIndex(mLeftThumb);
        final int rightIndex = mBar.getNearestTickIndex(mRightThumb);

        final String leftPinValue = String.valueOf(mBar.getNearestTickValue(mLeftThumb));
        final String rightPinValue = String.valueOf(mBar.getNearestTickValue(mRightThumb));
        if (mOnRangeBarChangeListener != null) {
            mOnRangeBarChangeListener.onValueChanged(this, leftIndex, rightIndex, leftPinValue, rightPinValue);
        }
    }

    /**
     * @return Offset which is used for left and right side of RangeBar
     */
    public final float getSideBarOffset() {
        return mTickRadius;
    }

    /**
     * @return Offset which is used for top of RangeBar
     */
    public final int getTopBarOffset() {
        return Constants.DEFAULT_PIN_MARGIN;
    }

    /**
     * @return Offset which is used for bottom of RangeBar
     */
    public final int getBottomBarOffset() {
        return (int) mTickRadius * 2;
    }

    /**
     * Register a callback to be invoked when this view values are changed.
     *
     * @param onRangeBarChangeListener The callback that will run
     */
    public final void setOnRangeBarChangeListener(OnRangeBarChangeListener onRangeBarChangeListener) {
        this.mOnRangeBarChangeListener = onRangeBarChangeListener;
    }

    public void setTickStartValue(int value) {
        if (isTickStartValueValid(value)) {
            this.mTickStartValue = value;

            mBar.setTickStartValue(value);

            updateView(true);
        }
    }

    public int getTickStartValue() {
        return mTickStartValue;
    }

    public void setTickEndValue(int value) {
        if (isTickEndValueValid(value)) {
            this.mTickEndValue = value;

            mBar.setTickEndValue(value);

            updateView(true);
        }
    }

    public int getTickEndValue() {
        return mTickEndValue;
    }

    private boolean isTickEndValueValid(int value) {
        if (value > mTickStartValue && value > 0) {
            return true;
        } else {
            Log.d(Constants.LOG_TAG, "Tick end is invalid");
            return false;
        }
    }

    private boolean isTickStartValueValid(int value) {
        if (value < mTickEndValue) {
            return true;
        } else {
            Log.d(Constants.LOG_TAG, "Tick start value is invalid");
            return false;
        }
    }

    public void setTickInterval(float interval) {
        if (isIntervalValid(interval, mTickStartValue, mTickEndValue)) {
            this.mTickInterval = interval;

            mBar.setTickInterval(mTickInterval);

            int lastIndex = mBar.getSegmentsAmount(mTickStartValue, mTickEndValue);
            mRestoredLeftIndex = 0;
            mRestoredRightIndex = lastIndex;

            updateView(true);
        }
    }

    private boolean isIntervalValid(float interval, int leftValue, int rightValue) {
        if (interval != 0) {
            for (int index = 0; index < rightValue; index++) {
                if (interval * index + leftValue == rightValue) {
                    return true;
                }
            }
        }

        Log.e(Constants.LOG_TAG, "Wrong interval");
        return false;
    }

    public float getTickInterval() {
        return mBar.getTickRadius();
    }

    public void setTickRadius(float radius) {
        this.mTickRadius = radius;

        mBar.setTickRadius(mTickRadius);
        mLeftThumb.setThumbRadius(mTickRadius);
        mRightThumb.setThumbRadius(mTickRadius);


        /**
         * RangeBar should be recreated to calculate again bar and segments width to draw ticks.
         */
        isAfterResize = true;
        createBar();

        updateView(false);
        requestLayout();
    }

    public float getTickRadius() {
        return mBar.getTickRadius();
    }

    public void setTickColor(int color) {
        this.mTickColor = color;

        mBar.setTickColor(color);

        updateView(true);
    }

    public int getTickColor() {
        return mBar.getTickColor();
    }

    public void setPinWidth(float width) {
        if (width > 0) {
            this.mPinWidth = width;

            mBar.setPinWidth(width);
            mLeftThumb.setPinWidth(width);
            mRightThumb.setPinWidth(width);
            mLeftThumb.setY(mBar.getY());
            mRightThumb.setY(mBar.getY());
            mConnectingLine.setY(mBar.getY());

            updateView(true);
            requestLayout();
        }
    }

    public float getPinWidth() {
        return mPinWidth;
    }

    public void setPinColor(int color) {
        this.mPinColor = color;

        mLeftThumb.setPinColor(color);
        mRightThumb.setPinColor(color);

        updateView(true);
    }

    public int getPinColor() {
        return mPinColor;
    }

    public void setLeftThumbIndex(int index) {
        if (isLeftThumbIndexValid(index)) {
            mRestoredLeftIndex = index;

            updateView(true);
        }
    }

    public int getLeftThumbIndex() {
        return mRestoredLeftIndex;
    }

    public void setRightThumbIndex(int index) {
        if (isRightThumbIndexValid(index)) {
            mRestoredRightIndex = index;

            updateView(true);
        }
    }

    public int getRightThumbIndex() {
        return mRestoredRightIndex;
    }

    private boolean isLeftThumbIndexValid(int index) {
        return mRestoredRightIndex > index && index >= 0;
    }

    private boolean isRightThumbIndexValid(int index) {
        int lastIndex = mBar.getSegmentsAmount(mTickStartValue, mTickEndValue);
        return mRestoredLeftIndex < index && index <= lastIndex;
    }

    public void setBarStrokeWidth(float width) {
        this.mBarStrokeWidth = width;

        mBar.setBarStrokeWidth(width);

        updateView(true);
    }

    public float getBarStrokeWidth() {
        return mBar.getBarStrokeWidth();
    }

    public void setBarColor(int color) {
        this.mBarColor = color;

        mBar.setBarColor(color);

        updateView(true);
    }

    public int getBarColor() {
        return mBar.getBarColor();
    }

    public void setConnectingLineStrokeWidth(float width) {
        this.mConnectingLineStrokeWidth = width;

        mConnectingLine.setStrokeWidth(width);

        updateView(true);
    }

    public float getConnectingLineStrokeWidth() {
        return mConnectingLine.getStrokeWidth();
    }

    public void setConnectingLineColor(int color) {
        this.mConnectingLineColor = color;

        mConnectingLine.setLineColor(color);
        mBar.setSelectedTickColor(color);

        updateView(true);
    }

    public float getConnectingLineColor() {
        return mConnectingLine.getLineColor();
    }

    public void setTextColor(int color) {
        this.mTextColor = color;

        mLeftThumb.setPinTextColor(color);
        mRightThumb.setPinTextColor(color);

        updateView(true);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setThumbColor(int color) {
        this.mTextColor = color;

        mLeftThumb.setThumbColor(color);
        mRightThumb.setThumbColor(color);

        updateView(true);
    }

    private void updateView(boolean isAfterRestore) {
        this.isAfterRestore = isAfterRestore;
        invalidate();
    }

    public int getThumbColor() {
        return mThumbColor;
    }
    /**
     * This interface is called after each change of RangeBar's values.
     */
    public interface OnRangeBarChangeListener {
        void onValueChanged(RangeBar rangeBar, int leftIndex, int rightIndex, String leftValue, String rightValue);
    }
}
