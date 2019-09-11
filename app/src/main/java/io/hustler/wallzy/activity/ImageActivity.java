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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;

public class ImageActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.info_btn)
    ImageView infoBtn;
    @BindView(R.id.like)
    ImageView like;
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
        infoHeaight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        backWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72, getResources().getDisplayMetrics());

        String url = getIntent().getStringExtra(Constants.INTENT_CAT_IMAGE);
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
        Glide.with(this).load(url).into(image);
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
        if(isInfoShown){
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


    }

    public void showInfo() {
        ValueAnimator valueAnimator
                = ValueAnimator.ofInt(0, (int) infoHeaight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = infoLayout.getLayoutParams();
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
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
