package io.hustler.wallzy.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.pagerAdapters.MainPagerAdapter;
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

    int jellyViewDynamicWidth;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));

        ButterKnife.bind(this);
        mSharedPrefs = new SharedPrefsUtils(HomeActivity.this);
        setStatubar();

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
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
                float transalationOffset = (positionOffset + position) * jellyViewDynamicWidth;
                layoutParams.leftMargin = (int) transalationOffset;
                jellyView.setLayoutParams(layoutParams);
//                SpringAnimation springAnimation=new SpringAnimation(jellyView, DynamicAnimation.TRANSLATION_X,layoutParams.leftMargin);
//                springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
//                springAnimation.start();

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        appName.setLongClickable(true);
        appName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                return false;
            }
        });

        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        setWidth();
        hideBottom();
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);

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
            jellyViewDynamicWidth = tabLayout.getWidth() / tabLayout.getTabCount();
            FrameLayout.LayoutParams jellyViewParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
            jellyViewParams.width = jellyViewDynamicWidth;
            jellyView.setLayoutParams(jellyViewParams);
        });
    }


    @OnClick({R.id.menu_icon, R.id.search_icon, R.id.themeLayout, R.id.creditsLayout, R.id.Signout_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_icon:
                if (bottomLayout.getHeight() == (int) convertDptoPixels(180, getResources())) {
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
                Log.i(TAG, "setStatubar: Daymode foun");
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're at night!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                    flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
                    this.getWindow().getDecorView().setSystemUiVisibility(flags);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                    Log.i(TAG, "setStatubar: NightMode Found");


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

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(180, getResources()), (int) convertDptoPixels(0, getResources()));
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
            layoutParams.height = ((Integer) valueAnimator1.getAnimatedValue());
            bottomLayout.requestLayout();
        });
        valueAnimator.setDuration(300).start();

    }

    private void showBottom() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(0, getResources()), (int) convertDptoPixels(180, getResources()));
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
}
