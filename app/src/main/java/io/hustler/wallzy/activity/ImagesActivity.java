package io.hustler.wallzy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.ImagesAdapter;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;

public class ImagesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cdl)
    CoordinatorLayout cdl;
    @BindView(R.id.images_rv)
    RecyclerView imagesRv;
    @BindView(R.id.bg_blur_image)
    ImageView bgBlurImage;

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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String catName = getIntent().getStringExtra(Constants.INTENT_CAT_NAME);
        String catImage = getIntent().getStringExtra(Constants.INTENT_CAT_IMAGE);

        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
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
            }
        });
//        DatabaseReference databaseReference = firebaseDatabase.getReference("CategoryImages").child(catName);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference databaseReference = firebaseDatabase.getReference("CollectionImages").child(catName);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            ArrayList<String> url_ArrayList = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                url_ArrayList.add(snapshot.getValue(String.class));
                            }
                            ImagesAdapter imagesAdapter = new ImagesAdapter(ImagesActivity.this, new ImagesAdapter.OnItemClcikListener() {
                                @Override
                                public void onItemClick(int position) {
                                    MessageUtils.showDismissableSnackBar(ImagesActivity.this, imagesRv, position + "");
                                }
                            }, url_ArrayList);
                            imagesRv.setLayoutManager(new GridLayoutManager(ImagesActivity.this, 3));
                            imagesRv.setAdapter(imagesAdapter);
                            int resId = R.anim.layout_anim_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ImagesActivity.this, resId);
                            imagesRv.setLayoutAnimation(animation);

                        } else {
                            MessageUtils.showDismissableSnackBar(ImagesActivity.this, imagesRv, "No Images available");
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }, 600);
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
