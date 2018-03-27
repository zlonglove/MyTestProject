package com.ISHello.ISExecutors;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kfzx-zhanglong on 2016/4/28.
 * Company ICBC
 * 用于执行定时任务和具有周期性的重复任务
 */
public class ScheduleThreadPool {
    private final static String TAG = "ScheduleThreadPool";
    private static ScheduleThreadPool scheduleThreadPool;
    private final static int corePoolSize = 5;
    private int count = 0;
    private ScheduledExecutorService execute;
    Runnable command = new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(2000);
            count++;
            Log.i(TAG, "--->" + count + "/Thread Name=" + Thread.currentThread().getName());
        }
    };

    private ScheduleThreadPool() {
        if (execute == null) {
            execute = Executors.newScheduledThreadPool(corePoolSize);
        }
    }

    public static ScheduleThreadPool getInstance() {
        if (scheduleThreadPool == null) {
            scheduleThreadPool = new ScheduleThreadPool();
        }
        return scheduleThreadPool;
    }

    public void excuteDelay(long delay) {
        for (int i = 0; i < 10; i++) {
            execute.schedule(command, delay, TimeUnit.MILLISECONDS);
        }
    }

    public void excuteAtFixedRate(long delay, long period) {
        execute.scheduleAtFixedRate(command, delay, 1000, TimeUnit.MILLISECONDS);
    }
}
