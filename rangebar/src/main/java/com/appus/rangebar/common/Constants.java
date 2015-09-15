package com.appus.rangebar.common;

import com.appus.rangebar.RangeBar;

/**
 * Created by igor.malytsky on 9/2/15.
 */
public class Constants {
    public static final String LOG_TAG = RangeBar.class.getSimpleName();

    /**
     * Constants for view
     * */
    public static final int DEFAULT_VIEW_WIDTH = 300;
    public static final int DEFAULT_VIEW_HEIGHT = 150;

    /**
     * Constants for ticks
     * */
    public static final int DEFAULT_TICK_START_VALUE = 1;
    public static final int DEFAULT_TICK_END_VALUE = 5;
    public static final float DEFAULT_TICK_INTERVAL = 1f;
    public static final float DEFAULT_TICK_RADIUS = 5f;
    public static final int DEFAULT_TICK_COLOR = 0xFFE0E0E0;

    /**
     * Constants for thumb
     * */
    public static final float DEFAULT_THUMB_RADIUS = 5f;
    public static final int DEFAULT_THUMB_COLOR = 0xFFBA68C8;
    public static final int DEFAULT_LEFT_THUMB_INDEX = 0;
    public static final int DEFAULT_RIGHT_THUMB_INDEX = 2;

    /**
     * Constants for pin
     * */
    public static final float DEFAULT_PIN_WIDTH = 50f;
    public static final int DEFAULT_PIN_COLOR = 0xFFBA68C8;
    public static final int DEFAULT_PIN_MARGIN = 10;

    /**
     * Constants for bar
     * */
    public static final float DEFAULT_BAR_STROKE_WIDTH = 3f;
    public static final int DEFAULT_BAR_COLOR = 0xFFE0E0E0;

    /**
     * Constants for connecting line
     * */
    public static final float DEFAULT_CONNECTING_LINE_STROKE_WIDTH = 3f;
    public static final int DEFAULT_CONNECTING_LINE_COLOR = 0xFFBA68C8;

    /**
     * Constants for text
     * */
    public static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;

    /**
     * Constants for saving view state
     * */
    public static final String BUNDLE_INSTANCE_STATE = "bundle_instance_state";

    public static final String BUNDLE_TICK_START_VALUE = "bundle_tick_start_value";
    public static final String BUNDLE_TICK_END_VALUE = "bundle_tick_end_value";
    public static final String BUNDLE_TICK_INTERVAL = "bundle_tick_interval";
    public static final String BUNDLE_TICK_RADIUS = "bundle_tick_radius";
    public static final String BUNDLE_TICK_COLOR = "bundle_tick_color";

    public static final String BUNDLE_THUMB_RADIUS = "bundle_thumb_radius";
    public static final String BUNDLE_THUMB_COLOR = "bundle_thumb_color";
    public static final String BUNDLE_LEFT_THUMB_INDEX = "bundle_left_thumb_coordinate";
    public static final String BUNDLE_RIGHT_THUMB_INDEX = "bundle_right_thumb_coordinate";

    public static final String BUNDLE_PIN_WIDTH = "bundle_pin_width";
    public static final String BUNDLE_PIN_COLOR = "bundle_pin_color";

    public static final String BUNDLE_BAR_STROKE_WIDTH = "bundle_bar_stroke_width";
    public static final String BUNDLE_BAR_COLOR = "bundle_bar_color";

    public static final String BUNDLE_CONNECTING_LINE_STROKE_WIDTH = "bundle_connecting_line_height";
    public static final String BUNDLE_CONNECTING_LINE_COLOR = "bundle_connecting_line_color";

    public static final String BUNDLE_TEXT_COLOR = "bundle_text_color";
    public static final String BUNDLE_TEXT_SIZE = "bundle_text_size";
}
