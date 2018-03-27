package com.ISHello.ISExecutors;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kfzx-zhanglong on 2016/4/28.
 * Company ICBC
 */
public class CachedThreadPool {
    private final static String TAG = "CachedThreadPool";
    private static CachedThreadPool cacheThreadPool;
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

    private CachedThreadPool() {
        if (execute == null) {
            execute = Executors.newCachedThreadPool();
        }
    }

    public static CachedThreadPool getInstance() {
        if (cacheThreadPool == null) {
            cacheThreadPool = new CachedThreadPool();
        }
        return cacheThreadPool;
    }

    public void excute() {
        for (int i = 0; i < 10; i++) {
            execute.execute(command);
        }
    }
}
