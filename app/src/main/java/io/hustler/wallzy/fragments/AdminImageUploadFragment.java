package io.hustler.wallzy.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.nio.file.Paths;
import java.util.ArrayList;
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
import io.hustler.wallzy.Room.Domains.CategoryTable;
import io.hustler.wallzy.adapters.VerticalImagesAdapter;
import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResCollectionClass;
import io.hustler.wallzy.model.imagekit.ResUploadImageToCdn;
import io.hustler.wallzy.model.wallzy.request.ReqUploadImages;
import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;
import io.hustler.wallzy.model.wallzy.response.ResGetAllCategories;
import io.hustler.wallzy.model.wallzy.response.ResGetAllCollections;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;

import static android.app.Activity.RESULT_OK;


public class AdminImageUploadFragment extends Fragment {

    @BindView(R.id.upload_imagescatcollbtn)
    Button uploadImagescatcollbtn;
    @BindView(R.id.upload_images_rv)
    RecyclerView uploadImagesRv;

    @BindView(R.id.upload_cat_btn_rd)
    RadioButton uploadCatBtnRd;
    @BindView(R.id.upload_col_btn_rd)
    RadioButton uploadColBtnRd;
    @BindView(R.id.rd_upload_Images_group)
    RadioGroup rdUploadImagesGroup;
    @BindView(R.id.cat_coll_items_rv)
    RecyclerView catCollItemsRv;
    @BindView(R.id.upload_cat_coll_images_layout)
    RelativeLayout uploadCatCollImagesLayout;
    @BindView(R.id.gallery_btn)
    Button galleryBtn;
    @BindView(R.id.header)
    TextView header;
    private int PICK_IMAGE_COLLECTION = 1;

    AppExecutor appExecutor;
    private ArrayList<String> selectedImagesArrayList;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    private VerticalImagesAdapter catcollAdapter, imagesAdapter;
    private int id = 0;
    private boolean isCat = true;
    private RestUtilities restUtilities;
    private ArrayList<ReqUploadImages.Image> uploadedImagesUrl;

    public AdminImageUploadFragment() {
        // Required empty public constructor
    }


    public static AdminImageUploadFragment newInstance() {

        return new AdminImageUploadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_image_upload, container, false);
        ButterKnife.bind(this, view);
        restUtilities = new RestUtilities();

        uploadCatBtnRd.setChecked(true);
        isCat = true;
        appExecutor = AppExecutor.getInstance();

        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        catCollItemsRv.setLayoutManager(linearLayoutManager);
        uploadImagesRv.setLayoutManager(linearLayoutManager2);
        uploadCatBtnRd.setChecked(true);
        uploadCatBtnRd.performClick();


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


    @OnClick({R.id.upload_imagescatcollbtn, R.id.upload_cat_btn_rd, R.id.upload_col_btn_rd, R.id.rd_upload_Images_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_imagescatcollbtn:
                if (id == 0) {
                    MessageUtils.showShortToast(getActivity(), "Please select a category or collection");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Attention");
                    String val = isCat ? "Category" : "Collection";
                    builder.setMessage("Upload " + selectedImagesArrayList.size() + " images to" + header.getText().toString() + " " + val);
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startWorkerThreads();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                    builder.setCancelable(false);
                    startWorkerThreads();


                }
                break;
            case R.id.upload_cat_btn_rd:
                isCat = true;
                getCategoriesData();
                break;
            case R.id.upload_col_btn_rd:
                isCat = false;
                getCollectionsData();
                break;
            case R.id.rd_upload_Images_group:
                break;
        }
    }

    private void startWorkerThreads() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Step 1 : Uploading");
        progressDialog.setMessage("Uploading  images  to cdn");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_images_24dp));
        progressDialog.show();

        uploadedImagesUrl = new ArrayList<ReqUploadImages.Image>();
        appExecutor.getNetworkExecutor().execute(new Runnable() {
            @Override
            public void run() {
                new TaskCollector()
                        .putTaskCount(selectedImagesArrayList.size())
                        .callBack(new Callback() {
                            @Override
                            public void onComplete() {
                                uploadImagestoDatabase(restUtilities);
                                appExecutor.getMainThreadExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.cancel();
                                        MessageUtils.showShortToast(getActivity(), "Number of Images are successfully  uploaded to CDN are " + uploadedImagesUrl.size());

                                    }
                                });
                            }
                        }).sendToTaskManager().execute();
            }
        });
    }

    private void getCategoriesData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Categories");
        progressDialog.setMessage("Retrieving Categories from db");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new RestUtilities()
                .getCategory(Objects.requireNonNull(getActivity()).getApplicationContext(), new RestUtilities.OnSuccessListener() {
                    @Override
                    public void onSuccess(Object onSuccessResponse) {
                        progressDialog.cancel();
                        ResGetAllCategories resGetAllCategories
                                = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllCategories.class);
                        if (resGetAllCategories.isApiSuccess()) {
                            ArrayList<CategoryTable> data = new ArrayList<>();
                            for (BaseCategoryClass baseCategoryClass : resGetAllCategories.getCollections()) {
                                CategoryTable table = new CategoryTable();
                                table.setCollectionname(baseCategoryClass.getName());
                                table.setId((int) baseCategoryClass.getId());
                                table.setCoverImage(baseCategoryClass.getCover());
                                data.add(table);
                            }
                            id = 0;
                            catcollAdapter = new VerticalImagesAdapter(data, getActivity(), (category, imageView) -> {
                                id = category.getId();
                                header.setText(category.getCollectionname());
                                MessageUtils.showShortToast(getActivity(), category.getCollectionname() + " " + id);

                            });
                            catCollItemsRv.setAdapter(catcollAdapter);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.cancel();
                        MessageUtils.showShortToast(getActivity(), error);
                    }
                });
    }

    private void getCollectionsData() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting Collections");
        progressDialog.setMessage("Retrieving collections from db");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new RestUtilities().getCollection(Objects.requireNonNull(getActivity()).getApplicationContext(),
                new RestUtilities.OnSuccessListener() {
                    @Override
                    public void onSuccess(Object onSuccessResponse) {
                        progressDialog.cancel();
                        ResGetAllCollections resGetAllCategories
                                = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllCollections.class);
                        if (resGetAllCategories.isApiSuccess()) {
                            ArrayList<CategoryTable> data = new ArrayList<>();
                            for (ResCollectionClass baseCategoryClass : resGetAllCategories.getCollections()) {
                                CategoryTable table = new CategoryTable();
                                table.setCollectionname(baseCategoryClass.getName());
                                table.setId((int) baseCategoryClass.getId());
                                table.setCoverImage(baseCategoryClass.getCovers().get(1));
                                data.add(table);
                            }
                            id = 0;
                            catcollAdapter = new VerticalImagesAdapter(data, getActivity(), (category, imageView) -> {
                                id = category.getId();
                                header.setText(category.getCollectionname());
                                MessageUtils.showShortToast(getActivity(), category.getCollectionname() + " " + id);

                            });
                            catCollItemsRv.setAdapter(catcollAdapter);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        progressDialog.cancel();
                        MessageUtils.showShortToast(getActivity(), error);
                    }
                });
    }


    private void uploadImagestoDatabase(RestUtilities restUtilities) {
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


        ReqUploadImages reqUploadImages = new ReqUploadImages();
        reqUploadImages.setUploadedBy("ADMIN");
        reqUploadImages.setImages(uploadedImagesUrl);
        reqUploadImages.setOrigin("ANDROID_MOBILE");
        reqUploadImages.setVersion("1.0");
        reqUploadImages.setCountry("INDIA");


        restUtilities.uploadImages(Objects.requireNonNull(getActivity()).getApplicationContext(), reqUploadImages,
                new RestUtilities.OnSuccessListener() {
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
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == PICK_IMAGE_COLLECTION && resultCode == RESULT_OK && null != data) {
                selectedImagesArrayList = new ArrayList<>();
                if (data.getData() != null) {
                    /*USER SELECTS SINGLE IMAGE*/
                    selectedImagesArrayList.add(getSingleImageLocatioFromGalley(data.getData()));
                } else if (null != data.getClipData()) {
                    /*USER SELECTS MULTIPLE IMAGES*/
                    ClipData clipData = data.getClipData();
                    ArrayList<CategoryTable> categoryTables = new ArrayList<>();

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        String url = getSingleImageLocatioFromGalley(item.getUri());
                        selectedImagesArrayList.add(url);
                        CategoryTable categoryTable = new CategoryTable();
                        categoryTable.setCollectionname("");
                        categoryTable.setCoverImage(url);
                        categoryTables.add(categoryTable);

                    }
                    imagesAdapter = new VerticalImagesAdapter(categoryTables, getActivity(), new VerticalImagesAdapter.OnChildClickListener() {
                        @Override
                        public void onCLick(CategoryTable category, ImageView imageView) {
                            MessageUtils.showShortToast(getActivity(), category.getCollectionname());

                        }
                    });
                    uploadImagesRv.setAdapter(imagesAdapter);


                    MessageUtils.showShortToast(getActivity(), "Selected images are " + clipData.getItemCount());
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

    @OnClick(R.id.gallery_btn)
    public void onViewClicked() {
        launchGallery();
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
                    ReqUploadImages.Image image = new ReqUploadImages.Image();
                    image.setFilePath(baseResponse.getFilePath());
                    image.setFileId(baseResponse.getFileId());
                    image.setUrl(baseResponse.getUrl());
                    image.setName(baseResponse.getName());
                    image.setFileType(baseResponse.getFileType());
                    image.setThumbUrl(baseResponse.getThumbnailUrl());
                    image.setSize(baseResponse.getSize());
                    image.setWidth(baseResponse.getWidth());
                    image.setHeight(baseResponse.getHeight());
                    image.setCollection(isCat);
                    image.setHashTags(new ArrayList<String>());
                    image.setId(id);


                    uploadedImagesUrl.add(image);


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
