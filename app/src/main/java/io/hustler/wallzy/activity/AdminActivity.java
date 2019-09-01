package io.hustler.wallzy.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.pagerAdapters.AdminPagerAdapter;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.admin_viewPager)
    ViewPager adminViewPager;
    @BindView(R.id.admin_tablayout)
    TabLayout adminTablayout;
    @BindView(R.id.admin_root)
    RelativeLayout adminRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        AdminPagerAdapter adminPagerAdapter = new AdminPagerAdapter(getSupportFragmentManager());
        adminViewPager.setAdapter(adminPagerAdapter);
        adminTablayout.setupWithViewPager(adminViewPager);
    }
}
