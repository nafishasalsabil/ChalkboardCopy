package com.example.chalkboard_copy.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AppNotifications extends ContextWrapper {

    public static final String CHANNEL_ID = "com.example.chalkboard_copy";
    public static final String CHANNEL_NAME = "chalkborad";

    NotificationManager notificationManager;
    public AppNotifications(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        notificationManager.createNotificationChannel(notificationChannel);
    }
    public NotificationManager getManager()
    {
        if(notificationManager==null)
        {
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    @TargetApi(Build.VERSION_CODES.O)

    public NotificationCompat.Builder getAppNotifications(String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon)
    {
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(Integer.parseInt(icon))
                .setSound(soundUri)
                .setAutoCancel(true);
    }
}
