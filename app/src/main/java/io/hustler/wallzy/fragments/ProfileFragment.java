package io.hustler.wallzy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import io.hustler.wallzy.R;

public class ProfileFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    public static ProfileFragment getInstance() {
        return new ProfileFragment();

    }

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favs, container, false);
        return view;
    }


}
