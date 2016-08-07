package com.netease.idate.net.api;

import android.util.Log;

/**
 * Created by daisongsong on 16/8/7.
 */

public class Logger {
    private static boolean debug = true;

    public static void setDebug(boolean enable) {
        debug = enable;
    }

    public static void d(String tag, String format, Object... args) {
        if (debug) {
            Log.d(tag, String.format(format, args));
        }
    }
}
