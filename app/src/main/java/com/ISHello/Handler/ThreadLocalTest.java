package com.ISHello.Handler;

import android.util.Log;

/**
 * Created by kfzx-zhanglong on 2016/4/25.
 * Company ICBC
 */
public class ThreadLocalTest {
    private final String TAG = "ThreadLocalTest";
    private ThreadLocal<String> mStringThreadLocal = new ThreadLocal<String>();

    public void test() {
        mStringThreadLocal.set("mainThread");
        Log.i(TAG, "--->mainThread ThreadLocal==" + mStringThreadLocal.get());
        new Thread("Thread 1") {
            @Override
            public void run() {
                mStringThreadLocal.set("Thread 1");
                Log.i(TAG, "--->Thread 1 ThreadLocal==" + mStringThreadLocal.get());
            }
        }.start();

        new Thread("Thread 2") {
            @Override
            public void run() {
                Log.i(TAG, "--->Thread 2 ThreadLocal==" + mStringThreadLocal.get());
            }
        }.start();
    }
}
