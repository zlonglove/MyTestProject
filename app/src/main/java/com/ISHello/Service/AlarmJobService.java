package com.ISHello.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;


/**
 * 代替AlarmManager 完成5分钟一次的心跳工作
 *
 * @author kfzx-zhangsl
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmJobService extends JobService {

    private static final int ALARM_START = 999;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("zl", "AlarmJobService msg.what:" + msg.what + "");
            switch (msg.what) {
                case ALARM_START:

                    /*try {
                        IMApp.getInstance().getMyAidlInterface().tryConnect();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Intent intentBind = new Intent(NotifyReceiver.ACTION_NOTIFY);
                    BroadcastHelper.notifyEvent(NotifyReceiver.NOTIFY_TYPE_REBIND, intentBind);*/
                    JobSchedulerManager.createAlarmJobInfo2start(AlarmJobService.this);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e("zl", "AlarmJobService onStartJob");
        if (mHandler == null) {
            Log.e("zl", "mHandler is null");
            return false;
        }
        Message message = new Message();
        message.what = ALARM_START;
        mHandler.sendMessage(message);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.e("zl", "AlarmJobService onStopJob");
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("zl", "AlarmJobService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}