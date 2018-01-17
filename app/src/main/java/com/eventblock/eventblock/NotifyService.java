package com.eventblock.eventblock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

/**
 * Created by oscar on 15/01/18.
 */

public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), LoginActivity.class);

        intent1.putExtra("receivedTokens", true);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification.Builder mNotify = new Notification.Builder(this)
                .setContentTitle("Token bonus!")
                .setContentText("You can now receive your daily token bonus!")
                .setSmallIcon(R.drawable.event)
                .setContentIntent(pIntent)
                .setSound(sound)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL)
                //.addAction(0, "Login now", pIntent)
                .setAutoCancel(true);
        //int flags = Notification.FLAG_AUTO_CANCEL;
        //mNotify.build().flags = flags;
        if (mNM != null) {
            mNM.notify(1,mNotify.build());
        }

    }
}