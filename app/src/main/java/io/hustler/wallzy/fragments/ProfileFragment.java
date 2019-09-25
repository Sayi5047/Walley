package io.hustler.wallzy.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.SplashActivity;
import io.hustler.wallzy.adapters.UserImagesAdapter;
import io.hustler.wallzy.constants.ServerConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.wallzy.request.ReqUserImage;
import io.hustler.wallzy.model.wallzy.response.ResGetAllUserImages;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.model.wallzy.response.ResponseUserImageClass;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;
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
    @BindView(R.id.likes)
    ImageView likes;
    @BindView(R.id.downlaods)
    ImageView downlaods;
    @BindView(R.id.walls)
    ImageView walls;
    @BindView(R.id.tabLayout)
    RelativeLayout tabLayout;
    UserImagesAdapter userImagesAdapter;
    int currentScreen = 0;
    ArrayList<ResponseUserImageClass> resUserFavImagesFromApi;
    boolean guest;
    @BindView(R.id.text_head)
    TextView textHead;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.signinLayout)
    RelativeLayout signinLayout;
    @BindView(R.id.profile_card)
    CardView profileCard;
    @BindView(R.id.topRoot)
    RelativeLayout topRoot;

    public static ProfileFragment getInstance() {
        return new ProfileFragment();

    }

    public ProfileFragment() {
    }

    private void saveNightModeToPreferences(int newNightMode) {
        sharedPrefsUtils.putInt(WallZyConstants.SP_IS_NIGHT_MODEA_ACTIVATED_KEY, newNightMode);
    }

    private int getCurrentNightMode() {
        return getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        sharedPrefsUtils = new SharedPrefsUtils(Objects.requireNonNull(getContext()));
        guest = sharedPrefsUtils.getBoolean(WallZyConstants.SHARED_PREFS_GUEST_ACCOUNT);
        if (guest) {
            root.setVisibility(View.GONE);
            signinLayout.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SharedPrefsUtils(getActivity().getApplicationContext()).clearAllUserData();
                    saveNightModeToPreferences(getCurrentNightMode());
                    startActivity(new Intent(getActivity(), SplashActivity.class));
                }
            });
        } else {
            root.setVisibility(View.VISIBLE);
            signinLayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                profileImage.setClipToOutline(true);
            }
            resLoginUser = sharedPrefsUtils.getUserData();
            Glide.with(getContext()).load(resLoginUser.getImageUrl()).centerCrop().into(profileImage);
            profileName.setText(resLoginUser.getName());
            ReqUserImage reqUserImage = new ReqUserImage();
            reqUserImage.setUserId(resLoginUser.getId());
            reqUserImage.setOrigin("Android");
            reqUserImage.setVersion(BuildConfig.VERSION_NAME);
            reqUserImage.setCountry("IN");
            likes.performClick();
            new RestUtilities().getUserFavs(getContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    ResGetAllUserImages resGetAllUserImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllUserImages.class);
                    if (resGetAllUserImages.getStatuscode() == ServerConstants.API_SUCCESS) {
                        resUserFavImagesFromApi = resGetAllUserImages.getImages();
                        verticalRv.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
                        userImagesAdapter = new UserImagesAdapter(getActivity(), new UserImagesAdapter.OnItemClcikListener() {
                            @Override
                            public void onItemClick(ResponseUserImageClass position) {

                            }
                        }, getFilteredData(0));
                        currentScreen = 0;
                        verticalRv.setAdapter(userImagesAdapter);
                        favsVal.setText(String.valueOf(resGetAllUserImages.getLikeCount()));
                        downlaodVal.setText(String.valueOf(resGetAllUserImages.getDonwloadCount()));
                        wallsVal.setText(String.valueOf(resGetAllUserImages.getWallCount()));
                    }
                }

                @Override
                public void onError(String error) {
                    MessageUtils.showDismissableSnackBar(getActivity(), getView(), error);
                }
            });
        }
        TextUtils.findText_and_applyTypeface(topRoot, Objects.requireNonNull(getActivity()));
        return view;
    }


    @OnClick({R.id.likes, R.id.downlaods, R.id.walls, R.id.tabLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.likes:
                if (currentScreen != 0) {
                    currentScreen = 0;
                    userImagesAdapter.removeAll();
                    userImagesAdapter.addAllData(getFilteredData(0));
                    likes.setColorFilter(null);
                    walls.setColorFilter(android.R.color.darker_gray);
                    downlaods.setColorFilter(android.R.color.darker_gray);
                    increaseScale(likes);
                    decreaseScale(downlaods);
                    decreaseScale(walls);
                }
                break;
            case R.id.downlaods:
                if (currentScreen != 1) {
                    currentScreen = 1;
                    userImagesAdapter.removeAll();
                    userImagesAdapter.addAllData(getFilteredData(1));
                    likes.setColorFilter(android.R.color.darker_gray);
                    walls.setColorFilter(android.R.color.darker_gray);
                    downlaods.setColorFilter(null);
                    increaseScale(downlaods);
                    decreaseScale(likes);
                    decreaseScale(walls);
                }
                break;
            case R.id.walls:
                if (currentScreen != 2) {
                    currentScreen = 2;
                    userImagesAdapter.removeAll();
                    userImagesAdapter.addAllData(getFilteredData(2));
                    likes.setColorFilter(android.R.color.darker_gray);
                    walls.setColorFilter(null);
                    downlaods.setColorFilter(android.R.color.darker_gray);
                    increaseScale(walls);
                    decreaseScale(downlaods);
                    decreaseScale(likes);
                }
                break;
            case R.id.tabLayout:
                break;
        }
    }

    private void increaseScale(ImageView likes) {
        SpringAnimation springAnimation = new SpringAnimation(likes, DynamicAnimation.SCALE_X, 1.2f);
        SpringAnimation springAnimation2 = new SpringAnimation(likes, DynamicAnimation.SCALE_Y, 1.2f);
        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation2.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.start();
        springAnimation2.start();
    }

    private void decreaseScale(ImageView likes) {
        SpringAnimation springAnimation = new SpringAnimation(likes, DynamicAnimation.SCALE_X, 0.8f);
        SpringAnimation springAnimation2 = new SpringAnimation(likes, DynamicAnimation.SCALE_Y, 0.8f);
        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation2.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.start();
        springAnimation2.start();
    }

    private ArrayList<ResponseUserImageClass> getFilteredData(int val) {
        ArrayList<ResponseUserImageClass> arrayList = new ArrayList<>();
        if (null != resUserFavImagesFromApi) {
            switch (val) {
                case 1: {
                    for (ResponseUserImageClass image : resUserFavImagesFromApi) {
                        if (image.getDownloaded()) {
                            arrayList.add(image);
                        }
                        runLayoutAnimation(verticalRv);
                    }
                }
                break;
                case 2: {
                    for (ResponseUserImageClass image : resUserFavImagesFromApi) {
                        if (image.getWallSet()) {
                            arrayList.add(image);
                        }
                        runLayoutAnimation(verticalRv);

                    }
                }
                break;
                default: {
                    for (ResponseUserImageClass image : resUserFavImagesFromApi) {
                        if (image.getLiked()) {
                            arrayList.add(image);
                        }
                        runLayoutAnimation(verticalRv);

                    }
                }
                break;
            }
        }
        return arrayList;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_climb_up);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    @OnClick(R.id.loginButton)
    public void onViewClicked() {
    }
}
