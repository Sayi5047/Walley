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
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;
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
    LottieAnimationView download;
    @BindView(R.id.wallpaper)
    LottieAnimationView wallpaper;
    @BindView(R.id.report)
    LottieAnimationView reportBtn;
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
    boolean isLiked = false;

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
        String serialisedObject = getIntent().getStringExtra(WallZyConstants.INTENT_SERIALIZED_IMAGE);
        responseImageClass = new Gson().fromJson(serialisedObject, ResponseImageClass.class);
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
        fillViews(url);
    }

    private void fillViews(String url) {
        Glide.with(this).load(url).into(image);
        likes.setText("Likes \n" + responseImageClass.getLikes());
        downloads.setText("Downloads \n" + responseImageClass.getDownloads());
        walls.setText("Walls \n" + responseImageClass.getDislikes());
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

    public void applyBlur(Bitmap bitmap, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            infoLayout.setBackground(new BitmapDrawable(getResources(), ImageProcessingUtils.create_blur(bitmap, 25.0f, context)));

        }

    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

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
            ViewGroup.LayoutParams layoutParams2 = reportBtn.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            layoutParams2.width = ((Integer) valueAnimator.getAnimatedValue());
            back.requestLayout();
            reportBtn.requestLayout();
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
        ValueAnimator optionsValueAnimator = ValueAnimator.ofInt(0, (int) optionsWidth);
        ValueAnimator backButtonValueAnimator = ValueAnimator.ofInt(0, (int) backWidth);
        optionsValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = optionsLayout.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            optionsLayout.requestLayout();
        });
        backButtonValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = back.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = reportBtn.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            layoutParams2.width = ((Integer) valueAnimator.getAnimatedValue());
            back.requestLayout();
            reportBtn.requestLayout();
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

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Drawable drawable = null;
//
//            drawable = getDrawable(R.drawable.bg_rounded_rect_top);
//            Bitmap mutableBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(mutableBitmap);
//            drawable.setBounds(0, 0, image.getRight(), image.getBottom());
//            drawable.draw(canvas);
//            applyBlur(mutableBitmap, ImageActivity.this);
//
//        }

    }

    public void showInfo() {
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
                    springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
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
                if (isLiked) {
                    like.reverseAnimationSpeed();
                    like.playAnimation();
                    isLiked = false;
                } else {
                    like.playAnimation();
                    isLiked=true;
                }
                like.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        MessageUtils.showShortToast(ImageActivity.this, "Like Api Called");
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
                break;
            case R.id.wallpaper:
                break;
            case R.id.report:
                break;
        }
    }

    @OnClick(R.id.artistCreditLink)
    public void onViewClicked() {
    }
}
