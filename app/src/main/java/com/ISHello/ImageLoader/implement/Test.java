package com.ISHello.ImageLoader.implement;

import android.content.Context;

/**
 * Created by zhanglong on 2017/4/20.
 */

public class Test {

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        //config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
