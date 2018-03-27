package com.ISHello.ISNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.example.ishelloword.R;

public class ISNotification {

    Notification localNotification;
    NotificationManager mNotificationManager;

    public ISNotification() {
        // TODO Auto-generated constructor stub
    }

    public void showNotificationNew(Context context, int icon, String title, String contentText, Class<?> toClass) {
        Notification notification = null;
        PendingIntent pIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if (pIntent == null) {
                Intent localIntent = toClass == null ? new Intent() : new Intent(context, toClass);
                pIntent = PendingIntent.getActivity(context, 0, localIntent, 0);
            }
            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(icon)
                    .setTicker("您有一条新消息,请查收")
                    .setContentTitle(title)
                    .setContentText(contentText)
                    //.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            }
        }
        NotificationManager mNM = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        try {
            mNM.notify(2, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void showNotification(Context paramContext, int icon, String title,
                                 boolean sound, boolean Vibrate, boolean led, Class<?> toClass) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) paramContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (localNotification == null) {
            localNotification = new Notification(icon, title, System.currentTimeMillis());
        }
        if (sound) {
            localNotification.defaults = (Notification.DEFAULT_SOUND | localNotification.defaults);
        }
        if (Vibrate) {
            localNotification.defaults = (Notification.DEFAULT_VIBRATE | localNotification.defaults);
        }
        if (led) {
            localNotification.ledARGB = Color.BLUE;
            localNotification.ledOffMS = 0;
            localNotification.ledOnMS = 1;
            localNotification.flags = localNotification.flags | Notification.FLAG_SHOW_LIGHTS;
        }
        /**
         * 用户单击时，会触发它所指定的PendingIntent
         */

        Intent localIntent = toClass == null ? new Intent() : new Intent(paramContext, toClass);
        PendingIntent localPendingIntent = PendingIntent.getActivity(paramContext, 0, localIntent, 0);

        /**
         * 自定义视图
         */
        localNotification.contentView = new RemoteViews(paramContext.getPackageName(), R.layout.notification);

        localNotification.contentView.setImageViewResource(R.id.status_icon, icon);
        localNotification.contentView.setTextViewText(R.id.status_text, "自定义视图");
        localNotification.contentView.setProgressBar(R.id.status_progress, 100, 50, false);
        localNotification.contentView.setOnClickPendingIntent(R.id.status_progress, localPendingIntent);
        localNotification.contentIntent = localPendingIntent;
        mNotificationManager.notify(1, localNotification);
    }
}
