package io.hustler.wallzy.fragments;

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

import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.ImageActivity;
import io.hustler.wallzy.adapters.ImagesAdapter;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.customviews.StaggeredGridPaginationScrollListener;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.model.wallzy.response.ResGetAllImages;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;

public class ExploreFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.imagesrv)
    RecyclerView imagesrv;


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
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        appExecutor = AppExecutor.getInstance();
        imagesrv.setLayoutManager(gridLayoutManager);
        imagesrv.setHasFixedSize(true);
        imagesrv.setItemViewCacheSize(30);
        imagesrv.setNestedScrollingEnabled(true);
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
        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                if (null != getActivity()) {
                    new RestUtilities().getAllImages(getActivity(), 0, new RestUtilities.OnSuccessListener() {
                        @Override
                        public void onSuccess(Object onSuccessResponse) {
                            isLoading = false;
                            ResGetAllImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllImages.class);
                            TOTAL_PAGES = resGetCategoryImages.getTotalCount();
                            if (resGetCategoryImages.isApiSuccess()) {
                                appExecutor.getMainThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        latestImagesAdapter = new ImagesAdapter(getActivity(), new ImagesAdapter.OnItemClcikListener() {
                                            @Override
                                            public void onItemClick(ResponseImageClass responseImageClass) {
                                                Intent intent = new Intent(getActivity(), ImageActivity.class);
                                                intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, responseImageClass.getUrl());
                                                intent.putExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE,new Gson().toJson(responseImageClass));
                                                startActivity(intent); }
                                        }, resGetCategoryImages.getImages());
                                        latestImagesAdapter.setHasStableIds(true);
                                        imagesrv.setAdapter(latestImagesAdapter);
                                        int resId = R.anim.layout_anim_fall_down;
                                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                                        imagesrv.setLayoutAnimation(animation);
                                        if (currentPage >= TOTAL_PAGES) {
                                            isLastPage = true;
                                            MessageUtils.showShortToast(getActivity(), "REACHED END");

                                        }
                                    }
                                });
                            } else {
                                Log.e(TAG, "onSuccess: " + resGetCategoryImages.getMessage());

                            }
                        }

                        @Override
                        public void onError(String error) {
                            isLoading = false;
                            Log.e(TAG, "onSuccess: " + error);

                        }
                    });
                }
            }
        });

    }

    private void loadMoreDataItems() {
        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {
                isLoading = true;
                new RestUtilities().getAllImages(Objects.requireNonNull(getActivity()).getApplicationContext(), currentPage, new RestUtilities.OnSuccessListener() {
                    @Override
                    public void onSuccess(Object onSuccessResponse) {
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
                                        MessageUtils.showShortToast(getActivity(), "PAGINATION ADDED MORE ITEMS");
                                    }
                                });
                            }

                        } else {
                            Log.e(TAG, "onSuccess: " + resGetCategoryImages.getMessage());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        isLoading = false;
                        Log.e(TAG, "onSuccess: " + error);


                    }
                });
            }
        });
    }
}
