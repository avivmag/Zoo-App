package com.zoovisitors.bl;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.pl.MainActivity;
import com.zoovisitors.pl.map.MapActivity;
import com.zoovisitors.pl.schedule.ScheduleActivity;

/**
 * Created by Gili on 21/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Negev Zoo");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        //set the sound of the notification
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setSmallIcon(R.mipmap.logo);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

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
}
