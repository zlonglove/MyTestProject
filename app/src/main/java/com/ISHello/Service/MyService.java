package com.ISHello.Service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private final String TAG = "MyService";
    private int count;

    private boolean quit;

    private MyBinder binder = new MyBinder();

    // 新建一个Binder对象用于提供给客户端
    public class MyBinder extends Binder {
        public int getCount() {
            return MyService.this.getCount();
        }
    }

    private synchronized int getCount() {
        return count;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "--->onBind()-----");
        // 返回给客户端一个Binder对象
        return binder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "--->onCreate()-----");

        // 启动一条线程修改成员变量属性
        new Thread() {
            @Override
            public void run() {
                while (!quit) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    count++;
                }
            }

        }.start();

    }

    @Override
    public void onDestroy() {
        this.quit = true;
        Log.i(TAG, "--->onDestroy()-----");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "--->onUnbind()-----");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "--->onStartCommand()----");
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }


}