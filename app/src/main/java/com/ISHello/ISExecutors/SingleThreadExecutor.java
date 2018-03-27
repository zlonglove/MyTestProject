package com.ISHello.ISExecutors;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kfzx-zhanglong on 2016/4/28.
 * Company ICBC
 * 线程池内只有一个核心线程,确保所有任务都在一个线程中按顺序执行
 */
public class SingleThreadExecutor {
    private final static String TAG = "SingleThreadExecutor";
    private static SingleThreadExecutor singleThreadExecutor;
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

    private SingleThreadExecutor() {
        if (execute == null) {
            execute = Executors.newSingleThreadExecutor();
        }
    }

    public static SingleThreadExecutor getInstance() {
        if (singleThreadExecutor == null) {
            singleThreadExecutor = new SingleThreadExecutor();
        }
        return singleThreadExecutor;
    }

    public void excute() {
        for (int i = 0; i < 10; i++) {
            execute.execute(command);
        }
    }
}
