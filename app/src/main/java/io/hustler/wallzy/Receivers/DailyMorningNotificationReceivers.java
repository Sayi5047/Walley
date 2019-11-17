package io.hustler.wallzy.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Objects;

import io.hustler.wallzy.constants.NotificationConstants;
import io.hustler.wallzy.utils.BackGroundServiceUtils;
import io.hustler.wallzy.utils.NotificationUtils;

public class DailyMorningNotificationReceivers extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent) {
            Toast.makeText(context, "Morning Alarm Triggered", Toast.LENGTH_SHORT).show();
            NotificationUtils notificationUtils = NotificationUtils.getInstance();
            notificationUtils.createSimpleNotification(context, "Good Evening",
                    "Lets Refresh Your Home Screen With New Wallpapers",
                    NotificationConstants.getAdminDailyNotificationsChannelId(),
                    NotificationConstants.getAdminDailyNotificationsGroupId(),
                    NotificationConstants.getDailyNotificationMorningId());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && null != intent.getAction()
        ) {
            if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
                // Set the alarm here.
                BackGroundServiceUtils.DailyNotifications dailyNotifications = new BackGroundServiceUtils.DailyNotifications();
                dailyNotifications.startMorningAlarm(context);
            }
        }

    }
}

