package io.hustler.wallzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 5004;
    @BindView(R.id.header)
    TextView mHeader;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mEmail_login;
    @BindView(R.id.or_text)
    TextView mOrText;
    @BindView(R.id.google_signin)
    SignInButton mGoogleSignin;
    @BindView(R.id.loading)
    ProgressBar mLoadingBar;
    @BindView(R.id.signin_form)
    ConstraintLayout mSigninForm;
    @BindView(R.id.container)
    RelativeLayout mRootContainer;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        TextUtils.findText_and_applyTypeface(mRootContainer, LoginActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                SharedPrefsUtils sharedPreferencesUtils = new SharedPrefsUtils(getApplication());
                sharedPreferencesUtils.getString(Constants.SP_USERDATA_KEY);
                Toast.makeText(getApplicationContext(), MessageFormat.format("{0} Logged In", Objects.requireNonNull(user).getDisplayName()), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));

            } else {
                Toast.makeText(getApplicationContext(), "Sign-in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @OnClick(R.id.google_signin)
    public void onViewClicked() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch Google sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}
