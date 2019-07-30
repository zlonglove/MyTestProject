package com.ISHello.AndroidThread;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kfzx-zhanglong on 2016/4/26.
 * Company ICBC
 * <p>
 * HandlerThread+Handler构建成了一个带有消息循环机制的异步任务处理机制。
 * 因此开发者就可以将异步任务封装成消息的形式发送到工作线程中去执行了
 * <p>
 * 封装了HandlerThread和一个Handler
 * <p>
 * 1)IntentService创建时启动一个HandlerThread，同时将Handler绑定HandlerThread。
 * 所以通过Handler发送的消息都在HandlerThread中执行。
 * 2)然后IntentService进入生命周期onStartCommand再调用onStart将传进的Intent对象以消息的形式使用Handler发送。
 * 3)Handler收到消息后会调用onHandleIntent这样一个抽象方法，这个方法需要我们自己实现去处理逻辑。
 * 最后处理完毕stopSelf(msg.arg1);等待所有任务完成结束IntentService；
 */
public class ISIntentService extends IntentService {
    private final static String TAG = ISIntentService.class.getSimpleName();

    public ISIntentService() {
        this(TAG);
    }

    public ISIntentService(String name) {
        super(name);
        //onStartCommand()方法将返回START_REDELIVER_INTENT，onHandleIntent方法返回前进程死掉了,那么进程将会重新启动
        //intent将会重新投递
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "--->onCreate()");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("task_action");
        Log.i(TAG, "--->backTask==" + action + " Thread Name==" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
            if ("com.zlonglove.cn.download".equals(action)) {
                Log.i(TAG, "--->handle task :"+action);
                //根据action判断要执行的任务
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
    }
}
