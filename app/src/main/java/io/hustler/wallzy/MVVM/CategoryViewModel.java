package io.hustler.wallzy.MVVM;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.Room.AppDatabase;
import io.hustler.wallzy.Room.Domains.CategoryTable;
import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;
import io.hustler.wallzy.model.wallzy.response.ResGetAllCategories;
import io.hustler.wallzy.model.wallzy.response.ResGetCatCollCount;
import io.hustler.wallzy.networkhandller.RestUtilities;

public class CategoryViewModel extends AndroidViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase mAppDatabase;
    private LiveData<List<CategoryTable>> liveCategoryData;
    private List<String> testString;
    private AppExecutor mAppExecutor;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mAppExecutor = AppExecutor.getInstance();
        mAppDatabase = AppDatabase.getmAppDatabaseinstance(application);
        getCategoryImageSize();
    }

    /**
     * check the app db cat size
     * if >0 then get image size from api
     * if api result count is greater than our db val then fill db with latest
     * else
     * this is new db then add all available cats
     **/
    private void getCategoryImageSize() {
        liveCategoryData = mAppDatabase.categoryDao().getAllLiveCategories(true);
        new RestUtilities().getCategoryCount(getApplication().getApplicationContext(), new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                ResGetCatCollCount resGetCatCollCount = new Gson().fromJson(onSuccessResponse.toString(), ResGetCatCollCount.class);
                if (resGetCatCollCount.isApiSuccess()) {
                    int latestCount = resGetCatCollCount.getCount();
                    mAppExecutor.getDiskExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            int count;
                            count = mAppDatabase.categoryDao().getAllCategoriesCount(true);
                            if (count <= 0) {
                                new RestUtilities().getCategory(getApplication().getApplicationContext(), new RestUtilities.OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object onSuccessResponse) {
                                        processCatsAndStoreinDb(onSuccessResponse);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.e(TAG, "onError: " + error);


                                    }
                                });
                            } else if (count < latestCount) {
                                new RestUtilities().getLatestCats(getApplication().getApplicationContext(), new RestUtilities.OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object onSuccessResponse) {
                                        processCatsAndStoreinDb(onSuccessResponse);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.e(TAG, "onError: " + error);

                                    }
                                }, count);
                            }

                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);

            }
        });
    }

    private void processCatsAndStoreinDb(Object onSuccessResponse) {
        ResGetAllCategories resGetAllCollections = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllCategories.class);
        if (resGetAllCollections.isApiSuccess() && resGetAllCollections.getCollections().size() > 0) {
            ArrayList<CategoryTable> categoryTables = new ArrayList<>();
            for (BaseCategoryClass collectionClass : resGetAllCollections.getCollections()) {
                CategoryTable categoryTable = new CategoryTable();
                categoryTable.setCollectionname(collectionClass.getName());
                categoryTable.setCreatedDate(System.currentTimeMillis());
                categoryTable.setActive(true);
                categoryTable.setFirebaseId((int) collectionClass.getId());
                categoryTable.setCoverImage(collectionClass.getCover());
                categoryTables.add(categoryTable);
            }
            mAppExecutor.getDiskExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    mAppDatabase.categoryDao().insertAllCategories(categoryTables.toArray(new CategoryTable[categoryTables.size()]));

                }
            });
        }
    }

    public LiveData<List<CategoryTable>> getLiveCategoryData() {
        return liveCategoryData;
    }
}
