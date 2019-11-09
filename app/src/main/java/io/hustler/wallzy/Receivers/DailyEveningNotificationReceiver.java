package io.hustler.wallzy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

import io.hustler.wallzy.constants.NotificationConstants;
import io.hustler.wallzy.utils.BackGroundServiceUtils;
import io.hustler.wallzy.utils.NotificationUtils;

public class DailyEveningNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == intent) {
            Toast.makeText(context, "Evening Alarm Triggered", Toast.LENGTH_SHORT).show();
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.createSimpleNotification(context, "Good Evening",
                    "Lets Refresh Your Home Screen With New Wallpapers",
                    NotificationConstants.getLocalNotificationDailyChannelId(),
                    NotificationConstants.getLocalNotificationDailyGroupId(),
                    NotificationConstants.getDailyNotificationEveningId());
        } else if (Objects.requireNonNull(intent.getAction()).equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            BackGroundServiceUtils.DailyNotifications dailyNotifications = new BackGroundServiceUtils.DailyNotifications();
            dailyNotifications.startEveningAlarm(context);
        }

    }
}
