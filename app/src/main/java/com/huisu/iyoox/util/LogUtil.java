package com.huisu.iyoox.util;
import android.util.Log;

import com.huisu.iyoox.BuildConfig;


/**
 * 打印log日志
 */
public class LogUtil {
    public static void v(String key, String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.v(key, ">>" + msg);
    }

    public static void d(String key, String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.d(key, ">>" + msg);
    }

    public static void i(String key, String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.i(key, ">>" + msg);
    }

    public static void w(String key, String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.w(key, ">>" + msg);
    }

    public static void e(String key, String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.e(key, ">>" + msg);
    }

    public static void e(String msg) {
        if (!BuildConfig.LOGSHOW)
            return;
        Log.e("iyoox", msg);
    }
}
