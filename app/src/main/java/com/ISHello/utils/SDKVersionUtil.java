package com.ISHello.utils;

/**
 * Created by kfzx-zhanglong on 2016/6/29.
 * Company ICBC
 */

import android.content.Context;
import android.os.Build.VERSION;

public class SDKVersionUtil {
    /**
     * January 2010: Android 2.1
     */
    public static final int ECLAIR_MR1 = 7;
    /**
     * June 2010: Android 2.2
     */
    public static final int FROYO = 8;
    /**
     * November 2010: Android 2.3
     */
    public static final int GINGERBREAD = 9;
    /**
     * February 2011: Android 2.3.3.
     */
    public static final int GINGERBREAD_MR1 = 10;
    /**
     * February 2011: Android 3.0.
     **/
    public static final int HONEYCOMB = 11;
    /**
     * May 2011: Android 3.1.
     */
    public static final int HONEYCOMB_MR1 = 12;
    /**
     * June 2011: Android 3.2.
     */
    public static final int HONEYCOMB_MR2 = 13;
    /**
     * October 2011: Android 4.0.
     */
    public static final int ICE_CREAM_SANDWICH = 14;
    /**
     * December 2011: Android 4.0.3.
     */
    public static final int ICE_CREAM_SANDWICH_MR1 = 15;
    /**
     * June 2012: Android 4.1.
     */
    public static final int JELLY_BEAN = 16;
    /**
     * November 2012: Android 4.2, Moar jelly beans!
     **/
    public static final int JELLY_BEAN_MR1 = 17;
    /**
     * July 2013: Android 4.3, the revenge of the beans.
     */
    public static final int JELLY_BEAN_MR2 = 18;

    /**
     * October 2013: Android 4.4, KitKat, another tasty treat.
     **/
    public static final int KITKAT = 19;
    /**
     * Android 4.4W: KitKat for watches, snacks on the run.
     */
    public static final int KITKAT_WATCH = 20;

    /**
     * @hide
     */
    public static final int L = 21;


    public static boolean hasECLAIR_MR1() {
        return VERSION.SDK_INT >= ECLAIR_MR1;
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= FROYO;
    }

    public static boolean hasGingerbread() {
        return VERSION.SDK_INT >= GINGERBREAD;
    }

    public static boolean hasGingerbread_MR1() {
        return VERSION.SDK_INT >= GINGERBREAD_MR1;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return VERSION.SDK_INT >= HONEYCOMB_MR1;
    }

    public static boolean hasHoneycombMR2() {
        return VERSION.SDK_INT >= HONEYCOMB_MR2;
    }

    public static boolean hasICS() {
        return VERSION.SDK_INT >= ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBean() {
        return VERSION.SDK_INT >= JELLY_BEAN;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & ICE_CREAM_SANDWICH_MR1) >= 3;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }
}
