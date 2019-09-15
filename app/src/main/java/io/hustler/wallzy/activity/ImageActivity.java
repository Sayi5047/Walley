package io.hustler.wallzy.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.BuildConfig;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.Services.DownloadImageJobService;
import io.hustler.wallzy.constants.ServerConstants;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.model.wallzy.request.ReqUserImage;
import io.hustler.wallzy.model.wallzy.response.ResLoginUser;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.SharedPrefsUtils;
import io.hustler.wallzy.utils.TextUtils;

public class ImageActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.root)
    ConstraintLayout constraintLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.info_btn)
    ImageView infoBtn;
    @BindView(R.id.like)
    LottieAnimationView like;
    @BindView(R.id.download)
    ImageView download;
    @BindView(R.id.wallpaper)
    ImageView wallpaper;
    @BindView(R.id.report)
    ImageView reportBtn;
    @BindView(R.id.optionsLayout)
    LinearLayout optionsLayout;
    float optionsWidth, infoHeaight;
    float backWidth;
    boolean isInfoShown = false;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.likes)
    TextView likes;
    @BindView(R.id.downloads)
    TextView downloads;
    @BindView(R.id.walls)
    TextView walls;
    @BindView(R.id.imageAnalyticsLayout)
    LinearLayout imageAnalyticsLayout;
    @BindView(R.id.artistInfoHead)
    TextView artistInfoHead;
    @BindView(R.id.artistName)
    TextView artistName;
    @BindView(R.id.artistImage)
    ImageView artistImage;
    @BindView(R.id.artistCreditLink)
    ImageView artistCreditLink;
    @BindView(R.id.artistInforLayout)
    RelativeLayout artistInforLayout;
    @BindView(R.id.CopyRightsHead)
    TextView CopyRightsHead;
    @BindView(R.id.creditsData)
    TextView creditsData;
    @BindView(R.id.info_layout)
    RelativeLayout infoLayout;
    String url;
    ResponseImageClass responseImageClass;
    ResLoginUser resLoginUser;
    boolean isLiked = false;
    SharedPrefsUtils sharedPrefsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.bind(this);
        optionsWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 255, getResources().getDisplayMetrics());
        infoHeaight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        backWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());
        TextUtils.findText_and_applyTypeface(constraintLayout, ImageActivity.this);
        url = getIntent().getStringExtra(WallZyConstants.INTENT_CAT_IMAGE);
        sharedPrefsUtils = new SharedPrefsUtils(getApplicationContext());
        String serialisedObject = getIntent().getStringExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE);
        responseImageClass = new Gson().fromJson(serialisedObject, ResponseImageClass.class);
        resLoginUser = new Gson().fromJson(sharedPrefsUtils.getString(WallZyConstants.SP_USERDATA_KEY), ResLoginUser.class);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (back.getAlpha() <= 0.0f) {
                    showScreenViews();
                } else {
                    hideScreenViews();

                }
            }
        });
        hideScreenViews();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showScreenViews();
            }
        }, 1000);
        fillViews(url);
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void fillViews(String url) {
        getisUserLikd();
        Glide.with(this).asBitmap().load(url).into(image);
        AppExecutor.getInstance().getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitchef = null;
                try {
                    bitchef = Glide.with(ImageActivity.this).asBitmap().load(url).submit().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Bitmap finalChecfBitMap = bitchef;
                AppExecutor.getInstance().getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        applyBlur(finalChecfBitMap, ImageActivity.this);

                    }
                });
            }
        });

        likes.setText(String.valueOf(responseImageClass.getLikes()));
        downloads.setText(String.valueOf(responseImageClass.getDownloads()));
        walls.setText(String.valueOf(responseImageClass.getDislikes()));
        name.setText(null == responseImageClass.getName() ? "NA" : responseImageClass.getName());
        artistName.setText(null == responseImageClass.getArtistName() ? "NA" : responseImageClass.getArtistName());
        if (null != responseImageClass.getArtsistImage()) {
            Glide.with(this).load(responseImageClass.getArtsistImage()).into(artistImage);
        }
        if (null != responseImageClass.getArtistBackLink()) {
            artistCreditLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(responseImageClass.getArtistBackLink());
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.instagram.android");

                    if (isIntentAvailable(ImageActivity.this, insta)) {
                        startActivity(insta);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(responseImageClass.getArtistBackLink())));
                    }


                }
            });
        } else {
            artistCreditLink.setVisibility(View.GONE);
        }


    }

    @OnClick({R.id.back, R.id.like, R.id.download, R.id.wallpaper, R.id.report, R.id.info_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.info_btn:
                if (isInfoShown) {
                    hideInfo();
                } else {
                    showInfo();
                }
                break;
            case R.id.like:
                handleLikeButton();
                like.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        callLikeApi();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                break;
            case R.id.download:
                new MessageUtils().showBinaryAlertDialog(ImageActivity.this,
                        "Downloading",
                        "Wallpaper will be downloaded in the background. Meanwhile why don't you watch an ad and do us favour",
                        R.raw.lottie_download,
                        "Watch",
                        "No Thanks",
                        0,
                        new MessageUtils.BinaryClickListener() {
                            @Override
                            public void onPositiveClick() {
                                MessageUtils.showShortToast(ImageActivity.this, "Ad is Shown");
                            }

                            @Override
                            public void onNegativeClick() {
                                MessageUtils.showShortToast(ImageActivity.this, "Dialog dismissed");

                            }
                        }

                );
                downloadImage(responseImageClass.getUrl());
                callDownlaodApi();
                break;
            case R.id.wallpaper:
                new MessageUtils().showBinaryAlertDialog(ImageActivity.this,
                        "Applying Wallpaper",
                        "Wallpaper will be downloaded and applied in the background. Meanwhile why don't you watch an ad and do us favour",
                        R.raw.lottie_download,
                        "Watch",
                        "No Thanks",
                        0,
                        new MessageUtils.BinaryClickListener() {
                            @Override
                            public void onPositiveClick() {
                                MessageUtils.showShortToast(ImageActivity.this, "Ad is Shown");
                            }

                            @Override
                            public void onNegativeClick() {
                                MessageUtils.showShortToast(ImageActivity.this, "Dialog dismissed");

                            }
                        }

                );
                setWallPaper(responseImageClass.getUrl());
                callWallApi();
                break;
            case R.id.report:
                new MessageUtils().showBinaryAlertDialog(ImageActivity.this,
                        "Report Image",
                        "If you think this image violates copyrights law in any manner.Please, Email us with full details \nReported image will be removed upon review \nAre sure you want to report this Image.",
                        R.raw.lottie_report,
                        "Report",
                        "Email",
                        0,
                        new MessageUtils.BinaryClickListener() {
                            @Override
                            public void onPositiveClick() {
                                callreportApi();
                            }

                            @Override
                            public void onNegativeClick() {
                                launchEmail();

                            }
                        }

                );
                break;
        }
    }

    private void launchEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "Quotzyapp@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wallzy Image Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "===================" +
                "\n" +
                "I Have problem with Image named " + responseImageClass.getName() + "And " + responseImageClass.getId() +
                "\n" +
                " Do not delete above data, required for further processing. Please, write your message below+" +
                "\n" +
                "+===================");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void handleLikeButton() {
        if (isLiked) {
            like.reverseAnimationSpeed();
            like.playAnimation();
            isLiked = false;
        } else {
            like.playAnimation();
            isLiked = true;
        }
    }

    private void getisUserLikd() {
        ReqUserImage reqUserImage = new ReqUserImage();
        reqUserImage.setImageId(responseImageClass.getId());
        reqUserImage.setUserId(resLoginUser.getId());
        reqUserImage.setOrigin("ANDROID");
        reqUserImage.setVersion("1.0");
        new RestUtilities().isImageLiked(ImageActivity.this.getApplicationContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image get islike Failed due to" + baseResponse.getMessage());

                } else if (baseResponse.getStatuscode() == ServerConstants.USER_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image islike Failed due to" + baseResponse.getMessage());

                } else {
                    Log.i(TAG, "onSuccess: Image isLike successful");
                    if (baseResponse.isApiSuccess()) {
                        isLiked = true;
                        like.playAnimation();
                    } else {
                        isLiked = false;
                    }
                }

            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImageActivity.this, image, error);
                Log.i(TAG, "onSuccess: Image like Failed due to" + error);
            }
        });

    }

    private void callLikeApi() {
        ReqUserImage reqUserImage = new ReqUserImage();
        reqUserImage.setImageId(responseImageClass.getId());
        reqUserImage.setUserId(resLoginUser.getId());
        reqUserImage.setOrigin("ANDROID");
        reqUserImage.setVersion("1.0");

        new RestUtilities().likeImage(ImageActivity.this.getApplicationContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image like Failed due to" + baseResponse.getMessage());

                } else if (baseResponse.getStatuscode() == ServerConstants.USER_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image like Failed due to" + baseResponse.getMessage());

                } else {
                    Log.i(TAG, "onSuccess: Image like successfull");
                }

            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImageActivity.this, image, error);
                Log.i(TAG, "onSuccess: Image like Failed due to" + error);
            }
        });
    }

    private void callDownlaodApi() {
        ReqUserImage reqUserImage = new ReqUserImage();
        reqUserImage.setImageId(responseImageClass.getId());
        reqUserImage.setUserId(resLoginUser.getId());
        reqUserImage.setOrigin("ANDROID");
        reqUserImage.setVersion("1.0");

        new RestUtilities().downloadImage(ImageActivity.this.getApplicationContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image Download Failed due to" + baseResponse.getMessage());

                } else if (baseResponse.getStatuscode() == ServerConstants.USER_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image Download Failed due to" + baseResponse.getMessage());

                } else {
                    Log.i(TAG, "onSuccess: Image Download successfull");
                }

            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImageActivity.this, image, error);
                Log.i(TAG, "onSuccess: Image Download Failed due to" + error);
            }
        });
    }

    private void callWallApi() {
        ReqUserImage reqUserImage = new ReqUserImage();
        reqUserImage.setImageId(responseImageClass.getId());
        reqUserImage.setUserId(resLoginUser.getId());
        reqUserImage.setOrigin("ANDROID");
        reqUserImage.setVersion("1.0");

        new RestUtilities().setWall(ImageActivity.this.getApplicationContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image Set wall Failed due to" + baseResponse.getMessage());

                } else if (baseResponse.getStatuscode() == ServerConstants.USER_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image Set wall Failed due to" + baseResponse.getMessage());

                } else {
                    Log.i(TAG, "onSuccess: Image Set wall successful");
                }

            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImageActivity.this, image, error);
                Log.i(TAG, "onSuccess: Image Set wall Failed due to" + error);
            }
        });
    }

    private void callreportApi() {
        ReqUserImage reqUserImage = new ReqUserImage();
        reqUserImage.setImageId(responseImageClass.getId());
        reqUserImage.setUserId(resLoginUser.getId());
        reqUserImage.setOrigin("ANDROID");
        reqUserImage.setVersion("1.0");

        new RestUtilities().reportImage(ImageActivity.this.getApplicationContext(), reqUserImage, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                if (baseResponse.getStatuscode() == ServerConstants.IMAGE_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image report Failed due to" + baseResponse.getMessage());

                } else if (baseResponse.getStatuscode() == ServerConstants.USER_UNAVAILABLE) {
                    showErrorFromAPi(baseResponse);
                    Log.i(TAG, "onSuccess: Image report Failed due to" + baseResponse.getMessage());

                } else {
                    MessageUtils.showShortToast(ImageActivity.this, "Image Reported Successfully.");
                    Log.i(TAG, "onSuccess: Image report successful");
                }

            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(ImageActivity.this, image, error);
                Log.i(TAG, "onSuccess: Image report Failed due to" + error);
            }
        });
    }

    private void showErrorFromAPi(BaseResponse baseResponse) {
        if (BuildConfig.DEBUG) {
            MessageUtils.showShortToast(ImageActivity.this, baseResponse.getMessage());
        }
    }


    /**
     * ANIMATOIN METHODS
     */
    private void hideScreenViews() {
        ValueAnimator optionsLayoutAnimtor = ValueAnimator.ofInt((int) optionsWidth, 0);
        ValueAnimator backButtonValueAnimator = ValueAnimator.ofInt((int) backWidth, 0);

        optionsLayoutAnimtor.addUpdateListener(valueAnimator1 -> {
            Log.i(TAG, "onAnimationUpdate: " + valueAnimator1.getAnimatedValue());
            ViewGroup.LayoutParams layoutParams = optionsLayout.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator1.getAnimatedValue());
            optionsLayout.requestLayout();
        });
        backButtonValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = back.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            back.requestLayout();

            ViewGroup.LayoutParams layoutParams2 = infoBtn.getLayoutParams();
            layoutParams2.width = ((Integer) valueAnimator.getAnimatedValue());
            infoBtn.requestLayout();
        });
        optionsLayoutAnimtor.setDuration(300);
        backButtonValueAnimator.setDuration(300);
        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(optionsLayout, "alpha", 0.0f);
        optionsLayoutAnima.setDuration(300); // duration 3 seconds
        ObjectAnimator backBtnAnim = ObjectAnimator.ofFloat(back, "alpha", 0.0f);
        backBtnAnim.setDuration(300); // duration 3 seconds
        ObjectAnimator infoBtnAnim = ObjectAnimator.ofFloat(infoBtn, "alpha", 0.0f);
        backBtnAnim.setDuration(300);
        optionsLayoutAnima.start();
        backBtnAnim.start();
        infoBtnAnim.start();
        optionsLayoutAnimtor.start();
        backButtonValueAnimator.start();
        if (isInfoShown) {
            hideInfo();
        }
    }

    private void showScreenViews() {
        ValueAnimator backButtonValueAnimator = ValueAnimator.ofInt(0, (int) backWidth);

        ValueAnimator optionsValueAnimator = ValueAnimator.ofInt(0, (int) optionsWidth);
        optionsValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = optionsLayout.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            optionsLayout.requestLayout();
        });
        backButtonValueAnimator.addUpdateListener(valueAnimator -> {

            ViewGroup.LayoutParams layoutParams = back.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            back.requestLayout();

            ViewGroup.LayoutParams layoutParams2 = infoBtn.getLayoutParams();
            layoutParams2.width = ((Integer) valueAnimator.getAnimatedValue());
            infoBtn.requestLayout();
        });

        optionsValueAnimator.setDuration(300);
        backButtonValueAnimator.setDuration(300);

        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(optionsLayout, "alpha", 1.0f);
        optionsLayoutAnima.setDuration(300);
        ObjectAnimator backBtnAnim = ObjectAnimator.ofFloat(back, "alpha", 1.0f);
        backBtnAnim.setDuration(300);
        ObjectAnimator infoBtnAnim = ObjectAnimator.ofFloat(infoBtn, "alpha", 1.0f);
        backBtnAnim.setDuration(300);

        optionsLayoutAnima.start();
        backBtnAnim.start();
        infoBtnAnim.start();
        optionsValueAnimator.start();
        backButtonValueAnimator.start();


    }

    public void showInfo() {
        optionsLayout.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            optionsLayout.setElevation(4f);
        }
        ValueAnimator valueAnimator
                = ValueAnimator.ofInt(0, (int) infoHeaight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = infoLayout.getLayoutParams();
                layoutParams.height = (int) valueAnimator.getAnimatedValue();


                if (valueAnimator.getAnimatedFraction() < 0.5) {
                    Log.i(TAG, "onAnimationUpdate: Animated value" + valueAnimator.getAnimatedValue());
                    Log.i(TAG, "onAnimationUpdate: Animated fraction" + valueAnimator.getAnimatedFraction());
                    Log.i(TAG, "onAnimationUpdate: translate Animated fraction" + (-valueAnimator.getAnimatedFraction() * 1000));
                    image.setScaleX(1 - valueAnimator.getAnimatedFraction());
                    image.setScaleY(1 - valueAnimator.getAnimatedFraction());
                    SpringAnimation springAnimation = new SpringAnimation(image, DynamicAnimation.TRANSLATION_Y, -valueAnimator.getAnimatedFraction() * 1000);
                    springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
                    springAnimation.start();

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image.setElevation(32f);
                }
                infoLayout.requestLayout();
            }
        });
        valueAnimator.setDuration(300);
        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(infoLayout, "alpha", 1.0f);
        optionsLayoutAnima.setDuration(300);
        optionsLayoutAnima.start();
        valueAnimator.start();
        isInfoShown = true;

    }

    public void hideInfo() {
        optionsLayout.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            optionsLayout.setElevation(0f);
        }
        ValueAnimator valueAnimator
                = ValueAnimator.ofInt((int) infoHeaight, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = infoLayout.getLayoutParams();
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                if (valueAnimator.getAnimatedFraction() > 0.5) {
                    image.setScaleX(valueAnimator.getAnimatedFraction());
                    image.setScaleY(valueAnimator.getAnimatedFraction());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image.setTranslationZ((valueAnimator.getAnimatedFraction() * 1000) - 1000);
                    }
                    //                    new  SpringAnimation(image, DynamicAnimation.TRANSLATION_Y, (valueAnimator.getAnimatedFraction() * 1000) - 1000).start();

                    image.setTranslationY((valueAnimator.getAnimatedFraction() * 1000) - 1000);

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image.setElevation(0f);
                }
                infoLayout.requestLayout();
            }
        });
        valueAnimator.setDuration(300);
        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(infoLayout, "alpha", 0.0f);
        optionsLayoutAnima.setDuration(300);
        optionsLayoutAnima.start();
        valueAnimator.start();
        isInfoShown = false;

    }

    public void applyBlur(Bitmap bitmap, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            constraintLayout.setBackground((new BitmapDrawable(getResources(), ImageProcessingUtils.create_blur(bitmap, 25.0f, context))));

        }

    }

    /**
     * ANIMATION MMETHOD ENDS
     */

    /*File Methods*/
    private void downloadImage(String url) {
        Driver driver;
        FirebaseJobDispatcher firebaseJobDispatcher;
        Bundle bundle = new Bundle();
        bundle.putString(WallZyConstants.ImageUrl_to_download, url);
        bundle.putString(WallZyConstants.Image_Name_to_save_key, UUID.randomUUID().toString().substring(0, 9));
        bundle.putBoolean(WallZyConstants.is_to_setWallpaper_fromActivity, false);
        driver = new GooglePlayDriver(Objects.requireNonNull(ImageActivity.this));
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        firebaseJobDispatcher.cancel(WallZyConstants.DONWLOADIMAGE_IMAGE_JOB_TAG);
        Job downloadJob = firebaseJobDispatcher.
                newJobBuilder().
                setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(WallZyConstants.DONWLOADIMAGE_IMAGE_JOB_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);

    }

    private void setWallPaper(String url) {
        Driver driver;
        FirebaseJobDispatcher firebaseJobDispatcher;
        Bundle bundle = new Bundle();
        bundle.putString(WallZyConstants.ImageUrl_to_download, url);
        bundle.putString(WallZyConstants.Image_Name_to_save_key, UUID.randomUUID().toString().substring(0, 9));
        bundle.putBoolean(WallZyConstants.is_to_setWallpaper_fromActivity, true);
        driver = new GooglePlayDriver(Objects.requireNonNull(ImageActivity.this));
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        firebaseJobDispatcher.cancel(WallZyConstants.SETWALLPAPER_IMAGE_TAG);
        Job downloadJob = firebaseJobDispatcher
                .newJobBuilder()
                .setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.NOW)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(WallZyConstants.SETWALLPAPER_IMAGE_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);
    }


}
