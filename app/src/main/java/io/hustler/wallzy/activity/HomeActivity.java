package io.hustler.wallzy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqUpdateFcmToken;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.pagerAdapters.MainPagerAdapter;
import io.hustler.wallzy.utils.DimenUtils;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public SharedPrefsUtils mSharedPrefs;


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.jelly_tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.root)
    RelativeLayout root;

    MainPagerAdapter mainPagerAdapter;
    @BindView(R.id.jelly_view)
    View jellyView;
    @BindView(R.id.jelly_frame)
    FrameLayout jellyFrame;

    @BindView(R.id.menu_icon)
    ImageView menuIcon;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toggle_theme_textView)
    TextView toggleThemeTextView;
    @BindView(R.id.themeLayout)
    RelativeLayout themeLayout;
    @BindView(R.id.CreditsimageView)
    ImageView CreditsimageView;
    @BindView(R.id.Credits_textView)
    TextView CreditsTextView;
    @BindView(R.id.creditsLayout)
    RelativeLayout creditsLayout;
    @BindView(R.id.signOutIV)
    ImageView signOutIV;
    @BindView(R.id.Signout_textView)
    TextView SignoutTextView;
    @BindView(R.id.Signout_layout)
    RelativeLayout SignoutLayout;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;


    private int selectedPosition = 0;
    private int previousPosition = 0;
    boolean shown = false;
    private int previosOffsetPixel = 0;
    private int bottomViewHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /*Translates content to above nav bar*/
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(android.R.id.content), (v, insets) -> {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                    params.bottomMargin = insets.getSystemWindowInsetBottom();
                    return insets.consumeSystemWindowInsets();
                });
        this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        ButterKnife.bind(this);
        mSharedPrefs = new SharedPrefsUtils(HomeActivity.this);
        setStatubar();
        int height = jellyView.getHeight();
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setNestedScrollingEnabled(true);
        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);

                if (selectedPosition >= position && positionOffsetPixels > 10 && (positionOffsetPixels > previosOffsetPixel)) {
                    if ((positionOffset * 100 > 0) && (positionOffset * 100 < 85)) {
                        int width = (int) (tabView.getWidth() + ((tabView.getWidth() / 100) * (positionOffset * 100)));
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
                        layoutParams.width = (width);
                        //Log.i(TAG, "onPageScrolled: Calculated Value " + width);
                        if (layoutParams.height > tabView.getHeight() / 2) {
                            layoutParams.height = (int) (layoutParams.height - 0.1);
                        }
                        jellyView.setLayoutParams(layoutParams);
                    }
                    previosOffsetPixel = positionOffsetPixels;
                } else {
                    previosOffsetPixel = positionOffsetPixels;
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
//                    layoutParams.width = tabView.getWidth();
                    if (layoutParams.height > tabView.getHeight() / 2) {
                        layoutParams.height = (int) (layoutParams.height - 0.1);
                    }
                    jellyView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onPageSelected(int position) {
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);

                if (selectedPosition < position) {
                    // TODO: 19-09-2019 forward
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            final ValueAnimator xpositionAnimator = ValueAnimator.ofFloat(jellyView.getX(), tabView.getX());
                            final ValueAnimator insideWidthAnimtor = ValueAnimator.ofInt(jellyView.getWidth(), tabView.getWidth());
                            final ValueAnimator insideheightAnimtor = ValueAnimator.ofInt(jellyView.getHeight(), (int) DimenUtils.convertDptoPixels(32, getResources()));
                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.playTogether(insideWidthAnimtor, xpositionAnimator, insideheightAnimtor);

                            xpositionAnimator.addUpdateListener(valueAnimator -> {
                                float x = (Float) valueAnimator.getAnimatedValue();
                                jellyView.setX(x);
                            });
                            insideWidthAnimtor.addUpdateListener(valueAnimator -> {
                                ViewGroup.LayoutParams params = jellyView.getLayoutParams();
                                params.width = (Integer) insideWidthAnimtor.getAnimatedValue();
                                jellyView.setLayoutParams(params);

                            });
                            insideheightAnimtor.addUpdateListener(valueAnimator -> {
                                ViewGroup.LayoutParams params = jellyView.getLayoutParams();
                                params.height = (Integer) insideheightAnimtor.getAnimatedValue();
                                jellyView.setLayoutParams(params);
                            });
                            animatorSet.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    selectedPosition = position;

                                }
                            });
                            animatorSet.setDuration(300);
                            animatorSet.start();


                        }
                    }, 100);


                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            final ValueAnimator xpositionAnimator = ValueAnimator.ofFloat(jellyView.getX(), tabView.getX());
                            xpositionAnimator.addUpdateListener(valueAnimator -> {
                                float x = (Float) valueAnimator.getAnimatedValue();
                                jellyView.setX(x);
                            });
                            xpositionAnimator.start();
                            revertHeigtandWeight(tabView);
                            selectedPosition = position;

                        }
                    }, 100);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);

                //Log.i(TAG, "onPageScrollStateChanged: " + state);
                if (state == 0 && jellyView.getWidth() != tabView.getWidth()) {
                    revertHeigtandWeight(tabView);
                }
            }
        });

        appName.setLongClickable(true);
        appName.setOnLongClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, AdminActivity.class));
            return false;
        });

        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        setWidth();
        ViewTreeObserver viewTreeObserver = bottomLayout.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        bottomViewHeight = bottomLayout.getMeasuredHeight();
                        hideBottom();
                        bottomLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    }
                });
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);

    }

    private void revertHeigtandWeight(View tabView) {
        final ValueAnimator insideWidthAnimtor = ValueAnimator.ofInt(jellyView.getWidth(), tabView.getWidth());
        insideWidthAnimtor.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams params = jellyView.getLayoutParams();
            params.width = (Integer) insideWidthAnimtor.getAnimatedValue();
            jellyView.setLayoutParams(params);
        });
        insideWidthAnimtor.start();
        final ValueAnimator insideheightAnimtor = ValueAnimator.ofInt(jellyView.getHeight(), (int) DimenUtils.convertDptoPixels(32, getResources()));

        insideheightAnimtor.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams params = jellyView.getLayoutParams();
            params.height = (Integer) insideheightAnimtor.getAnimatedValue();
            jellyView.setLayoutParams(params);
        });
        insideheightAnimtor.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(insideheightAnimtor, insideWidthAnimtor);
        animatorSet.start();
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setWidth() {
        tabLayout.post(() -> {
            View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
            revertHeigtandWeight(tabView);
//            jellyViewDynamicWidth = tabView.getWidth();
//            FrameLayout.LayoutParams jellyViewParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
//            jellyViewParams.width = jellyViewDynamicWidth;
//            jellyView.setLayoutParams(jellyViewParams);
//            jellyView.requestLayout();
        });
    }


    @OnClick({R.id.menu_icon, R.id.search_icon, R.id.themeLayout, R.id.creditsLayout, R.id.Signout_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_icon:
                if (bottomLayout.getHeight() == bottomViewHeight) {
                    hideBottom();
                } else {
                    showBottom();
                }
                break;
            case R.id.search_icon:
                MessageUtils.showShortToast(HomeActivity.this, "Coming Soon..!");
                break;
            case R.id.themeLayout:
                int currentNightMode = getCurrentNightMode();
                changeBetweenDayandNightMode(currentNightMode);
                break;
            case R.id.creditsLayout:
                MessageUtils.showShortToast(HomeActivity.this, "Coming Soon..!");
                break;
            case R.id.Signout_layout:
                GoogleSignInClient mGoogleSigninClient;
                GoogleSignInOptions gso;
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

                mGoogleSigninClient = GoogleSignIn.getClient(this, gso);
                mGoogleSigninClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                new SharedPrefsUtils(getApplicationContext()).clearAllUserData();
                                saveNightModeToPreferences(getCurrentNightMode());
                                startActivity(new Intent(HomeActivity.this, SplashActivity.class));
                            }
                        });

                break;
        }
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

    private void setLightStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    private void setStatubar() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're in day time
                setLightStatusbar();
                //Log.i(TAG, "setStatubar: Daymode foun");
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're at night!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                    flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                    this.getWindow().getDecorView().setSystemUiVisibility(flags);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                    //Log.i(TAG, "setStatubar: NightMode Found");


                }
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                setLightStatusbar();
        }
    }

    private void initNightMode(int newNightMode) {
        AppCompatDelegate.setDefaultNightMode(newNightMode);
    }

    private void saveNightModeToPreferences(int newNightMode) {
        mSharedPrefs.putInt(WallZyConstants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY, newNightMode);
    }

    private void hideBottom() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(bottomViewHeight, (int) convertDptoPixels(0, getResources()));
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
            layoutParams.height = ((Integer) valueAnimator1.getAnimatedValue());
            bottomLayout.requestLayout();
        });
        valueAnimator.setDuration(300).start();

    }

    private void showBottom() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(0, getResources()), bottomViewHeight);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
            layoutParams.height = ((Integer) valueAnimator1.getAnimatedValue());
            bottomLayout.requestLayout();
        });
        valueAnimator.setDuration(300).start();

    }

    public float convertDptoPixels(float dp, Resources resources) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                }
                // Get new Instance ID token

                String token = Objects.requireNonNull(task.getResult()).getToken();
                ReqUpdateFcmToken reqUpdateFcmToken = new ReqUpdateFcmToken();
                reqUpdateFcmToken.setFmcToken(token);
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "onComplete: TOKEN " + token);
                }
                if (!new SharedPrefsUtils(HomeActivity.this).getBoolean(WallZyConstants.SHARED_PREFS_GUEST_ACCOUNT)) {
                    ResLoginUser resLoginUser = new SharedPrefsUtils(getApplicationContext()).getUserData();
                    if (null != resLoginUser) {
                        reqUpdateFcmToken.setUserId(resLoginUser.getId());
                        new RestUtilities().update_fcm_token(getApplicationContext(), reqUpdateFcmToken, new RestUtilities.OnSuccessListener() {
                            @Override
                            public void onSuccess(Object onSuccessResponse) {
                                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                                if (baseResponse.isApiSuccess()) {
                                    Log.i(TAG, "onSuccess: UPDATE FCM TOKEN");

                                    mSharedPrefs.putString(WallZyConstants.SHARED_PREFS_USER_FCM_TOKEN, token);

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
        });
    }
}
