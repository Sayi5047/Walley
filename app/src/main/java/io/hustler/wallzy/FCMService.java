package io.hustler.wallzy;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: " + remoteMessage.toString());
        NotificationUtils notificationUtils = new NotificationUtils();
        notificationUtils.createNotification(getApplicationContext(),
                "Testing Title",
                "Testing Message",
                NotificationConstants.getCloudNotificationAnnouncementChannelId(),
                NotificationConstants.getCloudNotificationGroupId()
                , new Random(5400).nextInt());


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
}
