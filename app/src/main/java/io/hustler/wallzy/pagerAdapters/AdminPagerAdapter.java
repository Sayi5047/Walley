package io.hustler.wallzy.pagerAdapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import io.hustler.wallzy.fragments.AdminCatCollFragment;
import io.hustler.wallzy.fragments.AdminImageUploadFragment;
import io.hustler.wallzy.fragments.AdminNotificationFragment;

public class AdminPagerAdapter extends FragmentPagerAdapter {
    public AdminPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public AdminPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) return AdminCatCollFragment.newInstance();
        else if (position == 2) return AdminNotificationFragment.newInstance();
        else return AdminImageUploadFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Category";
        else if (position == 2) return "Notifications";
        else return "Images";
    }
}
