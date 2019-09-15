package io.hustler.wallzy.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class ProfileFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.verticalRv)
    RecyclerView verticalRv;
    ResLoginUser resLoginUser;

    SharedPrefsUtils sharedPrefsUtils;
    @BindView(R.id.profileName)
    TextView profileName;
    @BindView(R.id.donwload_tv)
    TextView donwloadTv;
    @BindView(R.id.downlaod_val)
    TextView downlaodVal;
    @BindView(R.id.favs_tv)
    TextView favsTv;
    @BindView(R.id.favs_val)
    TextView favsVal;
    @BindView(R.id.walls_tv)
    TextView wallsTv;
    @BindView(R.id.walls_val)
    TextView wallsVal;
    @BindView(R.id.root)
    RelativeLayout root;

    public static ProfileFragment getInstance() {
        return new ProfileFragment();

    }

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profileImage.setClipToOutline(true);
            profileImage.setElevation(16f);
            profileImage.requestLayout();
        }
        sharedPrefsUtils = new SharedPrefsUtils(getContext());
        resLoginUser = sharedPrefsUtils.getUserData();
        Glide.with(getContext()).load(resLoginUser.getImageUrl()).centerCrop().into(profileImage);
        profileName.setText(resLoginUser.getName());

        TextUtils.findText_and_applyTypeface(root, getActivity());
        return view;
    }


}
