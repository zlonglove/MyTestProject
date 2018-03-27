package com.ISHello.Handler;

import android.os.Looper;
import android.os.Process;

public class LooperThread extends Thread {
    private final static String TAG = "LooperThread";
    int mPriority;
    int mTid = -1;
    Looper mLooper;

    public LooperThread() {
        this(TAG);
    }

    public LooperThread(String name) {
        super(name);
        mPriority = Process.THREAD_PRIORITY_DEFAULT;
    }

    /**
     * Call back method that can be explicitly over ridden if needed to execute
     * some setup before Looper loops.
     */
    protected void onLooperPrepared() {
    }

    @Override
    public void run() {
        mTid = Process.myTid();
        // 将当前线程初始化为Looper线程
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        Process.setThreadPriority(mPriority);
        onLooperPrepared();
        // 开始循环处理消息队列
        Looper.loop();
        mTid = -1;
    }

    /**
     * This method returns the Looper associated with this thread. If this
     * thread not been started or for any reason is isAlive() returns false,
     * this method will return null. If this thread has been started, this
     * method will block until the looper has been initialized.
     *
     * @return The looper.
     */
    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        // If the thread has been started, wait until the looper has been
        // created.
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }

    /**
     * Ask the currently running looper to quit. If the thread has not been
     * started or has finished (that is if {@link #getLooper} returns null),
     * then false is returned. Otherwise the looper is asked to quit and true is
     * returned.
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
