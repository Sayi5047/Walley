package io.hustler.wallzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.utils.SharedPrefsUtils;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @BindView(R.id.bayaditoImage)
    ImageView bayaditoImage;
    @BindView(R.id.appName)
    TextView appName;
    @BindView(R.id.root)
    RelativeLayout root;
    public SharedPrefsUtils mSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        bayaditoImage.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.slideup));
        mAuth = FirebaseAuth.getInstance();
        Handler handler = new Handler();
        mSharedPrefs = new SharedPrefsUtils(getApplicationContext());
        initNightMode();
        handler.postDelayed(() -> {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (null != firebaseUser) {
                Toast.makeText(getApplicationContext(), "User already signed in " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));

            } else {
                Toast.makeText(getApplicationContext(), "User Not signed in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));


            }
        }, 2000);
    }

    private void initNightMode() {
        int nightMode = retrieveNightModeFromPreferences();
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private int retrieveNightModeFromPreferences() {
        return mSharedPrefs.getInt(Constants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
