package com.ISHello.AppManager;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.ISHello.database.SQliteManager;
import com.ISHello.databaseNew.DBHelper;

/**
 * @author kfzx-zhanglong App管理类
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {

    }

    public static synchronized AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * push,addElement
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        //activityStack.push(activity);
        activityStack.add(activity);
    }

    /**
     * 返回栈顶元素，并不删除
     *
     * @return
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 得到栈的栈顶元素,并不移除，可以使用remove进行移除 \n pop接口，弹出栈顶元素并从栈里面进行移除
     */
    public void finishActivity() {
        /**
         * activityStack.pop();删除下面函数的remove函数
         */
        //Activity activity = activityStack.lastElement();
        Activity activity = activityStack.pop();
        finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出App
     *
     * @param context
     */
    public void appExit(Context context) {
        try {
            DBHelper.getDBHelper().closeDataBase();//关闭数据库
            SQliteManager.getInstance().close();//关闭数据库
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.gc();
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
