package io.hustler.wallzy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.FcmConstants;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqSendAdminNotifications;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.TextUtils;


public class AdminNotificationFragment extends Fragment {


    @BindView(R.id.daily_notif_head)
    TextView dailyNotifHead;
    @BindView(R.id.daily_notifs_head)
    EditText dailyNotifsHead;
    @BindView(R.id.daily_notifs_message)
    EditText dailyNotifsMessage;
    @BindView(R.id.send_daily_notifs)
    Button sendDailyNotifsBtn;
    @BindView(R.id.daily_notifs_layout)
    RelativeLayout dailyNotifsLayout;
    @BindView(R.id.Announcement_notif_head)
    TextView AnnouncementNotifHead;
    @BindView(R.id.Announcement_notifs_head)
    EditText AnnouncementNotifsHead;
    @BindView(R.id.Announcement_notifs_message)
    EditText AnnouncementNotifsMessage;
    @BindView(R.id.send_Announcement_notifs)
    Button sendAnnouncementNotifsBtn;
    @BindView(R.id.Announcement_notifs_layout)
    RelativeLayout AnnouncementNotifsLayout;
    @BindView(R.id.Update_notif_head)
    TextView UpdateNotifHead;
    @BindView(R.id.Update_notifs_head)
    EditText UpdateNotifsHead;
    @BindView(R.id.Update_notifs_message)
    EditText UpdateNotifsMessage;
    @BindView(R.id.send_Update_notifs)
    Button sendUpdateNotifsBtn;
    @BindView(R.id.Update_notifs_layout)
    RelativeLayout UpdateNotifsLayout;
    @BindView(R.id.Wishes_notif_head)
    TextView WishesNotifHead;
    @BindView(R.id.Wishes_notifs_head)
    EditText WishesNotifsHead;
    @BindView(R.id.Wishes_notifs_message)
    EditText WishesNotifsMessage;
    @BindView(R.id.send_Wishes_notifs)
    Button sendWishesNotifsBtn;
    @BindView(R.id.Wishes_notifs_layout)
    RelativeLayout WishesNotifsLayout;
    @BindView(R.id.root)
    LinearLayout root;

    public AdminNotificationFragment() {
        // Required empty public constructor
    }


    public static AdminNotificationFragment newInstance() {
        return new AdminNotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_notification, container, false);
        ButterKnife.bind(this, view);
        TextUtils.findText_and_applyTypeface(root, Objects.requireNonNull(getActivity()));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnClick({R.id.send_daily_notifs, R.id.send_Announcement_notifs, R.id.send_Update_notifs, R.id.send_Wishes_notifs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_daily_notifs:
                sendNotification(FcmConstants.DAILY_NOTIFICATIONS_TOPIC_TYPE,
                        dailyNotifsHead.getText().toString(),
                        dailyNotifsMessage.getText().toString(),
                        null);
                break;
            case R.id.send_Announcement_notifs:
                sendNotification(FcmConstants.ANNOUNCEMENTS_TOPIC_TYPE,
                        AnnouncementNotifsHead.getText().toString(),
                        AnnouncementNotifsMessage.getText().toString(),
                        null);
                break;
            case R.id.send_Update_notifs:
                sendNotification(FcmConstants.UPDATES_TOPIC_TYPE,
                        UpdateNotifsHead.getText().toString(),
                        UpdateNotifsMessage.getText().toString(),
                        null);
                break;
            case R.id.send_Wishes_notifs:
                sendNotification(FcmConstants.ANNOUNCEMENTS_TOPIC_TYPE,
                        WishesNotifsHead.getText().toString(),
                        WishesNotifsMessage.getText().toString(),
                        null);
                break;
        }
    }

    private void sendNotification(String type, String title, String message, String image) {
        ReqSendAdminNotifications reqSendAdminNotifications = new ReqSendAdminNotifications();
        reqSendAdminNotifications.setType(type);
        reqSendAdminNotifications.setTitle(title);
        reqSendAdminNotifications.setMessage(message);
        if (null != image) {
            reqSendAdminNotifications.setImage(image);
        }
        callNotificationApi(reqSendAdminNotifications);
    }


    private void callNotificationApi(ReqSendAdminNotifications reqSendAdminNotifications) {
        new RestUtilities().sendAdminNotifications(getContext(), reqSendAdminNotifications, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.isApiSuccess()) {
                    MessageUtils.showShortToast(getActivity(), baseResponse.getMessage());
                } else {
                    MessageUtils.showShortToast(getActivity(), baseResponse.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                MessageUtils.showShortToast(getActivity(), error);
            }
        });
    }
}
