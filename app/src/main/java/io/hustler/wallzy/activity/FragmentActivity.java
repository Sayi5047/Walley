package io.hustler.wallzy.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.fragments.ProfileFragment;
import io.hustler.wallzy.utils.TextUtils;

public class FragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    FragmentManager supportFragentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        supportFragentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragentManager.beginTransaction();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        TextUtils.findText_and_applyTypeface(root, FragmentActivity.this);
        getIntentData();
        setStatusBar();
    }

    private void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setStatusBar() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is not active, we're in day time
                setLightStatusBar();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is active, we're at night!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                    flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                    this.getWindow().getDecorView().setSystemUiVisibility(flags);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);


                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                    this.getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_b));
                    toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.bg_b));
                    toolbar.setTitleTextColor(Color.WHITE);
                }
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                setLightStatusBar();
        }
    }


    private void getIntentData() {
        Intent intent = getIntent();
        int fragmentNumber = intent.getIntExtra(WallZyConstants.INTENT_FRAGMENT_ACTIVITY_FRAGMENT_NUMBER, 0);
        switch (fragmentNumber) {
            case 0: {
                callProfileFragment();
            }
            break;
        }

    }

    private void callProfileFragment() {
        fragmentTransaction.add(R.id.fragment_frame, ProfileFragment.getInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
