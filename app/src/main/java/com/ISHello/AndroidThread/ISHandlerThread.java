package com.ISHello.AndroidThread;

import android.os.Looper;

/**
 * Created by kfzx-zhanglong on 2016/4/26.
 * Company ICBC
 * 当不需要使用的时候调用它的quit()或者quitSafely()方法来终止线程的执行
 */
public class ISHandlerThread extends Thread {
    public static final int THREAD_PRIORITY_DEFAULT = 0;
    int mPriority;
    int mTid = -1;
    Looper mLooper;

    public ISHandlerThread(String name) {
        super(name);
        mPriority = THREAD_PRIORITY_DEFAULT;
    }

    /**
     * Constructs a HandlerThread.
     *
     * @param name
     * @param priority The priority to run the thread at. The value supplied must be from
     *                 {@link android.os.Process} and not from java.lang.Thread.
     */
    public ISHandlerThread(String name, int priority) {
        super(name);
        mPriority = priority;
    }

    /**
     * Call back method that can be explicitly over ridden if needed to execute some
     * setup before Looper loops.
     */
    protected void onLooperPrepared() {
    }

    public void run() {
        mTid = android.os.Process.myTid();
        Looper.prepare();//创建消息队列,创建了Looper,放在ThreadLocal中
        synchronized (this) {
            mLooper = Looper.myLooper();//从ThreadLocal中取出Looper对象
            notifyAll();//唤起wait()方法
        }
        android.os.Process.setThreadPriority(mPriority);
        onLooperPrepared();
        Looper.loop();//开启消息循环
        mTid = -1;
    }

    /**
     * 获取当前线程的 Looper
     * 如果线程不是正常运行的就返回 null
     * 如果线程启动后,Looper 还没创建,就 wait() 等待创建Looper后notify
     *
     * @return The looper.
     */
    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }

        // If the thread has been started, wait until the looper has been created.
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();//直到调用notifyAll()才唤起
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }

    /**
     * Ask the currently running looper to quit.  If the thread has not
     * been started or has finished (that is if {@link #getLooper} returns
     * null), then false is returned.  Otherwise the looper is asked to
     * quit and true is returned.
     */
    public boolean quit() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        }
        return false;
    }

    /**
     * Returns the identifier of this thread. See Process.myTid().
     */
    public int getThreadId() {
        return mTid;
    }
}
