package com.ISHello.ExitApp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 程序完美推出程序 Activity onCreate 的时候来调用 addActivity() 最后在你需要推出程序的地方调用
 * application.onTerminate() 就可以了
 *
 * @author sunpeng
 */
public class BaseApplication extends Application {
    private List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }
        onDistory();
        System.exit(0);
    }

    /**
     * 释放其他资源
     */
    private void onDistory() {

    }

}
