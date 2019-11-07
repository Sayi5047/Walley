package io.hustler.wallzy.ArComponents;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Objects;

import io.hustler.wallzy.R;


public class BaseWorker extends Worker {
    private static final String WORK_RESULT = "work_result";

    public BaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskMessage = taskData.getString("TEST");
        showNotification("WorkerManager", null == taskMessage ? "Null Received" : taskMessage);
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Job Finished").build();
        return Result.success(outputData);
    }

    private void showNotification(String taskName, String taskDescription) {

        String groupChannelId = "Group_Channel_Id";
        String groupChannelName = "Group_Channel_Name";
        String groupingBundleKey = "Grouping_bundle_key";

        String channelName = "Channel_Name";
        String channelId = "Channel_Id";

        int singleNotificationId = 5047;
        int bundleNotificationId = 5047;

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel;
        NotificationChannel groupNotificationChannel;
        /*NOTIFICATION CHANNEL*/
        assert notificationManager != null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ) {
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            groupNotificationChannel = new NotificationChannel(groupChannelId, groupChannelName, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.createNotificationChannel(groupNotificationChannel);
        }
        if (bundleNotificationId == singleNotificationId) {
            singleNotificationId = bundleNotificationId + 1;
        } else {
            singleNotificationId++;
        }
        /*BUNDLE NOTIFICATION*/
        NotificationCompat.Builder groupNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), groupChannelId);
        groupNotificationBuilder
                .setGroup(groupingBundleKey)
                .setGroupSummary(true)
                .setContentTitle("Bundle Notification... " + bundleNotificationId)
                .setContentText("Notification Group")
                .setSmallIcon(R.mipmap.ic_launcher);
        Objects.requireNonNull(notificationManager).notify(bundleNotificationId, groupNotificationBuilder.build());

        /*ADDING NOTIFICATIONS TO THE CHANNEL*/
        NotificationCompat.Builder singleNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setGroup(groupingBundleKey)
                .setGroupSummary(false)
                .setContentTitle("Single Notification... " + singleNotificationId)
                .setContentText(taskDescription)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setSmallIcon(R.mipmap.ic_launcher);
        Objects.requireNonNull(notificationManager).notify(singleNotificationId, singleNotificationBuilder.build());
        Log.i("WORKER", "showNotification: Called");

    }
}
