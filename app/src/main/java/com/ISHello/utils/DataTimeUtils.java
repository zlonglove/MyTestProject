package com.ISHello.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanglong on 2017/2/16.
 */

/**
 * SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，如果定义为
 * static，必须加锁，或者使用 DateUtils 工具类。
 * 正例:注意线程安全，使用 DateUtils。亦推荐如下处理:
 * private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() { @Override
 * protected DateFormat initialValue() {
 * return new SimpleDateFormat("yyyy-MM-dd");
 * } };
 * 说明:如果是 JDK8 的应用，可以使用 Instant 代替 Date，LocalDateTime 代替 Calendar，
 * DateTimeFormatter 代替 Simpledateformatter，官方给出的解释:simple beautiful strong immutable thread-safe。
 */
public class DataTimeUtils {
    private final static String TAG = "DataTimeUtils";
    /**
     * 不同的线程会创建不同的SimpleDateFormat对象
     */
    private static ThreadLocal<SimpleDateFormat> dtf = new ThreadLocal() {

        @Override
        protected SimpleDateFormat initialValue() {
            Log.i(TAG, "--->df initialValue");
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static ThreadLocal<SimpleDateFormat> df = new ThreadLocal() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String getDateTimeString(long milliseconds) {
        return dtf.get().format(new Date(milliseconds));
    }

    public static String getDateString(long milliseconds) {
        return df.get().format(new Date(milliseconds));
    }

}

