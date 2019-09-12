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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.MessageFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
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
        mSharedPrefs = new SharedPrefsUtils(getApplicationContext());
        initNightMode();
    }


    private void initNightMode() {
        int nightMode = retrieveNightModeFromPreferences();
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private int retrieveNightModeFromPreferences() {
        return mSharedPrefs.getInt(WallZyConstants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            /*USER NOT SIGNED IN*/
            Toast.makeText(getApplicationContext(), "User Not signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

        } else {
            /*USER SIGNED IN*/
            Toast.makeText(getApplicationContext(), MessageFormat.format("{0} Logged In", Objects.requireNonNull(account).getDisplayName()), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));

        }
    }

    private void firebaseLoginCheck(Handler handler) {
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

}
