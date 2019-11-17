package io.hustler.wallzy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Objects;

import io.hustler.wallzy.Receivers.DailyEveningNotificationReceiver;
import io.hustler.wallzy.Receivers.DailyMorningNotificationReceivers;
import io.hustler.wallzy.constants.NotificationConstants;

public class BackGroundServiceUtils {
    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public static class DailyNotifications {
        public final String TAG = this.getClass().getSimpleName();

        public void startMorningAlarm(Context context) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, DailyMorningNotificationReceivers.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationConstants.getDailyNotificationHomePendingIntentRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 07);
            calendar.set(Calendar.MINUTE, 00);
            Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.i(TAG, "startMorningAlarm: Morning Alarm Started");
        }

        public void startEveningAlarm(Context context) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, DailyEveningNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationConstants.getDailyNotificationHomePendingIntentRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 19);
            calendar.set(Calendar.MINUTE, 00);
            Objects.requireNonNull(alarmManager).setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.i(TAG, "startMorningAlarm: Evening Alarm Started");

        }

        public void cancelMorningAlarm(Context context, boolean isMorning) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent;
            if (isMorning) {
                intent = new Intent(context, DailyMorningNotificationReceivers.class);
            } else {
                intent = new Intent(context, DailyEveningNotificationReceiver.class);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NotificationConstants.getDailyNotificationHomePendingIntentRequestCode(), intent, PendingIntent.FLAG_NO_CREATE);
            if (null != pendingIntent) {
                alarmManager.cancel(pendingIntent);
                Log.i(TAG, "Alarm cancelled and cancelMorningAlarm: " + isMorning);
            }

        }

    }
}
