package com.ISHello.utils;

/**
 * Created by kfzx-zhanglong on 2016/6/28.
 * Company ICBC
 */

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {

    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && !imm.showSoftInput(view, 0)) {
            LogUtil.log("showInputMethod", "Failed to show soft input method.");
        }

    }

    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
