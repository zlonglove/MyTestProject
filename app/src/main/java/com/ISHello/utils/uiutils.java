package com.ISHello.utils;

import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author kfzx-zhanglong
 */
public class uiutils {

    private final String TAG = "uiutils";
    private static uiutils uiutils;

    private uiutils() {

    }

    /**
     * Returns singleton class instance
     */
    public static uiutils getInstance() {
        if (uiutils == null) {
            synchronized (uiutils.class) {
                if (uiutils == null) {
                    uiutils = new uiutils();
                }
            }
        }
        return uiutils;
    }

    /**
     * 隐藏标题栏,在onCreate()里调用,调用super之前
     *
     * @param activity
     */
    public void hideTitle(Activity activity) {
        // TODO Auto-generated method stub
        if (activity == null) {
            return;
        }
        Log.i(TAG, "－－－>HideTitle");
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 状态栏隐藏(并且全屏)
     */
    public void hideStatusBar(Activity activity) {
        // TODO Auto-generated method stub
        if (activity == null) {
            return;
        }
        // 隐藏标题
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得窗口对象
        Window myWindow = activity.getWindow();
        // 设置Flag标识
        myWindow.setFlags(flag, flag);
    }

}
