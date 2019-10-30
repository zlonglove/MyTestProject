package com.ISHello.ScreenInfo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ISHello.Application.BaseApplication;

/**
 * 获取屏幕的信息
 *
 * @author zhanglong
 */
public class ISScreenInfo {
    private int width;
    private int height;
    private float density;
    private int densityDpi;

    private static ISScreenInfo isScreenInfo;

    public static ISScreenInfo instance() {
        if (isScreenInfo == null) {
            isScreenInfo = new ISScreenInfo();
        }
        return isScreenInfo;
    }

    public ISScreenInfo() {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        //activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        wm.getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;  // 屏幕宽度（像素）
        height = metric.heightPixels;  // 屏幕高度（像素）
        density = metric.density;  //          屏幕密度（0.75 / 1.0 / 1.5 / 2 / 3 / 4 ）
        densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240 / 320 / 480 / 640 ）
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(int densityDpi) {
        this.densityDpi = densityDpi;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "width==" + getWidth() + " height==" + getHeight() + " density==" + getDensity()
                + " densityDpi==" + getDensityDpi();

    }


}
