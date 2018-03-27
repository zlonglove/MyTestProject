package im.icbc.cn.keyboard.utils;

import android.util.Log;

public class LogUtils {

    private static boolean FLAG = true;
    private static final String TAG = "icbcKeyboard";

    public static void setFlag(boolean flag) {
        FLAG = flag;
    }

    /**
     * 根据Log不同等级
     */

    public static void v(String text) {
        if (FLAG) {
            Log.v(TAG, text);
        }
    }

    public static void d(String text) {
        if (FLAG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (FLAG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (FLAG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (FLAG) {
            Log.e(TAG, text);
        }
    }
}
