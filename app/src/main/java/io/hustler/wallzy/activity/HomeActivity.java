package io.hustler.wallzy.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.utils.SharedPrefsUtils;

public class HomeActivity extends AppCompatActivity {
    public SharedPrefsUtils mSharedPrefs;


    @BindView(R.id.signout_btn)
    Button signoutBtn;

    FirebaseAuth mFirebaseAuth;
    @BindView(R.id.night_mode_btn)
    Button nightModeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mSharedPrefs = new SharedPrefsUtils(HomeActivity.this);
    }

    private int getCurrentNightMode() {
        return getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
    }

    private void changeBetweenDayandNightMode(int currentNightMode) {
        int newNightMode;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else {
            newNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        }
        saveNightModeToPreferences(newNightMode);
        initNightMode(newNightMode);
        recreate();
    }

    private void initNightMode(int newNightMode) {
        AppCompatDelegate.setDefaultNightMode(newNightMode);
    }

    private void saveNightModeToPreferences(int newNightMode) {
        mSharedPrefs.putInt(Constants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY, newNightMode);
    }


    @OnClick({R.id.night_mode_btn, R.id.signout_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.night_mode_btn:
                int currentNightMode = getCurrentNightMode();
                changeBetweenDayandNightMode(currentNightMode);
                break;
            case R.id.signout_btn:
                FirebaseAuth.getInstance().signOut();
                new SharedPrefsUtils(getApplicationContext()).clearAllUserData();
                saveNightModeToPreferences(getCurrentNightMode());
                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                break;
        }
    }

}
