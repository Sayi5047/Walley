package io.hustler.wallzy.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.pagerAdapters.MainPagerAdapter;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class HomeActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mSharedPrefs = new SharedPrefsUtils(HomeActivity.this);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        TextUtils.findText_and_applyTypeface(root, HomeActivity.this);

//        setTabIcon(tabLayout, 0, R.drawable.ic_today_images_24dp);
//        setTabIcon(tabLayout, 1, R.drawable.ic_categories_24dp);
//        setTabIcon(tabLayout, 2, R.drawable.ic_explore_24dp);
//        setTabIcon(tabLayout, 3, R.drawable.ic_collection_24dp);
//        setTabIcon(tabLayout, 4, R.drawable.ic_favorite_black_24dp);
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_color));
        tabLayout.setSelected(true);
        viewPager.setCurrentItem(0, true);

        setWidth();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) jellyView.getLayoutParams();
                float transalationOffset = (positionOffset + position) * jellyViewDynamicWidth;
                layoutParams.leftMargin = (int) transalationOffset;
                jellyView.setLayoutParams(layoutParams);
//
//                float multplicationOffset = positionOffset * 10;
//                if (multplicationOffset <= 7 && multplicationOffset != 0.000) {
//                    layoutParams.width = (int) transalationOffset;
//                    jellyView.setLayoutParams(layoutParams);
//
//                } else {
//                    setWidth();
//                    jellyView.setLayoutParams(layoutParams);
//
//                }


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

}
