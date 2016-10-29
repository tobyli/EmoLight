package edu.cmu.hcii.emolight;

import android.view.accessibility.AccessibilityEvent;

/**
 * @author toby
 * @date 10/29/16
 * @time 11:02 AM
 */
public class Const {

    final static public Integer[] ACCESSIBILITY_EVENT_TO_HANDLE = {AccessibilityEvent.TYPE_VIEW_CLICKED,
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED,
            AccessibilityEvent.TYPE_VIEW_SELECTED,
            AccessibilityEvent.TYPE_VIEW_FOCUSED,
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED,
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOWS_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED};

    final static public String[] APP_PACKAGE_NAME_TO_HANDLE = {"com.google.android.youtube", "com.musixmatch.android.lyrify"};

    final static public int YOUTUBE_MAIN_TITLE_THRESHOLD = 1100;
    final static public int MUSIXMATCH_HEIGHT_THRESHOLD = 300;
    final static public int MUSIXMATCH_WIDTH_THRESHOLD = 500;
}
