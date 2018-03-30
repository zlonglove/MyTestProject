package zlonglove.cn.aidl.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.Iterator;

/**
 * Created by zhanglong on 2018/3/28.
 */

public class util {
    public static String getProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator iterator = mActivityManager.getRunningAppProcesses().iterator();
        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!iterator.hasNext()) {
                return null;
            }
            appProcess = (ActivityManager.RunningAppProcessInfo) iterator.next();
        } while (appProcess.pid != pid);
        return appProcess.processName;
    }
}
