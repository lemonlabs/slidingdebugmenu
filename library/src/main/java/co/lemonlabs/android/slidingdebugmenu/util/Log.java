package co.lemonlabs.android.slidingdebugmenu.util;

import org.json.JSONObject;

/**
 * Wrapper of core {@link android.util.Log} functions
 * to allow control using SlidingDebugMenu
 * <p/>
 * Created by balysv on 13/01/14.
 * www.lemonlabs.co
 */
public class Log {

    public static int LOG_LEVEL = 2;

    // VERBOSE

    public static void v(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.VERBOSE)
            android.util.Log.v(tag, message);
    }

    public static void v(String tag, boolean b) {
        v(tag, String.valueOf(b));
    }

    public static void v(String tag, int i) {
        v(tag, String.valueOf(i));
    }

    public static void v(String tag, JSONObject json) {
        v(tag, json.toString());
    }

    // DEBUG

    public static void d(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.DEBUG)
            android.util.Log.d(tag, message);
    }

    public static void d(String tag, boolean b) {
        d(tag, String.valueOf(b));
    }

    public static void d(String tag, int i) {
        d(tag, String.valueOf(i));
    }

    public static void d(String tag, JSONObject json) {
        d(tag, json.toString());
    }

    // INFO

    public static void i(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.INFO)
            android.util.Log.i(tag, message);
    }

    // WARN

    public static void w(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.WARN)
            android.util.Log.w(tag, message);
    }

    // ERROR
    public static void e(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.ERROR)
            android.util.Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable error) {
        if (LOG_LEVEL <= android.util.Log.ERROR)
            android.util.Log.e(tag, message, error);
    }

    public static void e(String tag, JSONObject json) {
        e(tag, json.toString());
    }

    // WTF
    public static void wtf(String tag, String message) {
        if (LOG_LEVEL <= android.util.Log.ASSERT)
            android.util.Log.wtf(tag, message);
    }

    public static void wtf(String tag, String message, Throwable error) {
        if (LOG_LEVEL <= android.util.Log.ASSERT)
            android.util.Log.wtf(tag, message, error);
    }

}
