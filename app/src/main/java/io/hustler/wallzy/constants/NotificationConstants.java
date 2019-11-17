package io.hustler.wallzy.constants;

import java.util.ArrayList;

public class NotificationConstants {

    private static final int DAILY_NOTIFICATION_MORNING_ID = 200;
    private static final int DAILY_NOTIFICATION_EVENING_ID = 200;
    private static final int DAILY_NOTIFICATION_HOME_PENDING_INTENT_REQUEST_CODE = 100;

    /*GROUP NOTIFICATION IDS*/
    private static final int TEST_NOTIFICATION_GROUP_UNIQUE_ID = 200;
    private static final int ADMIN_ANNOUNCEMENTS_NOTIFICATIONS_GROUP_UNIQUE_ID = 201;
    private static final int ADMIN_UPDATES_NOTIFICATIONS_GROUP_UNIQUE_ID = 202;
    private static final int ADMIN_DAILY_NOTIFICATIONS_GROUP_UNIQUE_ID = 203;
    private static final int DOWNLOAD_NOTIFICATIONS_GROUP_UNIQUE_ID = 204;

    /**
     * GROUP MOT KEYS
     */
    private static final String TEST_NOTIFICATION_GROUP_KEY = "io.hustler.wallzy.TEST_NOTIFICATION_GROUP_KEY";
    private static final String ADMIN_ANNOUNCEMENTS_NOTIFICATIONS_GROUP_KEY = "io.hustler.wallzy.ADMIN_ANNOUNCEMENTS_NOTIFICATIONS_GROUP_KEY";
    private static final String ADMIN_UPDATES_NOTIFICATIONS_GROUP_KEY = "io.hustler.wallzy.ADMIN_UPDATES_NOTIFICATIONS_GROUP_KEY";
    private static final String ADMIN_DAILY_NOTIFICATIONS_GROUP_KEY = "io.hustler.wallzy.ADMIN_DAILY_NOTIFICATIONS_GROUP_KEY";
    private static final String DOWNLOAD_NOTIFICATIONS_GROUP_KEY = "io.hustler.wallzy.DOWNLOAD_NOTIFICATIONS_GROUP_KEY";

    /*GROUP IDS*/
    private static final String TEST_NOTIFICATIONS_GROUP_ID = "TEST_NOTIFICATIONS_GROUP_ID";
    private static final String ADMIN_NOTIFICATION_ANNOUNCEMENT_GROUP_ID = "ADMIN_NOTIFICATION_ANNOUNCEMENT_GROUP_ID";
    private static final String ADMIN_UPDATES_NOTIFICATIONS_GROUP_ID = "ADMIN_UPDATES_NOTIFICATIONS_GROUP_ID";
    private static final String ADMIN_DAILY_NOTIFICATIONS_GROUP_ID = "ADMIN_DAILY_NOTIFICATIONS_GROUP_ID";
    private static final String DOWNLOAD_NOTIFICATIONS_GROUP_ID = "DOWNLOAD_NOTIFICATIONS_GROUP_ID";

    /*GROUP NAMES*/
    private static final String TEST_NOTIFICATIONS_GROUP_NAME = "Latest Updates Group";
    private static final String ADMIN_ANNOUNCEMENT_NOTIFICATIONS_GROUP_NAME = "Latest Announcements Group";
    private static final String ADMIN_UPDATES_NOTIFICATIONS_GROUP_NAME = "Latest Updates Group";
    private static final String ADMIN_DAILY_NOTIFICATIONS_GROUP_NAME = "Daily Local Notifications Group";
    private static final String DOWNLOAD_NOTIFICATIONS_GROUP_NAME = "Image Download Group";


    /*CHANNEL IDS*/
    private static final String TEST_NOTIFICATION_CHANNEL_ID = "TEST_NOTIFICATION_CHANNEL_ID";
    private static final String ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_ID = "ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_ID";
    private static final String ADMIN_UPDATES_NOTIFICATION_CHANNEL_ID = "ADMIN_UPDATES_NOTIFICATION_CHANNEL_ID";
    private static final String ADMIN_DAILY_NOTIFICATIONS_CHANNEL_ID = "ADMIN_DAILY_NOTIFICATIONS_CHANNEL_ID";
    private static final String DOWNLOAD_NOTIFICATIONS_CHANNEL_ID = "DOWNLOAD_NOTIFICATIONS_CHANNEL_ID";

    /*CHANNEL NAMES*/

    private static final String TEST_NOTIFICATIONS_CHANNEL_NAME = "Test Notifications Channel";
    private static final String ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_NAME = "Latest Announcements Channel";
    private static final String ADMIN_UPDATES_NOTIFICATIONS_CHANNEL_NAME = "Latest Updates Channel";
    private static final String ADMIN_DAILY_NOTIFICATIONS_CHANNEL_NAME = "Daily Local Notifications Channel";
    private static final String DOWNLOAD_NOTIFICATIONS_CHANNEL_NAME = "Image Download Channel";


    public static ArrayList<String> getAllGroupId() {
        ArrayList<String> groupIdList = new ArrayList<>();
        groupIdList.add(TEST_NOTIFICATIONS_GROUP_ID);
        groupIdList.add(ADMIN_NOTIFICATION_ANNOUNCEMENT_GROUP_ID);
        groupIdList.add(ADMIN_DAILY_NOTIFICATIONS_GROUP_ID);
        groupIdList.add(DOWNLOAD_NOTIFICATIONS_GROUP_ID);
        groupIdList.add(ADMIN_UPDATES_NOTIFICATIONS_GROUP_ID);
        return groupIdList;
    }

    public static ArrayList<String> getAllGroupNames() {

        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add(TEST_NOTIFICATIONS_GROUP_NAME);
        groupNames.add(ADMIN_ANNOUNCEMENT_NOTIFICATIONS_GROUP_NAME);
        groupNames.add(ADMIN_DAILY_NOTIFICATIONS_GROUP_NAME);
        groupNames.add(DOWNLOAD_NOTIFICATIONS_GROUP_NAME);
        groupNames.add(ADMIN_UPDATES_NOTIFICATIONS_GROUP_NAME);
        return groupNames;
    }

    public static ArrayList<String> getAllChannelIds() {
        ArrayList<String> channelIdList = new ArrayList<>();
        channelIdList.add(TEST_NOTIFICATION_CHANNEL_ID);
        channelIdList.add(ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_ID);
        channelIdList.add(ADMIN_DAILY_NOTIFICATIONS_CHANNEL_ID);
        channelIdList.add(DOWNLOAD_NOTIFICATIONS_CHANNEL_ID);
        channelIdList.add(ADMIN_UPDATES_NOTIFICATION_CHANNEL_ID);
        return channelIdList;
    }

    public static ArrayList<String> getAllChannelNames() {
        ArrayList<String> channelNamesList = new ArrayList<>();
        channelNamesList.add(TEST_NOTIFICATIONS_CHANNEL_NAME);
        channelNamesList.add(ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_NAME);
        channelNamesList.add(ADMIN_DAILY_NOTIFICATIONS_CHANNEL_NAME);
        channelNamesList.add(DOWNLOAD_NOTIFICATIONS_CHANNEL_NAME);
        channelNamesList.add(ADMIN_UPDATES_NOTIFICATIONS_CHANNEL_NAME);
        return channelNamesList;
    }

    public static ArrayList<Integer> getAllGroupUniqueIds() {
        ArrayList<Integer> channelUnqiueIdsList = new ArrayList<>();
        channelUnqiueIdsList.add(TEST_NOTIFICATION_GROUP_UNIQUE_ID);
        channelUnqiueIdsList.add(ADMIN_ANNOUNCEMENTS_NOTIFICATIONS_GROUP_UNIQUE_ID);
        channelUnqiueIdsList.add(ADMIN_UPDATES_NOTIFICATIONS_GROUP_UNIQUE_ID);
        channelUnqiueIdsList.add(ADMIN_DAILY_NOTIFICATIONS_GROUP_UNIQUE_ID);
        channelUnqiueIdsList.add(DOWNLOAD_NOTIFICATIONS_GROUP_UNIQUE_ID);
        return channelUnqiueIdsList;
    }


    /*GETTERS AND SETTERS*/

    public static String getTestNotificationGroupKey() {
        return TEST_NOTIFICATION_GROUP_KEY;
    }


    public static String getAdminDailyNotificationsGroupId() {
        return ADMIN_DAILY_NOTIFICATIONS_GROUP_ID;
    }


    public static String getAdminAnnouncementsNotificationChannelId() {
        return ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_ID;
    }

    public static String getAdminDailyNotificationsChannelId() {
        return ADMIN_DAILY_NOTIFICATIONS_CHANNEL_ID;
    }

    public static int getDailyNotificationMorningId() {
        return DAILY_NOTIFICATION_MORNING_ID;
    }

    public static int getDailyNotificationEveningId() {
        return DAILY_NOTIFICATION_EVENING_ID;
    }

    public static String getAdminAnnouncementsNotificationsGroupKey() {
        return ADMIN_ANNOUNCEMENTS_NOTIFICATIONS_GROUP_KEY;
    }

    public static String getAdminDailyNotificationsGroupKey() {
        return ADMIN_DAILY_NOTIFICATIONS_GROUP_KEY;
    }

    public static String getDownloadNotificationsGroupKey() {
        return DOWNLOAD_NOTIFICATIONS_GROUP_KEY;
    }

    public static String getTestNotificationsGroupId() {
        return TEST_NOTIFICATIONS_GROUP_ID;
    }

    public static String getAdminNotificationAnnouncementGroupId() {
        return ADMIN_NOTIFICATION_ANNOUNCEMENT_GROUP_ID;
    }

    public static String getDownloadNotificationsGroupId() {
        return DOWNLOAD_NOTIFICATIONS_GROUP_ID;
    }

    public static String getTestNotificationChannelId() {
        return TEST_NOTIFICATION_CHANNEL_ID;
    }

    public static String getDownloadNotificationsChannelId() {
        return DOWNLOAD_NOTIFICATIONS_CHANNEL_ID;
    }

    public static int getDailyNotificationHomePendingIntentRequestCode() {
        return DAILY_NOTIFICATION_HOME_PENDING_INTENT_REQUEST_CODE;
    }

    public static String getAdminUpdatesNotificationsGroupKey() {
        return ADMIN_UPDATES_NOTIFICATIONS_GROUP_KEY;
    }

    public static String getAdminUpdatesNotificationsGroupId() {
        return ADMIN_UPDATES_NOTIFICATIONS_GROUP_ID;
    }

    public static String getTestNotificationsGroupName() {
        return TEST_NOTIFICATIONS_GROUP_NAME;
    }

    public static String getAdminAnnouncementNotificationsGroupName() {
        return ADMIN_ANNOUNCEMENT_NOTIFICATIONS_GROUP_NAME;
    }

    public static String getAdminUpdatesNotificationsGroupName() {
        return ADMIN_UPDATES_NOTIFICATIONS_GROUP_NAME;
    }

    public static String getAdminDailyNotificationsGroupName() {
        return ADMIN_DAILY_NOTIFICATIONS_GROUP_NAME;
    }

    public static String getDownloadNotificationsGroupName() {
        return DOWNLOAD_NOTIFICATIONS_GROUP_NAME;
    }

    public static String getAdminUpdatesNotificationChannelId() {
        return ADMIN_UPDATES_NOTIFICATION_CHANNEL_ID;
    }

    public static String getTestNotificationsChannelName() {
        return TEST_NOTIFICATIONS_CHANNEL_NAME;
    }

    public static String getAdminAnnouncementsNotificationChannelName() {
        return ADMIN_ANNOUNCEMENTS_NOTIFICATION_CHANNEL_NAME;
    }

    public static String getAdminUpdatesNotificationsChannelName() {
        return ADMIN_UPDATES_NOTIFICATIONS_CHANNEL_NAME;
    }

    public static String getAdminDailyNotificationsChannelName() {
        return ADMIN_DAILY_NOTIFICATIONS_CHANNEL_NAME;
    }

    public static String getDownloadNotificationsChannelName() {
        return DOWNLOAD_NOTIFICATIONS_CHANNEL_NAME;
    }
}
