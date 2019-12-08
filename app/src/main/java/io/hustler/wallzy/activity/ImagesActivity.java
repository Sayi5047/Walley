package io.hustler.wallzy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.ImagesAdapter;
import io.hustler.wallzy.constants.FcmConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.customviews.StaggeredGridPaginationScrollListener;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.wallzy.request.ReqGetCollectionOrCategoryImages;
import io.hustler.wallzy.model.wallzy.request.ReqSubscribeTopic;
import io.hustler.wallzy.model.wallzy.response.ResGetCategoryImages;
import io.hustler.wallzy.model.wallzy.response.ResGetCollectionIMages;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;

public class ImagesActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private final String tag = this.getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cdl)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.images_rv)
    RecyclerView imagesRv;
    @BindView(R.id.bg_blur_image)
    ImageView bgBlurImage;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView((R.id.subscribeButton))
    Button subScribeButton;

    private static final int PAGE_START = 0;
    boolean isLoading = false;
    boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int totalPages = 0;
    String catImage;
    Long catId;
    String name;
    ImagesAdapter imagesAdapter;
    StaggeredGridPaginationScrollListener gridPaginationScrollListener;
    private boolean isCat;
    private long userId;
    private long topicId;
    private OnCompleteListener<Void> onCompleteListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        imagesRv.setLayoutManager(gridLayoutManager);
        onCompleteListener = task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "onComplete: User SuccessFully Subscribed ");
            }
        };
        setupToolBar();
        getIntentData();
        handleAppBarOffsetChanges();
        initiateScrollListener(gridLayoutManager);
        loadDataForFirstTime();
        blurTheBgImage();
        handleSubscribeButtonClick();
    }

    private void handleSubscribeButtonClick() {
        subScribeButton.setOnClickListener(view -> {
            ReqSubscribeTopic reqSubscribeTopic = new ReqSubscribeTopic();
            reqSubscribeTopic.setUserId(userId);
            reqSubscribeTopic.setTopicId(topicId);
            if (subScribeButton.getText().equals(getString(R.string.subscribe)))
                reqSubscribeTopic.setSubscribe(true);
            else reqSubscribeTopic.setSubscribe(false);
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subscribing");
            progressDialog.setMessage("Please, Wait");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
                if (i == KeyEvent.KEYCODE_BACK) {
                    MessageUtils.showShortToast(ImagesActivity.this, "Please,Wait...!");
                    return true;
                }
                return false;
            });
            progressDialog.show();
            new RestUtilities().subscribeToTopic(getApplicationContext(), reqSubscribeTopic, new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    progressDialog.cancel();
                    BaseResponse baseResponse = (new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class));
                    if (baseResponse.isApiSuccess()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(name).addOnCompleteListener(onCompleteListener);
                        MessageUtils.showDismissableSnackBar(ImagesActivity.this, subScribeButton, "Successfully subscribed ");
                        handleSubscribeButton(reqSubscribeTopic.isSubscribe());
                    } else {
                        MessageUtils.showDismissableSnackBar(ImagesActivity.this, subScribeButton, baseResponse.getMessage());
                    }
                }

                @Override
                public void onError(String error) {
                    progressDialog.cancel();
                    MessageUtils.showDismissableSnackBar(ImagesActivity.this, subScribeButton, error);
                }
            });
        });
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void handleAppBarOffsetChanges() {
        appBarLayout.addOnOffsetChangedListener((AppBarLayout appBarLayout1, int verticalOffset) -> {

            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() != 0) {
                float alphaValue = Float.parseFloat(new DecimalFormat("#.##").format(Math.abs((float) verticalOffset) / (float) appBarLayout.getTotalScrollRange()));
                subScribeButton.setAlpha(1 - alphaValue);
            } else {
                subScribeButton.setAlpha(1.0f);

            }
        });
    }

    private void initiateScrollListener(StaggeredGridLayoutManager gridLayoutManager) {
        gridPaginationScrollListener = new StaggeredGridPaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadMoreDataItems();
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
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
        imagesRv.addOnScrollListener(gridPaginationScrollListener);
    }

    private void blurTheBgImage() {
        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.getDiskExecutor().execute(() -> {
            try {
                Bitmap chefBitMap;
                chefBitMap = Glide.with(ImagesActivity.this).asBitmap().centerCrop().load(catImage).submit().get();
                Bitmap finalChefBitMap = chefBitMap;
                appExecutor.getMainThreadExecutor().execute(() -> applyBlur(finalChefBitMap, ImagesActivity.this));
            } catch (ExecutionException e) {
                Log.e(tag, String.format("onCreate: EXCEPTION%s", e.getMessage()));
            } catch (InterruptedException e) {
                Log.e(tag, String.format("onCreate: EXCEPTION%s", e.getMessage()));
                Thread.currentThread().interrupt();
            }
        });
    }

    private void getIntentData() {
        catImage = getIntent().getStringExtra(WallZyConstants.INTENT_CAT_IMAGE);
        catId = getIntent().getLongExtra(WallZyConstants.INTENT_CAT_ID, 0L);
        isCat = getIntent().getBooleanExtra(WallZyConstants.INTENT_IS_CAT, false);
        userId = getIntent().getLongExtra(WallZyConstants.INTENT_USER_ID, 0L);
        topicId = getIntent().getLongExtra(WallZyConstants.INTENT_TOPIC_ID, 0L);
        name = getIntent().getStringExtra(WallZyConstants.INTENT_TOPIC_NAME);
    }

    private void loadMoreDataItems() {
        isLoading = true;
        ReqGetCollectionOrCategoryImages reqGetCollectionorCategoryImages = new ReqGetCollectionOrCategoryImages();
        reqGetCollectionorCategoryImages.setId(catId);
        reqGetCollectionorCategoryImages.setPageNumber(currentPage);
        reqGetCollectionorCategoryImages.setUserId(userId);
        if (isCat) {
            callApiGetCategoryImages(reqGetCollectionorCategoryImages);
        } else {
            callApiGetCollectionImages(reqGetCollectionorCategoryImages);

        }


    }

    private void loadDataForFirstTime() {
        isLoading = true;
        ReqGetCollectionOrCategoryImages reqGetCollectionorCategoryImages = new ReqGetCollectionOrCategoryImages();
        reqGetCollectionorCategoryImages.setId(catId);
        reqGetCollectionorCategoryImages.setPageNumber(0);
        reqGetCollectionorCategoryImages.setUserId(userId);
        if (isCat) callApiGetCategoryImages(reqGetCollectionorCategoryImages);
        else callApiGetCollectionImages(reqGetCollectionorCategoryImages);

    }

    private void callApiGetCollectionImages(ReqGetCollectionOrCategoryImages reqGetCollectionorCategoryImages) {
        new RestUtilities().getCollectionImsges(getApplicationContext(), reqGetCollectionorCategoryImages, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                isLoading = false;
                ResGetCollectionIMages resGetCollectionIMages = new Gson().fromJson(onSuccessResponse.toString(), ResGetCollectionIMages.class);
                totalPages = resGetCollectionIMages.getTotalPages();
                if (resGetCollectionIMages.isApiSuccess()) {
                    handleSubscribeButton(resGetCollectionIMages.isSubscribed());
                    if (null == imagesAdapter) {
                        imagesAdapter = new ImagesAdapter(ImagesActivity.this, responseImageClass -> {
                            Intent intent = new Intent(ImagesActivity.this, SingleImageActivity.class);
                            intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, responseImageClass.getUrl());
                            intent.putExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE_CLASS, new Gson().toJson(responseImageClass));
                            startActivity(intent);
                        }, resGetCollectionIMages.getImages());
                        imagesRv.setAdapter(imagesAdapter);
                        int resId = R.anim.layout_anim_climb_up;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ImagesActivity.this, resId);
                        imagesRv.setLayoutAnimation(animation);
                        if (currentPage >= totalPages) {
                            isLastPage = true;
                            MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.thats_all_for_now));

                        }
                    } else {
                        imagesAdapter.AddData(resGetCollectionIMages.getImages());
                        if (currentPage >= totalPages) {
                            isLastPage = true;
                            MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.more_images_for_you));
                        }
                        MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.thats_all_for_now));
                    }

                } else {
                    Log.e(tag, String.format("onSuccess: %s", resGetCollectionIMages.getMessage()));

                }
            }

            @Override
            public void onError(String error) {
                isLoading = false;
                Log.e(tag, String.format("onSuccess: %s", error));

            }
        });
    }

    private void callApiGetCategoryImages(ReqGetCollectionOrCategoryImages reqGetCollectionorCategoryImages) {
        new RestUtilities().getCategoryImages(getApplicationContext(), reqGetCollectionorCategoryImages, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                isLoading = false;
                ResGetCategoryImages resGetCategoryImages = new Gson().fromJson(onSuccessResponse.toString(), ResGetCategoryImages.class);
                totalPages = resGetCategoryImages.getTotalPages();
                if (resGetCategoryImages.isApiSuccess()) {
                    handleSubscribeButton(resGetCategoryImages.isSubscribed());
                    if (null == imagesAdapter) {
                        imagesAdapter = new ImagesAdapter(ImagesActivity.this, responseImageClass -> {
                            Intent intent = new Intent(ImagesActivity.this, SingleImageActivity.class);
                            intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, responseImageClass.getUrl());
                            intent.putExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE_CLASS, new Gson().toJson(responseImageClass));
                            intent.putExtra(WallZyConstants.INTENT_IS_FROM_SEARCH, false);
                            startActivity(intent);
                        }, resGetCategoryImages.getImages());
                        imagesRv.setAdapter(imagesAdapter);
                        int resId = R.anim.layout_anim_climb_up;
                        imagesRv.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(ImagesActivity.this, resId));
                        if (currentPage >= totalPages) {
                            isLastPage = true;
                            MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.thats_all_for_now));
                        }
                    } else {
                        imagesAdapter.AddAllData(resGetCategoryImages.getImages());
                        if (currentPage >= totalPages) {
                            isLastPage = true;
                            MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.reache_end));
                        }
                        MessageUtils.showShortToast(ImagesActivity.this, getString(R.string.more_images_for_you));
                    }
                } else {
                    Log.e(tag, "onSuccess: " + resGetCategoryImages.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                isLoading = false;
                Log.e(tag, "onSuccess: " + error);

            }
        });
    }

    private void handleSubscribeButton(boolean isSubscribed) {

        if (isSubscribed) {
            subScribeButton.setText(R.string.unsubscribe);
            subScribeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect_negative));

        } else {
            subScribeButton.setText(R.string.subscribe);
            subScribeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_rect_positive));
            FirebaseMessaging.getInstance().unsubscribeFromTopic(FcmConstants.TEST_TOPIC).addOnCompleteListener(onCompleteListener);

        }
    }

    private void callApiSubscriptionManagement(ReqSubscribeTopic reqSubscribeTopic) {
        new RestUtilities().subscribeToTopic(getApplicationContext(), reqSubscribeTopic, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = (BaseResponse) onSuccessResponse;
                if (baseResponse.isApiSuccess()) {

                } else {
                    MessageUtils.showDismissableSnackBar(ImagesActivity.this, subScribeButton, baseResponse.getMessage());

                }
            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImagesActivity.this, subScribeButton, error);

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
