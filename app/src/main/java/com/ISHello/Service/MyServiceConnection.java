package com.ISHello.Service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class MyServiceConnection implements ServiceConnection {

    private final String TAG = "MyServiceConnection";
    MyService.MyBinder binder;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "---onServiceConnected()----");
        binder = (MyService.MyBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        Log.i(TAG, "---onServiceDisconnected()----");
    }

    /**
     * 获取binder对象一边获取server中的数据
     *
     * @return
     */
    public MyService.MyBinder getBinder() {
        return binder;
    }
}
