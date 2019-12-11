package io.hustler.wallzy.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.utils.BackGroundServiceUtils;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.daily_wall_switch)
    Switch dailyWallSwitch;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.TimeGap)
    TextView TimeGap;
    @BindView(R.id.TimeGap_message)
    TextView TimeGapMessage;
    @BindView(R.id.TimeGap_value)
    TextView TimeGapValue;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.wallCat)
    TextView wallCat;
    @BindView(R.id.wall_cat_message)
    TextView wallCatMessage;
    @BindView(R.id.wallcat_val)
    TextView wallcatVal;
    @BindView(R.id.layout3)
    RelativeLayout layout3;
    @BindView(R.id.onWifi_tv)
    TextView onWifiTv;
    @BindView(R.id.on_wifi_tv_message)
    TextView onWifiTvMessage;
    @BindView(R.id.on_wifi_tv_checkbox)
    CheckBox onWifiTvCheckbox;
    @BindView(R.id.layout4)
    RelativeLayout layout4;
    SharedPrefsUtils sharedPrefsUtils;
    boolean isEnabled, isWifiEnabled;
    String dailyCat;
    int dailyTimegap;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.space1)
    View space1;
    @BindView(R.id.Daily_notification_title)
    TextView DailyNotificationTitle;
    @BindView(R.id.Daily_notification_message)
    TextView DailyNotificationMessage;
    @BindView(R.id.daily_notification_switch)
    Switch dailyNotificationSwitch;
    @BindView(R.id.layout5)
    RelativeLayout layout5;
    int isDailyNotifsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        setStatusBar();
        sharedPrefsUtils = new SharedPrefsUtils(SettingsActivity.this);
        isEnabled = sharedPrefsUtils.getBoolean(WallZyConstants.SHARED_PREFS_DAILE_WALLS_ENABLED);
        isWifiEnabled = sharedPrefsUtils.getBoolean(WallZyConstants.SHARED_PREFS_DAILE_WALLS_WIFI_ENABLED);
        dailyCat = sharedPrefsUtils.getString(WallZyConstants.SHARED_PREFS_DAILE_WALLS_CAT);
        dailyTimegap = sharedPrefsUtils.getInt(WallZyConstants.SHARED_PREFS_DAILE_WALLS_TIMEGAP);
        isDailyNotifsEnabled = sharedPrefsUtils.getInt(WallZyConstants.SHARED_PREFS_DAILY_NOTIFS_ENABLED);
        TextUtils.findText_and_applyTypeface(root, SettingsActivity.this);

        dailyWallSwitch.setChecked(isEnabled);

        if (isEnabled) {
            changeAutoWallpaperViewStates(true, 1f);
        } else {
            changeAutoWallpaperViewStates(false, 0.3f);
        }

        if (isDailyNotifsEnabled == 1) {
            changeDailyNotifsViewStates(true, 1f);
        } else {
            changeDailyNotifsViewStates(true, 0.3f);
            dailyNotificationSwitch.setChecked(false);

        }
    }

    private void setLightStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setStatusBar() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is not active, we're in day time
                setLightStatusbar();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is active, we're at night!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                    flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                    this.getWindow().getDecorView().setSystemUiVisibility(flags);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                    //Log.i(TAG, "setStatusBar: NightMode Found");


                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                    this.getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_b));
                    toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_b));
                    toolbar.setTitleTextColor(Color.WHITE);
                }
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                setLightStatusbar();
                break;
        }
    }


    private void changeAutoWallpaperViewStates(boolean isClickable, float v) {
        layout2.setClickable(isClickable);
        layout3.setClickable((isClickable));
        layout4.setClickable((isClickable));
        onWifiTvCheckbox.setEnabled(isClickable);

        layout2.setAlpha(v);
        layout3.setAlpha(v);
        layout4.setAlpha(v);
    }

    private void changeDailyNotifsViewStates(boolean isClickale, float alpha) {
        space1.setAlpha(alpha);
        layout5.setAlpha(alpha);
        layout5.setClickable(isClickale);
        dailyNotificationSwitch.setChecked(isClickale);

    }

    @OnClick({R.id.daily_wall_switch, R.id.layout2, R.id.layout3, R.id.on_wifi_tv_checkbox, R.id.daily_notification_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.daily_wall_switch:
                sharedPrefsUtils.putBoolean(WallZyConstants.SHARED_PREFS_DAILE_WALLS_ENABLED, dailyWallSwitch.isChecked());
                if (dailyWallSwitch.isChecked()) {
                    changeAutoWallpaperViewStates(true, 1f);
                } else {
                    changeAutoWallpaperViewStates(false, 0.3f);
                }
                break;
            case R.id.daily_notification_switch:
                sharedPrefsUtils.putInt(WallZyConstants.SHARED_PREFS_DAILY_NOTIFS_ENABLED, dailyNotificationSwitch.isChecked() ? 1 : 0);
                BackGroundServiceUtils.DailyNotifications dailyNotifications = new BackGroundServiceUtils.DailyNotifications();

                if (dailyNotificationSwitch.isChecked()) {
                    changeDailyNotifsViewStates(true, 1f);
                    dailyNotifications.startMorningAlarm(getApplicationContext());
                    dailyNotifications.startEveningAlarm(getApplicationContext());
                } else {
                    changeDailyNotifsViewStates(false, 0.3f);
                    dailyNotifications.cancelMorningAlarm(getApplicationContext(), true);
                    dailyNotifications.cancelMorningAlarm(getApplicationContext(), false);
                }
                break;
            case R.id.layout2:
                MessageUtils.showShortToast(SettingsActivity.this, "Clicked");
                break;
            case R.id.layout3:
                MessageUtils.showShortToast(SettingsActivity.this, "Clicked");

                break;
            case R.id.on_wifi_tv_checkbox:
                sharedPrefsUtils.putBoolean(WallZyConstants.SHARED_PREFS_DAILE_WALLS_WIFI_ENABLED, onWifiTvCheckbox.isChecked());
                onWifiTvCheckbox.setChecked(sharedPrefsUtils.getBoolean(WallZyConstants.SHARED_PREFS_DAILE_WALLS_WIFI_ENABLED));
                break;
        }
    }
}
