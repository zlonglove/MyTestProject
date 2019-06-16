package com.ISHello.DesignMode.proxy;

import android.app.Notification;
import android.content.Context;

/**
 * 大视图的通知的构建
 */
public class NotifyBig extends Notify {
    public NotifyBig (Context context) {
        super(context);
    }
    @Override
    public void send() {
        //builder.setContent(new RemoteViews(context.getPackageName(), R.layout.item_dialog_menu));
        //builder.setCustomBigContentView(new RemoteViews(context.getPackageName(), R.layout.item_dialog_menu));
        Notification notification = builder.build();
        notificationManager.notify(0, notification);
    }

    @Override
    public void cancel() {
        notificationManager.cancel(0);
    }
}
