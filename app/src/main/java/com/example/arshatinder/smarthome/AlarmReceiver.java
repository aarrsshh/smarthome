package com.example.arshatinder.smarthome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Atinder on 08-07-2017.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;
    Ringtone ringTone;

    @Override
    public void onReceive(Context context, Intent arg1) {
        Intent myIntent = new Intent(context, event.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("Smart Home Notification!")
                .setContentText("One of your Event has just Started. Click here to know Event!!")
                .setTicker("Notification!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("One of your Event has just Started. Click here to know Event!!"))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
        ringTone = RingtoneManager
                .getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        ringTone.play();
    }
}