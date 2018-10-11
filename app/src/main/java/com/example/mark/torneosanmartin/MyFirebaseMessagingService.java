package com.example.mark.torneosanmartin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TituloMensaje;
    private String BodyMensaje;
    private String DataMensaje;
    private static String TAG="serviceMensaje";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationtitle = "";
        String notificationtext = "";
        int id=(int)System.currentTimeMillis();
        try{
            notificationtitle= remoteMessage.getData().get("hello");
            notificationtext = remoteMessage.getData().get("codigo");

        }catch (NullPointerException e){
            Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
        }
        Log.d(TAG, "onMessageReceived: data: " + notificationtitle);
        Log.d(TAG, "onMessageReceived: notification body: " + notificationtext);


        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"com.example.mark.torneosanmartin.ANDROID");
        Intent intent=new Intent(this,PanelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pending=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.InboxStyle inboxStyle= new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("grupo de notificaciones");
        builder.setSmallIcon(R.mipmap.ic_notification_football)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                .setContentText(notificationtitle)
                .setContentTitle(notificationtext)
                .setAutoCancel(true)
                .setGroup("futbol")
                .setGroupSummary(true)
                .setStyle(inboxStyle)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.mundial)))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.mundial));
        builder.setContentIntent(pending);
        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(1,builder.build());
    }





    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
