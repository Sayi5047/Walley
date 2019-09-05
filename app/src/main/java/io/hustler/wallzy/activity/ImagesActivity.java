package io.hustler.wallzy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.ImagesAdapter;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.customviews.PaginationScrollListeners;
import io.hustler.wallzy.model.wallzy.request.ReqGetCollectionorCategoryImages;
import io.hustler.wallzy.model.wallzy.response.ResGetCategoryImages;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;

public class ImagesActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cdl)
    CoordinatorLayout cdl;
    @BindView(R.id.images_rv)
    RecyclerView imagesRv;
    @BindView(R.id.bg_blur_image)
    ImageView bgBlurImage;

    private final int PAGE_START = 0;
    int totalItemCount = 0;
    boolean isLoading = false;
    boolean isLastPage = false;
    private int currentpage = PAGE_START;
    private int TOTAL_PAGES = 0;
    private String catName;
    String catImage;
    Integer catId;
    ImagesAdapter imagesAdapter;


    PaginationScrollListeners paginationScrollListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ImagesActivity.this, 3);
        imagesRv.setLayoutManager(gridLayoutManager);
        catName = getIntent().getStringExtra(Constants.INTENT_CAT_NAME);
        catImage = getIntent().getStringExtra(Constants.INTENT_CAT_IMAGE);
        catId = getIntent().getIntExtra(Constants.INTENT_CAT_ID, 0);

        paginationScrollListeners = new PaginationScrollListeners(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentpage += 1;
                // TODO: 05-09-2019 call Api For More Data
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
        imagesRv.addOnScrollListener(paginationScrollListeners);
        loadDataForFirstTime();


        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.getDiskExecutor().execute(() -> {
            try {
                Bitmap checfBitMap = null;
                checfBitMap = Glide.with(ImagesActivity.this).asBitmap().load(catImage).submit().get();
                Bitmap finalChecfBitMap = checfBitMap;
                appExecutor.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        applyBlur(finalChecfBitMap, ImagesActivity.this);

                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadMoreDataItems() {

        isLoading = true;
        ReqGetCollectionorCategoryImages reqGetCollectionorCategoryImages = new ReqGetCollectionorCategoryImages();
        reqGetCollectionorCategoryImages.setId(catId);
        reqGetCollectionorCategoryImages.setPageNumber(currentpage);
        new RestUtilities().getCategoryImages(getApplicationContext(), reqGetCollectionorCategoryImages, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                isLoading = false;
                ResGetCategoryImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetCategoryImages.class);
                TOTAL_PAGES = resGetCategoryImages.getTotalPages();
                if (resGetCategoryImages.isApiSuccess()) {
                    if (null != imagesAdapter) {
                        imagesAdapter.AddData(resGetCategoryImages.getImages());
                    }
                    if (currentpage >= TOTAL_PAGES) {
                        isLastPage = true;
                        MessageUtils.showShortToast(ImagesActivity.this, "REACHED END");

                    }
                    MessageUtils.showShortToast(ImagesActivity.this, "PAGINATION ADDED MORE ITEMS");
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

    private void loadDataForFirstTime() {
        isLoading = true;
        ReqGetCollectionorCategoryImages reqGetCollectionorCategoryImages = new ReqGetCollectionorCategoryImages();
        reqGetCollectionorCategoryImages.setId(catId);
        reqGetCollectionorCategoryImages.setPageNumber(0);
        new RestUtilities().getCategoryImages(getApplicationContext(), reqGetCollectionorCategoryImages, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                isLoading = false;
                ResGetCategoryImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetCategoryImages.class);
                TOTAL_PAGES = resGetCategoryImages.getTotalPages();
                if (resGetCategoryImages.isApiSuccess()) {
                    imagesAdapter = new ImagesAdapter(ImagesActivity.this, new ImagesAdapter.OnItemClcikListener() {
                        @Override
                        public void onItemClick(int position) {
                            // TODO: 06-09-2019 SHOW IMAGE
                            MessageUtils.showDismissableSnackBar(ImagesActivity.this, imagesRv, position + "");
                        }
                    }, resGetCategoryImages.getImages());
                    imagesRv.setAdapter(imagesAdapter);
                    int resId = R.anim.layout_anim_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ImagesActivity.this, resId);
                    imagesRv.setLayoutAnimation(animation);
                    if (currentpage >= TOTAL_PAGES) {
                        isLastPage = true;
                        MessageUtils.showShortToast(ImagesActivity.this, "REACHED END");

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

    public void applyBlur(Bitmap bitmap, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(ImagesActivity.this).asBitmap().centerCrop().load(ImageProcessingUtils.create_blur(bitmap, 25.0f, context)).into(bgBlurImage);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();

    }
}
