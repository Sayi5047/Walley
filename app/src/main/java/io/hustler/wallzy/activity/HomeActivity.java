package io.hustler.wallzy.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.pagerAdapters.MainPagerAdapter;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public SharedPrefsUtils mSharedPrefs;


    @BindView(R.id.signout_btn)
    ImageView signoutBtn;
    @BindView(R.id.night_mode_btn)
    ImageView nightModeBtn;

    FirebaseAuth mFirebaseAuth;

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
    private GoogleSignInClient mGoogleSigninClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mSharedPrefs = new SharedPrefsUtils(HomeActivity.this);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);

        tabLayout.setSelected(true);
        viewPager.setCurrentItem(0, true);
        MessageUtils.showShortToast(HomeActivity.this, new SharedPrefsUtils(getApplicationContext()).getString(Constants.SHARED_PREFS_SYSTEM_AUTH_KEY));
        Log.i(TAG, new SharedPrefsUtils(getApplicationContext()).getString(Constants.SHARED_PREFS_SYSTEM_AUTH_KEY));

        setWidth();
        hideBottom();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
                float transalationOffset = (positionOffset + position) * jellyViewDynamicWidth;
                layoutParams.leftMargin = (int) transalationOffset;
                jellyView.setLayoutParams(layoutParams);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                   (tab.getIcon()).setTint(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_color));
//                } else {
//                    Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_color), PorterDuff.Mode.SRC_IN);
//
//                }
//            }

//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    (tab.getIcon()).setTint(ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected_color));
//                } else {
//                    Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN);
//
//                }
//

//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//
//            }
//        });
//    }

    }

    private void setWidth() {
        tabLayout.post(() -> {
            jellyViewDynamicWidth = tabLayout.getWidth() / tabLayout.getTabCount();
            FrameLayout.LayoutParams jellyViewParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
            jellyViewParams.width = jellyViewDynamicWidth;
            jellyView.setLayoutParams(jellyViewParams);
        });
    }

    /*CUSTOMISING TAB FEATURES*/
    private void setTabIcon(TabLayout tabLayout, int i, int p) {
        Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(ContextCompat.getDrawable(getApplicationContext(), p));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (tabLayout.getTabAt(i).getIcon()).setTint(ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected_color));
        } else {
            (tabLayout.getTabAt(i).getIcon()).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tab_unselected_color), PorterDuff.Mode.SRC_IN);

        }

    }

    /*NIGHT MODE FEATURE*/

    @OnClick({R.id.night_mode_btn, R.id.signout_btn, R.id.menu_icon, R.id.search_icon, R.id.themeLayout, R.id.creditsLayout, R.id.Signout_layout})
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

    private void initNightMode(int newNightMode) {
        AppCompatDelegate.setDefaultNightMode(newNightMode);
    }

    private void saveNightModeToPreferences(int newNightMode) {
        mSharedPrefs.putInt(Constants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY, newNightMode);
    }

    private void hideBottom() {

        float alpha = 100f;
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(180, getResources()), (int) convertDptoPixels(0, getResources()));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
                layoutParams.height = ((Integer) valueAnimator.getAnimatedValue());
                bottomLayout.requestLayout();
            }
        });
        valueAnimator.setDuration(300).start();

    }

    private void showBottom() {

        float alpha = 100f;
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) convertDptoPixels(0, getResources()), (int) convertDptoPixels(180, getResources()));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = bottomLayout.getLayoutParams();
                layoutParams.height = ((Integer) valueAnimator.getAnimatedValue());
                bottomLayout.requestLayout();
            }
        });
        valueAnimator.setDuration(300).start();

    }

    public static float convertDptoPixels(float dp, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
