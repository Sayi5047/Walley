package io.hustler.wallzy.pagerAdapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import io.hustler.wallzy.fragments.CategoriesFragment;
import io.hustler.wallzy.fragments.CollectionsFragment;
import io.hustler.wallzy.fragments.ExploreFragment;
import io.hustler.wallzy.fragments.FavsFragment;
import io.hustler.wallzy.fragments.TodayImagesFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return 5;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Latest";
            case 1:
                return "CategoryTable";
            case 2:
                return "Explore";
            case 3:
                return "Curated";
            case 4:
                return "Liked";
            default:
                return "Explore";
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TodayImagesFragment.getInstance();
            case 1:
                return CategoriesFragment.getInstance();
            case 2:
                return ExploreFragment.getInstance();
            case 3:
                return CollectionsFragment.getInstance();
            case 4:
                return FavsFragment.getInstance();
            default:
                return ExploreFragment.getInstance();
        }
    }

}
