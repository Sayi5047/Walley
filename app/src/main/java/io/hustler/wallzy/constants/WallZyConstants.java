package io.hustler.wallzy.constants;

import android.os.Environment;

import java.io.File;
import java.text.MessageFormat;

public class WallZyConstants {

    public static final String INTENT_CAT_NAME = "INTENT_CAT_NAME";
    public static final String INTENT_CAT_IMAGE = "INTENT_CAT_IMAGE";
    public static final String INTENT_CAT_ID = "INTENT_CAT_ID";
    public static final String INTENT_IS_CAT = "INTENT_IS_CAT";

    public static final String SHARED_PREFS_SYSTEM_AUTH_KEY = "SHARED_PREFS_SYSTEM_AUTH_KEY";
    public static final String INTENT_SERIALIZED_IMAGE = "INTENT_SERIALISED_IMAGE_OBJECT";
    public static final String SHARED_PREFS_USER_FCM_TOKEN = "SHARED_PREFS_USER_FCM_TOKEN";
    public static final String SHARED_PREFS_GUEST_ACCOUNT ="SHARED_PREFS_GUEST_ACCOUNT" ;
    /*PROVIDER AUTORITY*/
    public static String FILEPROVIDER_AUTHORITY = "io.hustler.wallzy.fileprovider";

    /*SHARED PREFS KEYS*/
    public static final String SP_IS_NIGHT_MODEA_ACTIVATED_KEY = "SP_IS_NIGHT_MODEA_ACTIVATED_KEY";
    public static String SP_USERDATA_KEY = "SP_USER_DATA";

    /*FILE MANAGER PATHS*/
    public static final String APP_WALLPAPERS_FOLDER = MessageFormat.format("{0}{1}Wallzy", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), File.separator);

    /*SERVICE TAGS*/
    public static final String DONWLOADIMAGE_IMAGE_JOB_TAG = "DONWLOADIMAGE_IMAGE_JOB_TAG";
    public static final String SETWALLPAPER_IMAGE_TAG = "SETWALLPAPER_IMAGE_TAG";

    /*PERMISSION IDS*/
    public static final int MY_PERMISSION_REQUEST_STORAGE = 707;
    public static final int MY_PERMISSION_REQUEST_STORAGE_FOR_SETWALLPAPER = 708;
    public static final int MY_PERMISSION_REQUEST_STORAGE_FOR_DOWNLOAD_WALLPAPER = 709;


    /*INTENT KEYS*/
    public static String ImageUrl_to_download = "INTENT_IMAGE_TO_DOWNLOAD_KEY";
    public static String Image_Name_to_save_key = "INTENT_IMAGE_NAME_TO_SAVE_KEY";
    public static String is_to_setWallpaper_fromActivity = "INTENT_IS_TO_SET_WALLPAPER_KEY";
}
