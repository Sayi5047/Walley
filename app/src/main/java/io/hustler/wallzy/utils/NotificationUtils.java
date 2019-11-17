package io.hustler.wallzy.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.HomeActivity;
import io.hustler.wallzy.constants.NotificationConstants;

public class NotificationUtils {

    public final String TAG = this.getClass().getSimpleName();
    private static NotificationUtils notificationUtils;

    private NotificationUtils() {

    }

    public static NotificationUtils getInstance() {
        if (null != notificationUtils) {
            return notificationUtils;
        } else {
            notificationUtils = new NotificationUtils();
            return notificationUtils;
        }
    }

    /**
     * Method that creates all notification channel Groups at once
     *
     * @param context context to get the system service can be activity
     * @see NotificationConstants fot more Details of Constants
     */
    public void createAllNotificationGroups(Context context) {
        List<NotificationChannelGroup> notificationChannelGroups = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ArrayList<String> notificationGroupIdsList = NotificationConstants.getAllGroupId();
            ArrayList<String> notificationGroupNamesList = NotificationConstants.getAllGroupNames();
            notificationChannelGroups.add(new NotificationChannelGroup(notificationGroupIdsList.get(0), notificationGroupNamesList.get(0)));
            notificationChannelGroups.add(new NotificationChannelGroup(notificationGroupIdsList.get(1), notificationGroupNamesList.get(1)));
            notificationChannelGroups.add(new NotificationChannelGroup(notificationGroupIdsList.get(2), notificationGroupNamesList.get(2)));
            notificationChannelGroups.add(new NotificationChannelGroup(notificationGroupIdsList.get(3), notificationGroupNamesList.get(3)));
            notificationChannelGroups.add(new NotificationChannelGroup(notificationGroupIdsList.get(4), notificationGroupNamesList.get(4)));
            getNotificationManager(context).createNotificationChannelGroups(notificationChannelGroups);
        }
    }

    /**
     * Creates all Notifications Channels at once on app start
     *
     * @param context context to get the system service can be activity
     */
    public void createAllNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            ArrayList<String> notificationGroupIdsList = NotificationConstants.getAllGroupId();
            List<String> notificationChannelIdList = NotificationConstants.getAllChannelIds();
            List<String> notificationChannelNamesList = NotificationConstants.getAllChannelNames();
            for (int i = 0; i < notificationChannelIdList.size(); i++) {
                createSingleNotificationChannel(notificationChannelIdList.get(i),
                        notificationChannelNamesList.get(i),
                        context,
                        notificationGroupIdsList.get(i),
                        NotificationManager.IMPORTANCE_DEFAULT);
            }

        }
    }


    /**
     * Method that Create the Notification channel Groups . That Groups the similar kind of Notifications
     *
     * @param context   context to get the system service can be activity
     * @param groupId   Group unique id as string to identify the specific group to post notification Channels
     * @param groupName Group unique name as string to identify the specific group to post notification Channels
     * @see NotificationUtils for the  for the GROUP ID & GROUP NAME constants
     */
    public void createSingleNotificationGroup(String groupId, String groupName, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            getNotificationManager(context).createNotificationChannelGroup(new NotificationChannelGroup(groupId, groupName));


    }

    /**
     * Method to Create the Notification Channels to show different notification in different channels
     *
     * @param groupId     This Param tells us to merge the current channel in the given group
     * @param context     context to get the system service can be activity
     * @param channelId   Channel unique id as string to identify the specific group to post notifications
     * @param channelName Channel unique name as string to identify the specific group to post notifications
     * @see NotificationUtils for the CHANNEL ID & CHANNEL NAME constants
     */
    private void createSingleNotificationChannel(String channelId, String channelName, Context context, String groupId, int importance) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 600});
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setGroup(groupId);
            getNotificationManager(context).createNotificationChannel(notificationChannel);
        }


    }

    /**
     * Method to create Notification on specific Group and Channel
     *
     * @param groupKey       This Param tells us to merge the current channel in the given group
     * @param context        context to get the system service can be activity
     * @param channelId      Channel unique id as string to identify the specific group to post notifications
     * @param title          Notification Title Message
     * @param message        Notification Message Body
     * @param notificationId Unique Notification identifier for Notifications.
     */
    public void createSimpleNotification(Context context,
                                         String title,
                                         String message,
                                         String channelId,
                                         String groupKey,
                                         int notificationId,
                                         int groupNotificationId) {
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NotificationConstants.getDailyNotificationHomePendingIntentRequestCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channelId).setGroup(groupKey);
        }
        createGroupNotification(context, title, message, channelId, groupKey, groupNotificationId);
        getNotificationManager(context).notify(groupKey, notificationId, notificationBuilder.build());


    }

    public void createSingleImageNotification(Context context, String title, String message,
                                              String channelId, String groupKey,
                                              int notificationId, Bitmap bitmap) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NotificationConstants.getDailyNotificationHomePendingIntentRequestCode(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(bitmap)
                .setStyle(new Notification.BigPictureStyle().bigPicture(bitmap));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channelId).setGroup(groupKey);
        }
        createGroupNotification(context, title, message, channelId, groupKey, notificationId);
        getNotificationManager(context).notify(groupKey, notificationId, notificationBuilder.build());


    }

    private void createGroupNotification(Context context, String title, String message, String channelId, String groupId,
                                         int groupNotificationId) {

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder.setGroup(groupId).setGroupSummary(true).setChannelId(channelId);
        }
        getNotificationManager(context).notify(groupNotificationId, notificationBuilder.build());
    }

    public NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void clearAll(Context context) {
        getNotificationManager(context).cancelAll();
    }

    /**
     * Delete a Notification Channel
     *
     * @param context   context to get the system service can be activity
     * @param channelId Channel unique id as string to identify the specific channel to delete Notification channel.
     */
    private void deleteNotificationChannel(String channelId, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            getNotificationManager(context).deleteNotificationChannel(channelId);

    }

    /**
     * Method to navigate user to specific notification Channel
     *
     * @param context               context to get the system service can be activity
     * @param notificationChannelId Channel unique id as string to identify the specific channel to delete Notification channel.
     */
    private void goToNotificationSettings(String notificationChannelId, Context context) {
        Intent intent = null;
        NotificationChannel notificationChannel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            notificationChannel = getNotificationManager(context).getNotificationChannel(notificationChannelId);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannel.getId());
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            context.startActivity(intent);
        } else {
            Log.i(TAG, "goToNotificationSettings: CANT OPEN SETTINGS DUE TO LOW OS VERSION");
        }

    }
}
