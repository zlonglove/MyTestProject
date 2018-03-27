package com.ISHello.Chat.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PushNotifyService extends Service {
    public PushNotifyService() {
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startForeground(1001, new Notification());
        this.stopForeground(true);
        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
