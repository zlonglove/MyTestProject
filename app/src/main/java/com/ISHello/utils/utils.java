package com.ISHello.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class utils {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private final static String TAG = "utils";
    private static PackageManager sPackageManager;
    private static String sPackageName;
    private static int sVersionCode;
    private static String sVersionName;

    private static int sSdkVersion;

    private static int sGlEsVersion;

    public static String getDateFormat(Context context) {
        String dateFormat = Settings.System.getString(context.getContentResolver(), Settings.System.DATE_FORMAT);
        if (dateFormat == null || dateFormat.length() < 4) {
            dateFormat = DEFAULT_DATE_FORMAT;
        }
        return dateFormat;
    }


    public static String getCurDate(Context context) {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        /**
         * get date
         */
        String dateFormat = getDateFormat(context);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String date = formatter.format(curDate);

        /**
         * get week
         */
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        String week = weekDays[w];

        /**
         * get time
         */
        boolean is24Format = DateFormat.is24HourFormat(context);
        String timeFormat = "hh:mm:ss";
        if (is24Format) {
            timeFormat = "HH:mm：ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        String time = df.format(curDate);


        String curDateStr = date + " " + week + " " + time;
        int year = cal.get(Calendar.YEAR);
        if (year == 1970) {
            curDateStr = "";
        }
        return curDateStr;
    }

    public static int getVersionCode(Context context) {
        if (sPackageManager == null) {
            sPackageManager = context.getPackageManager();
        }
        if (sVersionCode == 0 && sPackageManager != null) {
            try {
                sPackageName = context.getPackageName();
                sVersionCode = sPackageManager.getPackageInfo(sPackageName, 0).versionCode;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sVersionCode;
    }

    public static String getVersionName(Context context) {
        if (sPackageManager == null) {
            sPackageManager = context.getPackageManager();
        }
        if (sPackageManager != null) {
            try {
                sPackageName = context.getPackageName();

                sVersionName = sPackageManager.getPackageInfo(sPackageName, 0).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sVersionName;
    }

    /**
     * Gets Android SDK version
     *
     * @return Android SDK version used to build the project
     */
    @SuppressWarnings("deprecation")
    public static int getSDKVersion() {
        if (sSdkVersion == 0) {
            sSdkVersion = Integer.parseInt(Build.VERSION.SDK);
        }
        return sSdkVersion;
    }

    public static int getGlEsVersion(Context context) {
        if (sGlEsVersion == 0) {
            sGlEsVersion = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().reqGlEsVersion;
            Log.d(TAG, "sGlEsVersion=" + sGlEsVersion);
        }
        return sGlEsVersion;
    }

    public static boolean isGlEs2Supported(Context context) {
        return getGlEsVersion(context) >= 0x20000;
    }

    public static boolean isSLEs2Supported() {
        return (utils.getSDKVersion() >= 9);
    }

}
