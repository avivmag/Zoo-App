package com.zoovisitors.cl.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.pl.LoadingScreen;
import com.zoovisitors.pl.MainActivity;

/**
 * Created by Gili on 21/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager notifManager;

    public static void sendSelfNotification(Class onClickGo, String headline, String message){
        Intent intent = new Intent(GlobalVariables.appCompatActivity, onClickGo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(GlobalVariables.appCompatActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(GlobalVariables.appCompatActivity);
        notificationBuilder.setContentTitle(headline);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        //set the sound of the notification
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setSmallIcon(R.mipmap.logo);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) GlobalVariables.appCompatActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            final int NOTIFY_ID = 1002;

            // There are hardcoding only for show it's just strings
            String name = "my_package_channel";
            String id = "my_package_channel_1"; // The user-visible name of the channel.
            String description = "my_package_first_channel"; // The user-visible description of the channel.

            Intent intent;
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;

            if (notifManager == null) {
                notifManager =
                        (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(this, id);

                intent = new Intent(this, LoadingScreen.class);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                builder.setContentTitle(remoteMessage.getData().get("Title"))  // required
                        .setSmallIcon(R.mipmap.logo) // required
                        .setContentText(remoteMessage.getData().get("Body"))  // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.orangeNegev))
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {

                builder = new NotificationCompat.Builder(this);

                intent = new Intent(this, LoadingScreen.class);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                builder.setContentTitle(remoteMessage.getData().get("Title"))                           // required
                        .setSmallIcon(R.mipmap.logo) // required
                        .setContentText(remoteMessage.getData().get("Body"))  // required
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setColor(getResources().getColor(R.color.orangeNegev))
                        .setLights(getResources().getColor(R.color.orangeNegev), 2000, 1000)
                        .setPriority(Notification.PRIORITY_HIGH);
            } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);
        }
    }
