package com.ISHello.RemoteCalls;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author zhanglong
 */
public class RemoteService extends Service {

    private PersonImpl binder = new PersonImpl();
    private final String TAG = "RemoteService";

    @Override
    public void onCreate() {
        Log.i(TAG, "--->onCreate()");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "--->onBind()");
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "--->onDestroy()");
        super.onDestroy();
    }


    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        Log.i(TAG, "--->onLowMemory()");
        super.onLowMemory();
    }


}
