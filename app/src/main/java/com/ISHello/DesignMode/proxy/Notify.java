package com.ISHello.DesignMode.proxy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

public abstract class Notify {
    protected Context context;
    protected NotificationManager notificationManager;
    protected NotificationCompat.Builder builder;

    public Notify(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher)
                // 设置通知栏能否被清楚，true不能被清除，false可以被清除
                .setOngoing(false)
                .setChannelId(context.getPackageName())
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * 发送一条通知
     */
    public abstract void send();

    /**
     * 取消一条通知
     */
    public abstract void cancel();
}
