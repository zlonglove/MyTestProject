package com.ISHello.AndroidThread;

import android.content.Context;
import android.content.Intent;

/**
 * Created by kfzx-zhanglong on 2016/4/25.
 * Company ICBC
 * AsyncTask  IntentService HandlerThread
 */
public class AndroidThread {
    public void callAsyncTask() {
        String[] array = {"www.baidu.com", "www.alibaba.com", "www.teng.com"};
        new ISAsyncTask().downLoadFile(array);
        /*new ISAsyncTask().downLoadFile(array);
        new ISAsyncTask().downLoadFile(array);*/
    }

    public void callIntentService(Context context) {
        Intent service = new Intent(context, ISIntentService.class);
        service.putExtra("task_action", "com.zlonglove.cn");
        context.startService(service);

        service.putExtra("task_action", "com.zlonglove.cn.hello");
        context.startService(service);

        service.putExtra("task_action", "com.zlonglove.cn.download");
        context.startService(service);
    }
}
