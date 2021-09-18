package com.example.chalkboard_copy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Date;

//import android.arch.persistence.room.Room;
//import android.support.v4.app.NotificationCompat;

public class NotifierAlarm extends BroadcastReceiver {

   // private AppDatabase appDatabase;
   private static final String CHANNEL_ID = "CHANNEL_SAMPLE";
    @Override
    public void onReceive(Context context, Intent intent) {

      //  appDatabase = AppDatabase.geAppdatabase(context.getApplicationContext());
     //   RoomDAO roomDAO = appDatabase.getRoomDAO();
        ReminderClass reminder = new ReminderClass();
        reminder.setMessage(intent.getStringExtra("Message"));
        reminder.setRemindDate(new Date(intent.getStringExtra("RemindDate")));
        reminder.setId(intent.getIntExtra("id",0));
        int notificationId = intent.getIntExtra("id",0);
        String message = intent.getStringExtra("Message");
       // roomDAO.Delete(reminder);
      //  AppDatabase.destroyInstance();

        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context,NotificationsFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1, 0);

      /*  TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(NotificationsFragment.class);
        taskStackBuilder.addNextIntent(intent1);*/

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            CharSequence channelName = "My Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Prepare Notification
        Uri alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.relativity)
                .setContentTitle("You set a reminder!")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis());



        // Notify
        notificationManager.notify(notificationId, builder.build());
    }
}
