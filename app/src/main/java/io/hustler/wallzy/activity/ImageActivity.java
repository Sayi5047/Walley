package io.hustler.wallzy.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;

public class ImageActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.image)
    TouchImageView image;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.like)
    ImageView like;
    @BindView(R.id.download)
    ImageView download;
    @BindView(R.id.wallpaper)
    ImageView wallpaper;
    @BindView(R.id.info)
    ImageView info;
    @BindView(R.id.optionsLayout)
    LinearLayout optionsLayout;
    float optionsWidth;
    float backWidth;

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
        backWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());

        String url = getIntent().getStringExtra(Constants.INTENT_CAT_IMAGE);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (back.getAlpha() <= 0.0f) {
                    showAlpha();
                } else {
                    hideAlpha();

                }
            }
        });
        Glide.with(this).load(url).into(image);
    }

    private void hideAlpha() {
        ValueAnimator optionsLayoutAnimtor = ValueAnimator.ofInt((int) optionsWidth, 0);
        ValueAnimator backButtonValueAnimator = ValueAnimator.ofInt((int) backWidth,0);

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
        });
        optionsLayoutAnimtor.setDuration(300);
        backButtonValueAnimator.setDuration(300);
        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(optionsLayout, "alpha", 0.0f);
        optionsLayoutAnima.setDuration(300); // duration 3 seconds
        ObjectAnimator backBtnAnim = ObjectAnimator.ofFloat(back, "alpha", 0.0f);
        backBtnAnim.setDuration(300); // duration 3 seconds
        
        optionsLayoutAnima.start();
        backBtnAnim.start();
        optionsLayoutAnimtor.start();
        backButtonValueAnimator.start();
    }

    private void showAlpha() {
        ValueAnimator optionsValueAnimator = ValueAnimator.ofInt(0, (int) optionsWidth);
        ValueAnimator backButtonValueAnimator = ValueAnimator.ofInt(0, (int) backWidth);
        optionsValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = optionsLayout.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            optionsLayout.requestLayout();
        });
        backButtonValueAnimator.addUpdateListener(valueAnimator -> {
            ViewGroup.LayoutParams layoutParams = back.getLayoutParams();
            layoutParams.width = ((Integer) valueAnimator.getAnimatedValue());
            back.requestLayout();
        });
        optionsValueAnimator.setDuration(300);
        backButtonValueAnimator.setDuration(300);
        
        ObjectAnimator optionsLayoutAnima = ObjectAnimator.ofFloat(optionsLayout, "alpha", 1.0f);
        optionsLayoutAnima.setDuration(300); // duration 3 seconds
        ObjectAnimator backBtnAnim = ObjectAnimator.ofFloat(back, "alpha", 1.0f);
        backBtnAnim.setDuration(300); // duration 3 seconds

        optionsLayoutAnima.start();
        backBtnAnim.start();
        optionsValueAnimator.start();
        backButtonValueAnimator.start();

    }


    @OnClick({R.id.back, R.id.like, R.id.download, R.id.wallpaper, R.id.info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.like:
                break;
            case R.id.download:
                break;
            case R.id.wallpaper:
                break;
            case R.id.info:
                break;
        }
    }
}
