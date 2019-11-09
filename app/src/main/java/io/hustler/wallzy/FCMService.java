package io.hustler.wallzy;

import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import io.hustler.wallzy.constants.NotificationConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqUpdateFcmToken;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.NotificationUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;

public class FCMService extends com.google.firebase.messaging.FirebaseMessagingService {
    private final String TAG = this.getClass().getSimpleName();

    public FCMService() {
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: " + remoteMessage.toString());
        NotificationUtils notificationUtils = new NotificationUtils();
        Map<String, String> data = remoteMessage.getData();

        LocalNotificationData localNotificationData = new Gson().fromJson(new Gson().toJson(data, new TypeToken<HashMap<String, String>>() {
        }.getType()), LocalNotificationData.class);
        try {
            Bitmap bitmap;
            bitmap = Glide.with(getApplicationContext()).asBitmap().load(localNotificationData.getImage()).submit().get();
            if (null == localNotificationData.image || localNotificationData.getImage().length() <= 0) {
                notificationUtils.createSimpleNotification(getApplicationContext(),
                        localNotificationData.getTitle(),
                        localNotificationData.getMessage(),
                        NotificationConstants.getCloudNotificationAnnouncementChannelId(),
                        NotificationConstants.getCloudNotificationKey()
                        , new Random(5400).nextInt());
            } else {
                notificationUtils.createSingleImageNotification(getApplicationContext(),
                        localNotificationData.getTitle(),
                        localNotificationData.getMessage(),
                        NotificationConstants.getCloudNotificationAnnouncementChannelId(),
                        NotificationConstants.getCloudNotificationKey()
                        , new Random(5400).nextInt(), bitmap);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        ReqUpdateFcmToken reqUpdateFcmToken = new ReqUpdateFcmToken();
        reqUpdateFcmToken.setFmcToken(s);
        SharedPrefsUtils sharedPrefsUtils = new SharedPrefsUtils(getApplicationContext());
        ResLoginUser resLoginUser = sharedPrefsUtils.getUserData();
        if (null != resLoginUser) {
            reqUpdateFcmToken.setUserId(resLoginUser.getId());
            new RestUtilities().update_fcm_token(getApplicationContext(), reqUpdateFcmToken, new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                    if (baseResponse.isApiSuccess()) {
                        Log.i(TAG, "onSuccess: UPDATE FCM TOKEN");
                        sharedPrefsUtils.putString(WallZyConstants.SHARED_PREFS_USER_FCM_TOKEN, s);
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i(TAG, "onFailure: UPDATE FCM TOKEN " + error);

                }
            });

        }

    }

    public static class LocalNotificationData {

        String title, message, launchActivity, image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLaunchActivity() {
            return launchActivity;
        }

        public void setLaunchActivity(String launchActivity) {
            this.launchActivity = launchActivity;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
