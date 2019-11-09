package io.hustler.wallzy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.ArComponents.BaseWorker;
import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.BottomFeaturesAdapter;
import io.hustler.wallzy.adapters.SearchImagesAdapter;
import io.hustler.wallzy.constants.ServerConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqUpdateFcmToken;
import io.hustler.wallzy.model.wallzy.response.ResImageSearch;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.pagerAdapters.MainPagerAdapter;
import io.hustler.wallzy.utils.DimenUtils;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.NotificationUtils;
import io.hustler.wallzy.utils.PermissionUtils;
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
    LottieAnimationView menuIcon;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.search_icon)
    LottieAnimationView searchIcon;


    /*OPTIONS FOOTER*/
    @BindView(R.id.bottom_app_bar)
    LinearLayout bottom_app_bar;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;


    /*SEARCH VIEWS*/
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_images_rv)
    RecyclerView searchImagesRv;
    @BindView(R.id.image_search_layout)
    RelativeLayout innerImageSearchLayout;
    @BindView(R.id.message_icon)
    LottieAnimationView searchErrorMessageLottieIcon;
    @BindView(R.id.messageView)
    TextView searchErrorMessage;
    @BindView(R.id.search_message_layout)
    RelativeLayout searchMessageLayout;
    @BindView(R.id.search_layout)
    RelativeLayout searchLayout;

    /*BOTTOM VIEW*/
    @BindView(R.id.footer_rl)
    RelativeLayout footerRl;


    @BindView(R.id.bottom_features_rv)
    RecyclerView bottomFeaturesRv;


    private int selectedPosition = 0;
    private int previosOffsetPixel = 0;
    private int bottomViewHeight, searchViewHeight = 0;

    private boolean isMenuShowing, isSearchShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        /*Translates content to above nav bar*/
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
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
        setStatusBar();

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
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                bottomViewHeight = bottomLayout.getMeasuredHeight();
                searchViewHeight = searchLayout.getMeasuredHeight();
                hideBottom(bottomViewHeight, bottomLayout);
                hideBottom(searchViewHeight, searchLayout);
                bottomLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

        bottomFeaturesRv.setLayoutManager(new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false));
        List<String> namesArray;
        TypedArray imagesArray;
        namesArray = Arrays.asList(getResources().getStringArray(R.array.footer_features_array));
        imagesArray = (getResources().obtainTypedArray(R.array.footer_features_images));
        bottomFeaturesRv.setAdapter(new BottomFeaturesAdapter(HomeActivity.this, namesArray,
                imagesArray, featureName -> {
            switch (featureName) {

                case "Toggle Theme": {
                    int currentNightMode = getCurrentNightMode();
                    changeBetweenDayandNightMode(currentNightMode);
                }
                break;
                case "Profile": {
                    Intent intent = new Intent(HomeActivity.this, FragmentActivity.class);
                    intent.putExtra(WallZyConstants.INTENT_FRAGMENT_ACTIVITY_FRAGMENT_NUMBER, 0);
                    startActivity(intent);
                }
                break;

                case "Settings": {
                    startActivity(new Intent(HomeActivity.this, SettingsActivity.class));

                }
                break;

                case "About": {
                    MessageUtils.showShortToast(HomeActivity.this, getString(R.string.coming_soon));
                }
                break;

                case "SignOut": {
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
                }
                break;
            }
        }));
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        searchIcon.setFrame(29);


        if (!PermissionUtils.isStoragePermissionAvailable(Objects.requireNonNull(HomeActivity.this))) {
            PermissionUtils.requestStoragrPermissions(HomeActivity.this, WallZyConstants.MY_PERMISSION_REQUEST_STORAGE);
        }

        NotificationUtils notificationUtils=new NotificationUtils();
        notificationUtils.createAllNotificationGroups(this.getApplicationContext());
        notificationUtils.createAllNotificationChannels(this.getApplicationContext());

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


    @OnClick({R.id.menu_icon, R.id.search_icon, R.id.app_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_icon:
                if (bottomLayout.getHeight() == bottomViewHeight) {
                    menuIcon.setMinAndMaxFrame(99, 124);
                    menuIcon.playAnimation();
                    hideBottom(bottomViewHeight, bottomLayout);
                    Log.i(TAG, "onViewClicked: HIDE BOTTOM");
                } else {
                    menuIcon.setMinAndMaxFrame(31, 99);
                    menuIcon.playAnimation();
                    showBottom(bottomViewHeight, bottomLayout);
                    Log.i(TAG, "onViewClicked: SHOW BOTTOM");
                }
                break;
            case R.id.search_icon:
                if (isSearchShowing) {
                    searchIcon.setFrame(48);
                    hideBottom(searchViewHeight, searchLayout);
                } else if (searchLayout.getHeight() == searchViewHeight) {
                    searchIcon.setFrame(48);
                    hideBottom(searchViewHeight, searchLayout);

                } else {
                    searchIcon.setFrame(48);
                    searchIcon.setMinAndMaxFrame(48, 68);
                    searchIcon.playAnimation();
                    showBottom(searchViewHeight, searchLayout);
                    handleSearch();

                }
                break;
            case R.id.app_name: {
                WorkManager workManager = WorkManager.getInstance(HomeActivity.this);
                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(BaseWorker.class).build();
                Constraints constraints = new Constraints.Builder().setRequiresCharging(false).build();

                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(BaseWorker.class, 15, TimeUnit.MINUTES, 10, TimeUnit.MINUTES).build();
//                workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.getId()).observe(HomeActivity.this, workInfo -> {
//                    if (null != workInfo) {
//                        WorkInfo.State state = workInfo.getState();
//                        MessageUtils.showShortToast(HomeActivity.this, state.toString());
//                    }
//                });
                workManager.getWorkInfoByIdLiveData(periodicWorkRequest.getId()).observe(HomeActivity.this, workInfo -> {
                    if (null != workInfo) {
                        WorkInfo.State state = workInfo.getState();
                        MessageUtils.showShortToast(HomeActivity.this, state.toString());
                        Log.i("WORKER", "ON WORKER EXECUTED " + state.toString());
                    }
                });
                workManager.enqueue(periodicWorkRequest);

                MessageUtils.showShortToast(HomeActivity.this, "Started");
//                workManager.enqueue(oneTimeWorkRequest);

            }
            break;
        }
    }

    private void handleSearch() {
        searchImagesRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    callSearchAPi(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void callSearchAPi(String toString) {
        new RestUtilities().searchImegesByTag(getApplicationContext(), toString, 0, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                ResImageSearch resImageSearch = new Gson().fromJson(onSuccessResponse.toString(), ResImageSearch.class);

                if (resImageSearch.getStatuscode() == ServerConstants.API_SUCCESS) {
                    if (resImageSearch.getTagImages().size() <= 0) {
                        hideViewAndShowErrorMessage(R.string.error_msg_no_data_found);


                    } else {
                        searchMessageLayout.setVisibility(View.GONE);
                        searchImagesRv.setVisibility(View.VISIBLE);
                        SearchImagesAdapter imagesAdapter = new SearchImagesAdapter(HomeActivity.this, new SearchImagesAdapter.OnItemClcikListener() {
                            @Override
                            public void onItemClick(ResImageSearch.TagImage position) {
                                Intent intent = new Intent(HomeActivity.this, SingleImageActivity.class);
                                intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, position.getRawUrl());
                                intent.putExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE_CLASS, "");
                                intent.putExtra(WallZyConstants.INTENT_IS_FROM_SEARCH, true);
                                intent.putExtra(WallZyConstants.INTENT_SEARCH_IMAGE_ID, position.getId());
                                startActivity(intent);
                            }
                        }, resImageSearch.getTagImages());
                        searchImagesRv.setAdapter(imagesAdapter);

                    }


                } else if (resImageSearch.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    hideViewAndShowErrorMessage(R.string.error_msg_no_data_found);
                } else if (resImageSearch.getStatuscode() == ServerConstants.API_FAILURE) {
                    hideViewAndShowErrorMessage(R.string.error_msg_something_went_wrong);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void hideViewAndShowErrorMessage(int p) {
        searchImagesRv.setVisibility(View.GONE);
        searchMessageLayout.setVisibility(View.VISIBLE);
        searchErrorMessageLottieIcon.setAnimation(R.raw.lottie_not_fount_error);
        searchErrorMessageLottieIcon.playAnimation();
        searchErrorMessageLottieIcon.setRepeatCount(LottieDrawable.INFINITE);
        searchErrorMessage.setText(p);
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

    private void setStatusBar() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're in day time
                setLightStatusbar();
                //Log.i(TAG, "setStatusBar: Daymode foun");
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're at night!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                    flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                    this.getWindow().getDecorView().setSystemUiVisibility(flags);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                    //Log.i(TAG, "setStatusBar: NightMode Found");


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

    private void hideBottom(int height, RelativeLayout relativeLayout) {
        if (relativeLayout.getId() == R.id.bottom_layout) {
            isMenuShowing = false;
        } else {
            isSearchShowing = false;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(height, (int) convertDptoPixels(0, getResources()));
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
            layoutParams.height = ((Integer) valueAnimator1.getAnimatedValue());
            relativeLayout.requestLayout();
        });
        valueAnimator.setDuration(300).start();

    }

    private void showBottom(int height, RelativeLayout relativeLayout) {
        if (relativeLayout.getId() == R.id.bottom_layout) {
            isMenuShowing = true;

        } else {
            isSearchShowing = true;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(0, getResources()), height);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
            layoutParams.height = ((Integer) valueAnimator1.getAnimatedValue());
            relativeLayout.requestLayout();
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

    @Override
    public void onBackPressed() {
        if (isMenuShowing) {
            menuIcon.performClick();
        } else if (isSearchShowing) {
            searchIcon.performClick();

        } else if (isMenuShowing && isSearchShowing) {
            menuIcon.performClick();
            searchIcon.performClick();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WallZyConstants.MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showStorageErrorMessage();
                }
            }
            break;
            case WallZyConstants.MY_PERMISSION_REQUEST_STORAGE_FOR_DOWNLOAD_WALLPAPER: {
                handlePermissionRsult(grantResults);

            }
            break;
            case WallZyConstants.MY_PERMISSION_REQUEST_STORAGE_FOR_SETWALLPAPER: {
                handlePermissionRsult(grantResults);
            }
            break;
        }

    }

    private void handlePermissionRsult(@NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: STORAGE PERMISSION GRANTED");


        } else {
            showStorageErrorMessage();
        }
    }

    private void showStorageErrorMessage() {
        MessageUtils.showDismissableSnackBar(Objects.requireNonNull(HomeActivity.this), viewPager, getString(R.string.storage_permission_rejected_message));
    }
}
