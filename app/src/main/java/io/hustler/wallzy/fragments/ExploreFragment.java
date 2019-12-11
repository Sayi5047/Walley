package io.hustler.wallzy.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.SingleImageActivity;
import io.hustler.wallzy.adapters.ImagesAdapter;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.customviews.StaggeredGridPaginationScrollListener;
import io.hustler.wallzy.model.wallzy.response.ResGetAllImages;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.DimenUtils;
import io.hustler.wallzy.utils.MessageUtils;

public class ExploreFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.imagesrv)
    RecyclerView imagesrv;
    @BindView(R.id.loadingAnimation)
    LottieAnimationView loadingAnimation;


    private StaggeredGridLayoutManager gridLayoutManager;
    private ImagesAdapter latestImagesAdapter;
    private final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int TOTAL_PAGES = 0;
    private Unbinder unbinder;
    private AppExecutor appExecutor;

    public static ExploreFragment getInstance() {
        return new ExploreFragment();

    }

    public ExploreFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, view);
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        appExecutor = AppExecutor.getInstance();
        imagesrv.setLayoutManager(gridLayoutManager);
        imagesrv.setNestedScrollingEnabled(true);
        stopLoadingAnimation();
        StaggeredGridPaginationScrollListener gridPaginationScrollListener = new StaggeredGridPaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadMoreDataItems();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadDataForFirstTime();
                imagesrv.addOnScrollListener(gridPaginationScrollListener);

            }
        }, 600);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadDataForFirstTime() {
        showLoadingANimtion();
        isLoading = true;
        if (null != getActivity()) {
            new RestUtilities().getAllImages(getActivity(), 0, new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    stopLoadingAnimation();
                    isLoading = false;
                    ResGetAllImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllImages.class);
                    TOTAL_PAGES = resGetCategoryImages.getTotalCount();
                    if (resGetCategoryImages.isApiSuccess()) {
                        appExecutor.getMainThreadExecutor().execute(() -> {
                            latestImagesAdapter = new ImagesAdapter(getActivity(), responseImageClass -> {
                                Intent intent = new Intent(getActivity(), SingleImageActivity.class);
                                intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, responseImageClass.getUrl());
                                intent.putExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE_CLASS, new Gson().toJson(responseImageClass));
                                intent.putExtra(WallZyConstants.INTENT_IS_FROM_SEARCH, false);
                                startActivity(intent);
                            }, resGetCategoryImages.getImages());
                            latestImagesAdapter.setHasStableIds(true);
                            imagesrv.setItemAnimator(null);
                            imagesrv.setAdapter(latestImagesAdapter);
                            if(null!=getActivity()){
                                int resId = R.anim.layout_anim_climb_up;
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                                imagesrv.setLayoutAnimation(animation);
                            }

                            if (currentPage >= TOTAL_PAGES) {
                                isLastPage = true;
                                MessageUtils.showShortToast(getActivity(), "REACHED END");

                            }
                        });
                    } else {
                        Log.e(TAG, "onSuccess: " + resGetCategoryImages.getMessage());

                    }
                }

                @Override
                public void onError(String error) {
                    stopLoadingAnimation();
                    isLoading = false;
                    Log.e(TAG, "onSuccess: " + error);

                }
            });
        }
//        appExecutor.getNetworkExecutor().execute(() -> {
//
//        });

    }

    private void stopLoadingAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt((int) DimenUtils.convertDptoPixels(64, Objects.requireNonNull(getActivity()).getResources()), 0);
                    valueAnimator.addUpdateListener(valueAnimator1 -> {
                        ViewGroup.LayoutParams layoutParams = loadingAnimation.getLayoutParams();
                        layoutParams.height = (int) valueAnimator1.getAnimatedValue();
                        loadingAnimation.requestLayout();
                    });
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            loadingAnimation.setVisibility(View.GONE);
                            loadingAnimation.pauseAnimation();
                        }
                    });
                    valueAnimator.setDuration(300);
                    valueAnimator.start();
                }
            }
        }, 2000);
    }

    private void showLoadingANimtion() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) DimenUtils.convertDptoPixels(64, getResources()));
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            ViewGroup.LayoutParams layoutParams = loadingAnimation.getLayoutParams();
            layoutParams.height = ((int) valueAnimator1.getAnimatedValue());
            loadingAnimation.requestLayout();
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
    }

    private void loadMoreDataItems() {
        showLoadingANimtion();
        isLoading = true;
        new RestUtilities().getAllImages(Objects.requireNonNull(getActivity()).getApplicationContext(), currentPage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                stopLoadingAnimation();
                isLoading = false;
                ResGetAllImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllImages.class);
                TOTAL_PAGES = resGetCategoryImages.getTotalCount();
                if (resGetCategoryImages.isApiSuccess()) {
                    if (null != latestImagesAdapter) {
                        appExecutor.getMainThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                latestImagesAdapter.AddAllData(resGetCategoryImages.getImages());

                                if (currentPage >= TOTAL_PAGES) {
                                    isLastPage = true;
                                    MessageUtils.showShortToast(getActivity(), "REACHED END");

                                }
                                // MessageUtils.showShortToast(getActivity(), "PAGINATION ADDED MORE ITEMS");
                            }
                        });
                    }

                } else {
                    Log.e(TAG, "onSuccess: " + resGetCategoryImages.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                stopLoadingAnimation();
                isLoading = false;
                Log.e(TAG, "onSuccess: " + error);


            }
        });

//        appExecutor.getNetworkExecutor().execute(() -> {
//           });
    }
}
