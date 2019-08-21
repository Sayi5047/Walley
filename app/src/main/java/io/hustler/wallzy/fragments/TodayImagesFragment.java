package io.hustler.wallzy.fragments;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.Services.DownloadImageJobService;
import io.hustler.wallzy.adapters.HorizantalImagesAdapter;
import io.hustler.wallzy.constants.Constants;
import io.hustler.wallzy.customviews.HorizntalRecyclerView;
import io.hustler.wallzy.utils.ImageProcessingUtils;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.PermissionUtils;

public class TodayImagesFragment extends Fragment {
    @BindView(R.id.item_list)
    HorizntalRecyclerView itemList;
    @BindView(R.id.blur_image)
    ImageView blurImage;
    @BindView(R.id.fab_download_image)
    FloatingActionButton fabDownloadImage;
    @BindView(R.id.medianView)
    View medianView;
    @BindView(R.id.fab_set_wallpaper)
    FloatingActionButton fabSetWallpaper;


    private Driver driver;
    private FirebaseJobDispatcher firebaseJobDispatcher;
    private ArrayList<String> images;
    private String currentDisplayingImage = "";


    public static TodayImagesFragment getInstance() {
        return new TodayImagesFragment();

    }

    public TodayImagesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);
        if (!PermissionUtils.isStoragePermissionAvailable(Objects.requireNonNull(getActivity()))) {
            PermissionUtils.requestStoragrPermissions(getActivity(), Constants.MY_PERMISSION_REQUEST_STORAGE);
        }
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading Images");
        progressDialog.setMessage("Loading latest images.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        itemList = view.findViewById(R.id.item_list);
        blurImage = view.findViewById(R.id.blur_image);
        HorizantalImagesAdapter horizantalImagesAdapter = new HorizantalImagesAdapter(getActivity());
        itemList.intialize(horizantalImagesAdapter);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("LatestImages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images = new ArrayList<>();
                for (DataSnapshot snapshot : Objects.requireNonNull(dataSnapshot.getChildren())) {
                    progressDialog.cancel();
                    images.add(snapshot.getValue(String.class));
                    horizantalImagesAdapter.setData(images);
                }
                currentDisplayingImage = images.get(0);
                itemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            currentDisplayingImage = images.get(((LinearLayoutManager) Objects.requireNonNull(itemList.getLayoutManager())).findFirstVisibleItemPosition());
                        }
                    }
                });
//                itemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            String currentDisplayingImage = images.get(((LinearLayoutManager) Objects.requireNonNull(itemList.getLayoutManager())).findFirstVisibleItemPosition());
////            Glide.with(Objects.requireNonNull(getActivity()))
////                    .load()
////                    .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(blurImage);
////            blurImage.setDrawingCacheEnabled(true);
////            blurImage.buildDrawingCache();
////            if (!blurImage.getDrawingCache().isRecycled()) {
////                blurImage.destroyDrawingCache();
////            }
//
//                            Bitmap bitmap;
//                            try {
//                                URL url = new URL(currentDisplayingImage);
//                                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                applyBlur(bitmap);
//                            } catch (IOException e) {
//                                System.out.println(e);
//                            }
//
//                        }
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.cancel();
            }
        });

        return view;
    }

    public void applyBlur(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurImage.setImageBitmap(ImageProcessingUtils.create_blur(bitmap, 20.0f, getActivity()));
        }

    }

    @OnClick({R.id.fab_download_image, R.id.fab_set_wallpaper})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_download_image:
                if (PermissionUtils.isStoragePermissionAvailable(Objects.requireNonNull(getActivity()))) {
                    if (currentDisplayingImage != null) {
                        if (currentDisplayingImage.length() > 0) {
                            downloadImage(currentDisplayingImage);
                        }
                    }
                } else {
                    PermissionUtils.requestStoragrPermissions(getActivity(), Constants.MY_PERMISSION_REQUEST_STORAGE_FOR_DOWNLOAD_WALLPAPER);
                }
                break;
            case R.id.fab_set_wallpaper:
                if (PermissionUtils.isStoragePermissionAvailable(Objects.requireNonNull(getActivity()))) {
                    if (currentDisplayingImage != null) {
                        if (currentDisplayingImage.length() > 0) {
                            setWallPaper(currentDisplayingImage);
                        }
                    }
                } else {
                    PermissionUtils.requestStoragrPermissions(getActivity(), Constants.MY_PERMISSION_REQUEST_STORAGE_FOR_SETWALLPAPER);
                }

                break;
        }
    }

    private void downloadImage(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ImageUrl_to_download, url);
        bundle.putString(Constants.Image_Name_to_save_key, UUID.randomUUID().toString().substring(0, 9));
        bundle.putBoolean(Constants.is_to_setWallpaper_fromActivity, false);
        if (null == driver) {
            driver = new GooglePlayDriver(Objects.requireNonNull(getActivity()));
        }
        if (null == firebaseJobDispatcher) {
            firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        }
        firebaseJobDispatcher.cancel(Constants.DONWLOADIMAGE_IMAGE_JOB_TAG);
        Job downloadJob = firebaseJobDispatcher.
                newJobBuilder().
                setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(Constants.DONWLOADIMAGE_IMAGE_JOB_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);

    }

    private void setWallPaper(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ImageUrl_to_download, url);
        bundle.putString(Constants.Image_Name_to_save_key, UUID.randomUUID().toString().substring(0, 9));
        bundle.putBoolean(Constants.is_to_setWallpaper_fromActivity, true);
        if (null == driver) {
            driver = new GooglePlayDriver(Objects.requireNonNull(getActivity()));
        }
        if (null == firebaseJobDispatcher) {
            firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

        }
        firebaseJobDispatcher.cancel(Constants.SETWALLPAPER_IMAGE_TAG);
        Job downloadJob = firebaseJobDispatcher
                .newJobBuilder()
                .setService(DownloadImageJobService.class)
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.NOW)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(bundle)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(Constants.SETWALLPAPER_IMAGE_TAG)
                .build();
        firebaseJobDispatcher.mustSchedule(downloadJob);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSION_REQUEST_STORAGE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showStorageErrorMessage();
                }
            }
            break;
            case Constants.MY_PERMISSION_REQUEST_STORAGE_FOR_DOWNLOAD_WALLPAPER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fabDownloadImage.performClick();

                } else {
                    showStorageErrorMessage();
                }

            }
            break;
            case Constants.MY_PERMISSION_REQUEST_STORAGE_FOR_SETWALLPAPER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fabSetWallpaper.performClick();

                } else {
                    showStorageErrorMessage();
                }
            }
            break;
        }

    }

    private void showStorageErrorMessage() {
        MessageUtils.showDismissableSnackBar(Objects.requireNonNull(getActivity()), getView(), getString(R.string.storage_permission_rejected_message));
    }
}
