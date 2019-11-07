package io.hustler.wallzy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.OnBoardAdapter;
import io.hustler.wallzy.constants.ServerConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.wallzy.request.ReqEmailLogin;
import io.hustler.wallzy.model.wallzy.request.ReqGoogleSignup;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 5004;
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.header)
    TextView mHeader;
    @BindView(R.id.guest_login)
    Button guest_account;
    @BindView(R.id.google_signin)
    SignInButton mGoogleSignin;

    @BindView(R.id.container)
    RelativeLayout mRootContainer;
    @BindView(R.id.onBoardRv)
    RecyclerView bgRv;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSigninClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);
        TextUtils.findText_and_applyTypeface(mRootContainer, LoginActivity.this);
        bgRv.setLayoutManager(new LinearLayoutManager(LoginActivity.this, RecyclerView.HORIZONTAL, false));
        bgRv.setAdapter(new OnBoardAdapter(getApplicationContext(), null));
        guest_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging in as guest");
                progressDialog.setTitle("Guest Login");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                ReqEmailLogin reqEmailLogin = new ReqEmailLogin();
                reqEmailLogin.setEmail("guestUser" + System.currentTimeMillis() + "@wallzyguest.com");
                reqEmailLogin.setPassword(UUID.randomUUID().toString());
                new RestUtilities().guestLogin(getApplicationContext(), reqEmailLogin, new RestUtilities.OnSuccessListener() {
                    @Override
                    public void onSuccess(Object onSuccessResponse) {
                        progressDialog.cancel();
                        ResLoginUser resLoginUser = new Gson().fromJson(onSuccessResponse.toString(), ResLoginUser.class);
                        if (resLoginUser.isApiSuccess()) {
                            new SharedPrefsUtils(getApplicationContext()).storeUserData(resLoginUser);
                            new SharedPrefsUtils(getApplicationContext()).putString(WallZyConstants.SHARED_PREFS_SYSTEM_AUTH_KEY, resLoginUser.getSysAuthToken());
                            new SharedPrefsUtils(getApplicationContext()).putBoolean(WallZyConstants.SHARED_PREFS_GUEST_ACCOUNT, true);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                        } else {
                            MessageUtils.showDismissableSnackBar(LoginActivity.this, guest_account, resLoginUser.getMessage());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.cancel();
                        MessageUtils.showDismissableSnackBar(LoginActivity.this, guest_account, error);

                    }
                });
            }
        });

    }

    @OnClick(R.id.google_signin)
    public void onViewClicked() {
        Intent signInIntent = mGoogleSigninClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                SharedPrefsUtils sharedPreferencesUtils = new SharedPrefsUtils(getApplication());
                sharedPreferencesUtils.getString(WallZyConstants.SP_USERDATA_KEY);

            } else {
                Toast.makeText(getApplicationContext(), "Google Sign-in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            assert account != null;

            Log.w(TAG, "signInResult:failed code=" + account.getIdToken());
            ReqGoogleSignup reqGoogleSignup = new ReqGoogleSignup();
            reqGoogleSignup.setGoogleIdToken(account.getIdToken());
            new RestUtilities().googleSignup(getApplicationContext(), reqGoogleSignup, new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    ResLoginUser resLoginUser = new Gson().fromJson(onSuccessResponse.toString(), ResLoginUser.class);
                    if (resLoginUser.getStatuscode() == ServerConstants.API_SUCCESS) {
                        new SharedPrefsUtils(getApplicationContext()).storeUserData(resLoginUser);
                        new SharedPrefsUtils(getApplicationContext()).putString(WallZyConstants.SHARED_PREFS_SYSTEM_AUTH_KEY, resLoginUser.getSysAuthToken());
                        new SharedPrefsUtils(getApplicationContext()).putBoolean(WallZyConstants.SHARED_PREFS_GUEST_ACCOUNT, false);

                        updateUI(account);

                    } else {
                        MessageUtils.showShortToast(LoginActivity.this, resLoginUser.getMessage());
                    }

                }

                @Override
                public void onError(String error) {
                    MessageUtils.showShortToast(LoginActivity.this, error);
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            /*USER NOT SIGNED IN*/
            if (new SharedPrefsUtils(getApplicationContext()).putBoolean(WallZyConstants.SHARED_PREFS_GUEST_ACCOUNT, false)) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        } else {
            /*USER SIGNED IN*/
            FirebaseMessaging.getInstance().subscribeToTopic(WallZyConstants.DEFAULT_TOPIC_FOR_ALL_USERS).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "onComplete: User SuccessFully Subscribed to " + WallZyConstants.DEFAULT_TOPIC_FOR_ALL_USERS);
                    }
                }
            });
            Toast.makeText(getApplicationContext(), MessageFormat.format("{0} Successfully Signed In", Objects.requireNonNull(account).getDisplayName()), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

        }
    }

    private void firebaseGoogleLogin() {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch Google sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    //            Date date=new Date();
//            Date storeappDate = new Date(date.getTime() + 60 * 60 * 1000);
//            String compactJws = Jwts.builder()
//                    .claim("idToken", account.getIdToken())
//                    .setIssuedAt(date)
//                    .setExpiration(storeappDate)
//                    .signWith(SignatureAlgorithm.HS256, getString(R.string.sBn5vOoKKETgIohac44eRiB0B5YeODdTeeqP5FEhJutMCYtx8hXGxkXkds5zQGw).getBytes())
//                    .compact();
}
