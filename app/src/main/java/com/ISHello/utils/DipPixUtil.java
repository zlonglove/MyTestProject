package com.ISHello.utils;

import android.content.Context;

/**
 * @author kfzx-zhanglong
 */

public class DipPixUtil {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static class DisplayRect {
        private int width;

        private int height;

        public DisplayRect(int width, int height) {
            this.width = width;
            this.height = height;
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

    }

    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getWindowHeight(Context context) {
        // if (dmMetrics == null) {
        // dmMetrics = new DisplayMetrics();
        // }
        // ((WindowManager)
        // context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dmMetrics);
        // return dmMetrics.heightPixels;
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getWindowDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getWindowDensityDPI(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }
    /*
	 * public static DisplayRect getWindowDiaplay(Context context) { Display
	 * display = ((WindowManager) context
	 * .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	 * DisplayRect rect = new DisplayRect(display.getWidth(),
	 * display.getHeight()); return rect; }
	 * 
	 * public static int getWindowWidth(Context context) { WindowManager wm =
	 * (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); return
	 * wm.getDefaultDisplay().getWidth(); }
	 * 
	 * public static int getWindowHeight(Context context) { WindowManager wm =
	 * (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); return
	 * wm.getDefaultDisplay().getHeight(); }
	 */
}
