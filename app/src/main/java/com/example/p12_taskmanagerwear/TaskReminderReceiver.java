package com.example.p12_taskmanagerwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

public class TaskReminderReceiver extends BroadcastReceiver {

    int notifReqCode = 123;

    @Override
    public void onReceive(Context context, Intent i) {

        int id = i.getIntExtra("id", -1);
        String name = i.getStringExtra("name");
        String desc = i.getStringExtra("desc");

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, notifReqCode,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // TOP - action 1
        NotificationCompat.Action action = new
                NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Open on the phone",
                pIntent).build();

        NotificationCompat.WearableExtender extender = new
                NotificationCompat.WearableExtender();

        //action 2
        Intent intentreply = new Intent(context,
                MainActivity.class);
        intentreply.putExtra("id", i.getIntExtra("id", 0));
        PendingIntent pendingIntentReply = PendingIntent.getActivity
                (context, 0, intentreply,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput ri = new RemoteInput.Builder("status")
                .setLabel("Status report")
                .setChoices(new String [] {"Completed", "Not yet"})
                .build();

        // BOTTOM
        NotificationCompat.Action action2 = new
                NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,
                "Reply",
                pendingIntentReply)
                .addRemoteInput(ri)
                .build();

        extender.addAction(action);
        extender.addAction(action2);

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle(name);
        builder.setContentText(desc);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        builder.extend(extender);

        Notification n = builder.build();

        notificationManager.notify(notifReqCode, n);
    }

}