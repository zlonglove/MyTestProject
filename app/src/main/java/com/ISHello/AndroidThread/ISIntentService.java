package com.ISHello.AndroidThread;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kfzx-zhanglong on 2016/4/26.
 * Company ICBC
 */
public class ISIntentService extends IntentService {
    private final static String TAG = "ISIntentService";

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
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("task_action");
        Log.i(TAG, "--->backTask==" + action);
        try {
            Thread.sleep(3000);
            if ("com.zlonglove.cn.download".equals(action)) {
                Log.i(TAG, "--->mydownload Process==");
                //根据action判断要执行的任务
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
