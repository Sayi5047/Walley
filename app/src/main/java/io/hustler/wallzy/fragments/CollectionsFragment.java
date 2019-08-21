package io.hustler.wallzy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.hustler.wallzy.R;

public class CollectionsFragment extends Fragment {
    CollectionsFragment categoriesFragment;

    public static CollectionsFragment getInstance() {
        return new CollectionsFragment();

    }

    public CollectionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        return view;
    }
}
