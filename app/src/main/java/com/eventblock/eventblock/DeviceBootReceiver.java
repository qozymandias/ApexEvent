package com.eventblock.eventblock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by oscar on 19/01/18.
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {


            /* Setting the alarm here */
            Intent myIntent = new Intent(context, NotifyService.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();


            calendar.set(Calendar.SECOND, currentCal.get(Calendar.SECOND));
            calendar.set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE) + 1);
            calendar.set(Calendar.HOUR, currentCal.get(Calendar.HOUR));
            calendar.set(Calendar.AM_PM, currentCal.get(Calendar.AM_PM));

            long intendedTime = calendar.getTimeInMillis();
            long currentTime = currentCal.getTimeInMillis();

            if(intendedTime >= currentTime){
                // you can add buffer time too here to ignore some small differences in milliseconds
                // set from today
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

            } else{
                // set from next day
                // you might consider using calendar.add() for adding one day to the current day
                //calendar.add(Calendar.DAY_OF_MONTH, 1);
                intendedTime = calendar.getTimeInMillis();
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime,
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
            }
            Toast.makeText(context, "Starting Alarm", Toast.LENGTH_LONG).show();

        /*
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
        }*/
        }
    }
}