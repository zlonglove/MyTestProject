package im.icbc.cn.keyboard.demo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import im.icbc.cn.keyboard.utils.safeHandle;

public class phoneStateReceiver extends BroadcastReceiver {

    private final String m_sPackageName = "im.icbc.cn.keyboard";

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        if (safeHandle.getServerState()) {
            Intent intent = new Intent(arg0, catchInfo.class);
            arg0.stopService(intent);
            safeHandle.setServerState(false);
        }
    }

    private boolean isServiceRunning(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServers = manager.getRunningServices(100);

        if (runningServers == null || runningServers.size() <= 0)
            return false;

        int len = runningServers.size();
        for (int i = 0; i < len; i++) {
            RunningServiceInfo info = runningServers.get(i);
            if (info == null)
                continue;
            String name = info.service.getPackageName();
            if (name == null)
                continue;

            if (m_sPackageName.equals(name))
                return true;

        }

        return false;
    }

}
