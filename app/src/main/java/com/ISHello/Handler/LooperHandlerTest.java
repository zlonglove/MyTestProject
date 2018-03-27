package com.ISHello.Handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LooperHandlerTest {
    private final String TAG = "LooperHandlerTest";
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;

    public LooperHandlerTest() {

        LooperThread looperThread = new LooperThread("zl_looper_thread");
        looperThread.start();

        mServiceLooper = looperThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public void sendMessage(int what, Object object) {
        Message msg = mServiceHandler.obtainMessage();
        msg.what = what;
        msg.obj = object;
        mServiceHandler.sendMessage(msg);
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "--->message type==" + msg.what);
            switch (msg.what) {
                case 100:
                    doThings();
                    break;
                case 200:
                    doThings2();
                    break;
                default:
                    break;
            }
        }
    }

    private void doThings() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "--->things1 " + i);
        }
    }

    private void doThings2() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "--->things2 " + i);
        }
    }
}
