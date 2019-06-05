package com.ISHello.Service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


/**
 * @author
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class JobSchedulerManager {

    private static int mMinGroupJobId = 4 * 10;

    public static void createAlarmJobInfo2start(Context context) {
        Log.e("zl", "JobSchedulerManager createAlarmJobInfo2start");
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = createAlarmJobInfo(context);
        if (jobScheduler != null && jobScheduler.schedule(jobInfo) < 0) {
            Log.e("zl", "JobSchedulerManager jobScheduler error");
        }
    }

    public static JobInfo createAlarmJobInfo(Context context) {
        Log.e("zl", "JobSchedulerManager createAlarmJobInfo");
        JobInfo.Builder builder;
        // TODO: 2019/2/18 xx 5.0 6.0 jobInfo 允许设置定时间隔的任务模式
        // TODO: 2019/2/18 7.0+ jobInfo 不允许设置定时间隔任务模式 需要设置最小执行时间 并重新创建jobinfo再次执行 达到定时执行的模式
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder = new JobInfo.Builder(1, new ComponentName(context, AlarmJobService.class));
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPeriodic(10 * 1000);
        } else {
            builder = new JobInfo.Builder(1, new ComponentName(context, AlarmJobService.class));
            //设置网络类型
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    //设置最小执行间隔
                    .setMinimumLatency(10 * 1000)
                    .setOverrideDeadline(15 * 1000);
        }
        return builder.build();
    }
}