package com.example.fuelisticv2seller.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.LoginSignUp.MainActivity;
import com.example.fuelisticv2seller.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFCMServices extends FirebaseMessagingService {


//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, whatToOpen, PendingIntent.FLAG_ONE_SHOT);
//
//    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//            .setContentTitle(Common.NOTI_TITLE)
//            .setContentText(Common.NOTI_CONTENT)
//            .setStyle(new NotificationCompat.BigTextStyle().bigText(Common.NOTI_CONTENT))
//            .setAutoCancel(true)
////            .setSmallIcon(R.drawable.push_icon_small)
////            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.push_icon_large))
//            .setSound(defaultSoundUri)
//            .setColor(getResources().getColor(R.color.colorPrimary))
//            .setContentIntent(pendingIntent);
//
//    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//    notificationManager.no(0 /* ID of notification */,notificationBuilder.build());


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String,String> dataRecv = remoteMessage.getData();
        if(dataRecv != null)
        {
            if(dataRecv.get(Common.NOTI_TITLE).equals("New Order"))
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Common.IS_OPEN_ACTIVITY_NEW_ORDER, true);
                Common.showNotification(this, new Random().nextInt(),
                        dataRecv.get(Common.NOTI_TITLE),
                        dataRecv.get(Common.NOTI_CONTENT),
                        intent);

            }
            else
                Common.showNotification(this, new Random().nextInt(),
                    dataRecv.get(Common.NOTI_TITLE),
                    dataRecv.get(Common.NOTI_CONTENT),
                    null);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Common.updateToken(this, s, true, false);
    }


}
