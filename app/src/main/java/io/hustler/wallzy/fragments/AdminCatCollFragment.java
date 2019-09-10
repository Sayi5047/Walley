package io.hustler.wallzy.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.R;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResCollectionClass;
import io.hustler.wallzy.model.imagekit.ResUploadImageToCdn;
import io.hustler.wallzy.model.wallzy.request.ReqAddCategory;
import io.hustler.wallzy.model.wallzy.request.ReqAddCollection;
import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;

import static android.app.Activity.RESULT_OK;

public class AdminCatCollFragment extends Fragment {
    @BindView(R.id.image4)
    ImageView artistImage;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.coversHead)
    TextView coversHead;
    @BindView(R.id.link_input_layout)
    EditText linkInputLayout;
    @BindView(R.id.text_artist_link_input_layout)
    TextInputLayout textArtistLinkInputLayout;
    @BindView(R.id.isArtist_check_box)
    CheckBox isArtistCheckBox;
    private int PICK_IMAGE_COLLECTION = 1;
    private ArrayList<String> selectedImagesArrayList;

    @BindView(R.id.uploadcatcollbtn)
    Button uploadcatcollbtn;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.imagesLayout)
    LinearLayout imagesLayout;
    @BindView(R.id.cat_btn_rd)
    RadioButton catBtnRd;
    @BindView(R.id.col_btn_rd)
    RadioButton colBtnRd;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.name_input_layout)
    EditText nameInputLayout;
    @BindView(R.id.text_input_layout)
    TextInputLayout textInputLayout;
    @BindView(R.id.upload_cat_coll_layout)
    RelativeLayout uploadCatCollLayout;
    private String TAG = this.getClass().getSimpleName();
    private boolean isCategory = true;
    private ArrayList<String> uploadedImagesUrl;
    private RestUtilities restUtilities = new RestUtilities();

    public AdminCatCollFragment() {
    }


    public static AdminCatCollFragment newInstance() {

        return new AdminCatCollFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_cat_coll, container, false);
        ButterKnife.bind(this, view);
        catBtnRd.setChecked(true);
        isArtistCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    artistImage.setVisibility(View.VISIBLE);
                    textArtistLinkInputLayout.setVisibility(View.VISIBLE);
                } else {
                    artistImage.setVisibility(View.GONE);
                    textArtistLinkInputLayout.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnClick({R.id.uploadcatcollbtn, R.id.imagesLayout, R.id.cat_btn_rd, R.id.col_btn_rd})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadcatcollbtn:

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Step 1 : Uploading");
                progressDialog.setMessage("Uploading to image to cdn");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_images_24dp));
                progressDialog.show();

                if (isCategory) {
                    String fileName = null;
                    String fileLocation = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fileLocation = selectedImagesArrayList.get(0);
                        fileName = Paths.get(fileLocation).getFileName().toString();

                    }

                    restUtilities.uploadImageToIK(fileLocation, fileName,
                            Objects.requireNonNull(getActivity()).getApplicationContext(),
                            new RestUtilities.OnSuccessListener() {
                                @Override
                                public void onSuccess(Object onSuccessResponse) {
                                    Log.i(TAG, "onSuccess: " + onSuccessResponse.toString());
                                    ResUploadImageToCdn response = new Gson().fromJson(onSuccessResponse.toString()
                                            , ResUploadImageToCdn.class);
                                    progressDialog.setTitle("Step 2 : Pushing to server");
                                    progressDialog.setMessage("Upload to cdn is done and pushing to server");
                                    ReqAddCategory reqAddCategory = new ReqAddCategory();
                                    BaseCategoryClass baseCategoryClass = new BaseCategoryClass();
                                    baseCategoryClass.setName(nameInputLayout.getText().toString().trim().toLowerCase());
                                    baseCategoryClass.setCover(response.getUrl());
                                    ArrayList<BaseCategoryClass> classArrayList = new ArrayList<>();
                                    classArrayList.add(baseCategoryClass);
                                    reqAddCategory.setCategories(classArrayList);
                                    reqAddCategory.setUploadedBy("ADMIN");
                                    restUtilities.addCategory(getContext(), reqAddCategory, new RestUtilities.OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object onSuccessResponse) {
                                            progressDialog.cancel();
                                            BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                                            if (baseResponse.isApiSuccess()) {
                                                MessageUtils.showShortToast(getActivity(), "Category Successfully Added");
                                                selectedImagesArrayList.clear();
                                                uploadedImagesUrl.clear();
                                                nameInputLayout.setText("");
                                                image1.setImageBitmap(null);
                                                image2.setImageBitmap(null);
                                                image3.setImageBitmap(null);
                                            } else {
                                                MessageUtils.showShortToast(getActivity(), baseResponse.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {
                                            progressDialog.cancel();
                                            MessageUtils.showShortToast(getActivity(), error);

                                        }
                                    });


                                }

                                @Override
                                public void onError(String error) {
                                    progressDialog.cancel();

                                }
                            });
                } else {
                    uploadedImagesUrl = new ArrayList<>();
                    AppExecutor.getInstance().getNetworkExecutor().execute(() -> new TaskCollector()
                            .putTaskCount(isArtistCheckBox.isChecked() ? 4 : 3).
                                    callBack(() -> {
                                        uploadCollection(restUtilities);
                                        AppExecutor.getInstance().getMainThreadExecutor().execute(() -> {
                                            progressDialog.cancel();
                                            MessageUtils.showShortToast(getActivity(), "Number of Images are successfully  uploaded to CDN are " + uploadedImagesUrl.size());

                                        });

                                    }).
                                    sendToTaskManager().execute());
                }
                break;
            case R.id.imagesLayout:
                launchGallery();
                break;

            case R.id.cat_btn_rd:
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);

                isArtistCheckBox.setChecked(false);
                isArtistCheckBox.setVisibility(View.GONE);

                isCategory = true;

                break;
            case R.id.col_btn_rd:
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                isArtistCheckBox.setVisibility(View.VISIBLE);
                isCategory = false;
                break;
        }
    }

    private void uploadCollection(RestUtilities restUtilities) {
        final ProgressDialog[] progressDialog = new ProgressDialog[1];

        AppExecutor.getInstance().getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                progressDialog[0] = new ProgressDialog(getActivity());
                progressDialog[0].setTitle("Step 2 : Uploading to server");
                progressDialog[0].setMessage("Uploading to image to cdn");
                progressDialog[0].setCancelable(false);
                progressDialog[0].setCanceledOnTouchOutside(false);
                progressDialog[0].setIcon(getResources().getDrawable(R.drawable.ic_today_images_24dp));
                progressDialog[0].show();
            }
        });


        ReqAddCollection reqAddCollection = new ReqAddCollection();
        reqAddCollection.setUploadedBy("ADMIN");
        ResCollectionClass resCollectionClass = new ResCollectionClass();
        resCollectionClass.setName(nameInputLayout.getText().toString().trim().toLowerCase());
        ArrayList<ResCollectionClass> resCollectionClassArrayList = new ArrayList<>();
        HashMap<Integer, String> coverMap = new HashMap<>();
        for (int i = 0; i < uploadedImagesUrl.size(); i++) {
            String url = uploadedImagesUrl.get(i);
            coverMap.put(i, url);
        }
        resCollectionClass.setCovers(coverMap);
        if (isArtistCheckBox.getVisibility() == View.VISIBLE && isArtistCheckBox.isChecked()) {
            resCollectionClass.setArtistImage(uploadedImagesUrl.get(uploadedImagesUrl.size()-1));
            resCollectionClass.setArtistLink(linkInputLayout.getText().toString());
            resCollectionClass.setCurated(true);
            resCollectionClass.setArtistName(nameInputLayout.getText().toString());
        }
        resCollectionClassArrayList.add(resCollectionClass);
        reqAddCollection.setCollections(resCollectionClassArrayList);

        reqAddCollection.setOrigin("ANDROID_MOBILE_APP");
        reqAddCollection.setVersion("1.0");
        reqAddCollection.setCountry("IN");

        restUtilities.addCollection(Objects.requireNonNull(getActivity()).getApplicationContext(), reqAddCollection, new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                BaseResponse baseResponse = new Gson().fromJson(onSuccessResponse.toString(), BaseResponse.class);
                AppExecutor.getInstance().getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog[0].cancel();

                        if (baseResponse.isApiSuccess()) {
                            MessageUtils.showShortToast(getActivity(), "Collection Successfully Added");
                            selectedImagesArrayList.clear();
                            uploadedImagesUrl.clear();
                            nameInputLayout.setText("");
                            image1.setImageBitmap(null);
                            image2.setImageBitmap(null);
                            image3.setImageBitmap(null);
                        } else {
                            MessageUtils.showShortToast(getActivity(), baseResponse.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {
                AppExecutor.getInstance().getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog[0].cancel();
                        MessageUtils.showShortToast(getActivity(), error);
                    }
                });


            }
        });
    }


    private void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(intent, PICK_IMAGE_COLLECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {

            if (requestCode == PICK_IMAGE_COLLECTION && resultCode == RESULT_OK && null != data) {
                selectedImagesArrayList = new ArrayList<>();
                if (data.getData() != null) {
                    /*USER SELECTS SINGLE IMAGE*/
                    selectedImagesArrayList.add(getSingleImageLocatioFromGalley(data.getData()));
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(0)).centerCrop().into(image1);
                } else if (null != data.getClipData()) {
                    /*USER SELECTS MULTIPLE IMAGES*/
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        selectedImagesArrayList.add(getSingleImageLocatioFromGalley(item.getUri()));
                    }
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(0)).centerCrop().into(image1);
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(1)).centerCrop().into(image2);
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(2)).centerCrop().into(image3);
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(3)).centerCrop().into(artistImage);
                    MessageUtils.showShortToast(getActivity(), "Selected images are" + clipData.getItemCount());
                }

            } else {
                MessageUtils.showShortToast(getActivity(), "You haven't picked Image");

            }
        } catch (Exception e) {
            MessageUtils.showShortToast(getActivity(), "Something went wrong");
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getSingleImageLocatioFromGalley(Uri data) {
        String[] filepathColumn = {MediaStore.Images.Media.DATA};
        assert data != null;
        Cursor cursor = Objects.requireNonNull(getActivity())
                .getContentResolver()
                .query(data, filepathColumn,
                        null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
        String imagelocation = cursor.getString(columnIndex);
        cursor.close();
        return imagelocation;
    }


    private interface Callback {
        public void onComplete();
    }

    public class TaskCollector {
        private List<Runnable> tasks = new ArrayList<>();
        private Callback callback;
        private int taskCount;

        public TaskCollector collect(Runnable task) {
            tasks.add(task);
            return this;
        }

        TaskCollector putTaskCount(int taskCount) {
            this.taskCount = taskCount;
            return this;
        }

        TaskCollector callBack(Callback callback) {
            this.callback = callback;
            return this;
        }

        public TaskManager executeWithTasks() {
            return new TaskManager(tasks, callback);
        }

        TaskManager sendToTaskManager() {
            return new TaskManager(taskCount, callback);
        }

    }

    public class TaskManager extends Thread {
        private Callback callback;
        private CountDownLatch countDownLatch;
        private ConcurrentLinkedQueue<TaskWorker> taskWorkers;

        private TaskManager(List<Runnable> tasks, Callback callback) {
            this.callback = callback;
            taskWorkers = new ConcurrentLinkedQueue<>();
            countDownLatch = new CountDownLatch(tasks.size());
            for (int i = 0; i < tasks.size(); i++) {
                Runnable runnable = tasks.get(i);
                taskWorkers.add(new TaskWorker(countDownLatch, runnable, i));
            }
        }

        private TaskManager(int tasksCount, Callback callback) {
            this.callback = callback;
            taskWorkers = new ConcurrentLinkedQueue<>();
            countDownLatch = new CountDownLatch(tasksCount);
            for (int i = 0; i < tasksCount; i++) {
                taskWorkers.add(new TaskWorker(countDownLatch, i));
            }
        }

        void execute() {
            start();
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                TaskWorker taskWorker = taskWorkers.poll();
                if (taskWorker == null) {
                    break;
                }
                taskWorker.start();
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (callback != null) {
                callback.onComplete();
            }

        }


    }

    public class TaskWorker implements Runnable {
        CountDownLatch countDownLatch;
        Runnable task;
        Thread thread;
        AtomicBoolean started;
        int index;


        TaskWorker(CountDownLatch countDownLatch, Runnable task, int index) {
            this.countDownLatch = countDownLatch;
            this.task = task;
            this.index = index;
            this.thread = new Thread(this);
            this.started = new AtomicBoolean(false);

        }

        TaskWorker(CountDownLatch countDownLatch, int index) {
            this.countDownLatch = countDownLatch;
            this.index = index;
            this.thread = new Thread(this);
            this.started = new AtomicBoolean(false);

        }

        void start() {
            if (!started.getAndSet(true)) {
                thread.start();
            }
        }

        @Override
        public void run() {

            String fileName = null;
            String fileLocation = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fileLocation = selectedImagesArrayList.get(index);
                fileName = Paths.get(fileLocation).getFileName().toString();

            }

            restUtilities.uploadImageToIK(fileLocation, fileName, Objects.requireNonNull(getActivity()).getApplicationContext(), new RestUtilities.OnSuccessListener() {
                @Override
                public void onSuccess(Object onSuccessResponse) {
                    ResUploadImageToCdn baseResponse = new Gson().fromJson(onSuccessResponse.toString(), ResUploadImageToCdn.class);
                    uploadedImagesUrl.add(baseResponse.getUrl());
                    countDownLatch.countDown();
                    Log.i("THREAD", "REDUCED");

                }

                @Override
                public void onError(String error) {
                    AppExecutor.getInstance().getMainThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            MessageUtils.showShortToast(getActivity(), error);
                            countDownLatch.countDown();
                            Log.i("THREAD", "REDUCED");
                        }
                    });


                }
            });

        }
    }

}
