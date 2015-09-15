package com.appus.rangebar_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.appus.rangebar.RangeBar;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class MainActivity extends AppCompatActivity implements RangeBar.OnRangeBarChangeListener, SeekBar.OnSeekBarChangeListener {

    private RangeBar mRangeBar;

    private SeekBar mTickStartValue;
    private SeekBar mTickEndValue;
    private SeekBar mTickInterval;
    private SeekBar mTickRadius;
    private SeekBar mBarStrokeWidth;
    private SeekBar mConnectingLineStrokeWidth;
    private SeekBar mPinWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mRangeBar = (RangeBar) findViewById(R.id.rb_sample);

        mTickStartValue = (SeekBar) findViewById(R.id.sb_start_value);
        mTickEndValue = (SeekBar) findViewById(R.id.sb_end_value);
        mTickInterval = (SeekBar) findViewById(R.id.sb_tick_interval);
        mTickRadius = (SeekBar) findViewById(R.id.sb_tick_radius);
        mBarStrokeWidth = (SeekBar) findViewById(R.id.sb_bar_stroke_width);
        mConnectingLineStrokeWidth = (SeekBar) findViewById(R.id.sb_connecting_line_stroke_width);
        mPinWidth = (SeekBar) findViewById(R.id.sb_pin_width);
        initListeners();
    }

    private void initListeners() {
        mRangeBar.setOnRangeBarChangeListener(this);

        mTickStartValue.setOnSeekBarChangeListener(this);
        mTickEndValue.setOnSeekBarChangeListener(this);
        mTickInterval.setOnSeekBarChangeListener(this);
        mTickRadius.setOnSeekBarChangeListener(this);
        mBarStrokeWidth.setOnSeekBarChangeListener(this);
        mConnectingLineStrokeWidth.setOnSeekBarChangeListener(this);
        mPinWidth.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onValueChanged(RangeBar rangeBar, int leftIndex, int rightIndex, String leftValue, String rightValue) {
        Log.d(MainActivity.class.getSimpleName(), "rangebar: " + rangeBar.getId() + " leftValue: " + leftValue + " rightValue: " + rightValue);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_start_value:
                mRangeBar.setTickStartValue(progress);
                break;
            case R.id.sb_end_value:
                mRangeBar.setTickEndValue(progress);
                break;
            case R.id.sb_tick_interval:
                mRangeBar.setTickInterval(progress);
                break;
            case R.id.sb_tick_radius:
                mRangeBar.setTickRadius(progress);
                break;
            case R.id.sb_bar_stroke_width:
                mRangeBar.setBarStrokeWidth(progress);
                break;
            case R.id.sb_connecting_line_stroke_width:
                mRangeBar.setConnectingLineStrokeWidth(progress);
                break;
            case R.id.sb_pin_width:
                mRangeBar.setPinWidth(progress * 3);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
