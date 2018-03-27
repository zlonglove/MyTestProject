package com.ISHello.Service;

import android.content.Intent;
import android.util.Log;

import com.ISHello.Handler.AsyncService;

public class LooperPrintAsyncService extends AsyncService {
    private final String TAG = "LooperPrintAsyncService";

    public LooperPrintAsyncService(String ThreadName) {
        super(ThreadName);
    }

    public LooperPrintAsyncService() {
        this("LooperPrintAsyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "--->print " + i);
        }
    }

}
