package com.ISHello.ISExecutors;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kfzx-zhanglong on 2016/4/28.
 * Company ICBC
 */
public class FixedThreadPool {
    private final static String TAG = "FixedThreadPool";
    private static FixedThreadPool fixedThreadPool;
    private final static int FIX_THREAD_COUNT = 5;
    private int count = 0;
    private ExecutorService execute;
    Runnable command = new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(2000);
            count++;
            Log.i(TAG, "--->" + count + "/Thread Name=" + Thread.currentThread().getName());
        }
    };

    private FixedThreadPool() {
        if (execute == null) {
            execute = Executors.newFixedThreadPool(FIX_THREAD_COUNT);
        }
    }

    public static FixedThreadPool getInstance() {
        if (fixedThreadPool == null) {
            fixedThreadPool = new FixedThreadPool();
        }
        return fixedThreadPool;
    }

    public void excute() {
        for (int i = 0; i < 10; i++) {
            execute.execute(command);
        }
    }
}
