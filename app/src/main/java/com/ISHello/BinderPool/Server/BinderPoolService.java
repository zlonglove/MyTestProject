package com.ISHello.BinderPool.Server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BinderPoolService extends Service {
    private final String TAG = BinderPoolService.class.getSimpleName();
    private Binder mBinderPool = new BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"--->onBind");
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
