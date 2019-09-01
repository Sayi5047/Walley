package io.hustler.wallzy.pagerAdapters;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import io.hustler.wallzy.fragments.CategoriesFragment;
import io.hustler.wallzy.fragments.CollectionsFragment;
import io.hustler.wallzy.fragments.ExploreFragment;
import io.hustler.wallzy.fragments.FavsFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return getSpannableString("Category");
            case 2:
                return getSpannableString("Curated");
            case 3:
                return getSpannableString("Liked");
            default:
                return getSpannableString("Explore");
        }
    }

   public SpannableString getSpannableString(String val) {
        SpannableString spannableString = new SpannableString(val);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, val.length(), 0);
        return spannableString;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CategoriesFragment.getInstance();
            case 1:
                return ExploreFragment.getInstance();
            case 2:
                return CollectionsFragment.getInstance();
            case 3:
                return FavsFragment.getInstance();
//            case 4:
//                return FavsFragment.getInstance();
            default:
                return ExploreFragment.getInstance();
        }
    }

}
