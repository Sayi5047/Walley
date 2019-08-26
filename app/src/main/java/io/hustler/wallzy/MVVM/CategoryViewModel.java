package io.hustler.wallzy.MVVM;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.hustler.wallzy.Executors.AppExecutor;
import io.hustler.wallzy.Room.AppDatabase;
import io.hustler.wallzy.Room.Domains.CategoryTable;
import io.hustler.wallzy.model.firebasepojo.CategoryFBClass;

public class CategoryViewModel extends AndroidViewModel {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase mAppDatabase;
    private LiveData<List<CategoryTable>> liveCategoryData;
    private List<String> testString;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private AppExecutor mAppExecutor;

    /*THIS MODEL CRAETES PROBLWM WHEN NODE IS DELETED FROM THE FIREBASE*/
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mAppExecutor = AppExecutor.getInstance();
        mAppDatabase = AppDatabase.getmAppDatabaseinstance(application);
        getAllLiveCategoryDataFromRepositry();
        mAppExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference("Category");
                mDatabaseReference.limitToLast(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        mAppExecutor.getDiskExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                final int lastIdInLocalDB;
                                lastIdInLocalDB = mAppDatabase.categoryDao().getLastid();
                                int idFromFireBase = (Integer.parseInt(Objects.requireNonNull(dataSnapshot.getKey())));
                                if (0 == lastIdInLocalDB) {
                                    /*FRESH DB*/
                                    getAllDataAndSave();
                                    Log.i(TAG, "SAVING FRESH DATA");
                                } else if (idFromFireBase > (lastIdInLocalDB)) {
                                    /*NEW DATA AVAILABLE*/
                                    getLatestDataAndSave(lastIdInLocalDB, idFromFireBase);
                                    Log.i(TAG, "NEW DATA AVAILABLE");
                                } else {
                                    Log.i(TAG, "DATA IS IN SYNC");
                                }
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        CategoryFBClass categoryFBClass = dataSnapshot.getValue(CategoryFBClass.class);
                        int key = Integer.parseInt(dataSnapshot.getKey());
                        mAppExecutor.getDiskExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                CategoryTable categoryTable = mAppDatabase.categoryDao().getCategoryById(key);
                                if (null != categoryTable) {
                                    categoryTable.setCollectionname(categoryFBClass.getName());
                                    categoryTable.setCoverImage(categoryFBClass.getImage());

                                    mAppDatabase.categoryDao().updateCategory(categoryTable);
                                    getAllLiveCategoryDataFromRepositry();
                                }

                            }
                        });


                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        int key = Integer.parseInt(dataSnapshot.getKey());
                        mAppExecutor.getDiskExecutor().execute(new Runnable() {
                            @Override
                            public void run() {

                                CategoryFBClass categoryFBClass = dataSnapshot.getValue(CategoryFBClass.class);
                                int key = Integer.parseInt(dataSnapshot.getKey());
                                CategoryTable categoryTable = mAppDatabase.categoryDao().getCategoryById(key);
                                if (null != categoryTable) {
                                    categoryTable.setActive(false);
                                    mAppDatabase.categoryDao().updateCategory(categoryTable);
                                    getAllLiveCategoryDataFromRepositry();
                                }
                            }
                        });
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "ERROR WHILE GETTING LATEST ID DUE TO " + databaseError.getMessage());

                    }
                });
            }
        });
    }

    private void getAllLiveCategoryDataFromRepositry() {
        liveCategoryData = mAppDatabase.categoryDao().getAllLiveCategories(true);
        Log.i(TAG, String.valueOf(liveCategoryData));

    }


    private void getAllDataAndSave() {
        /*OUR APP DATA IS FRESH*/
        /*SO LETS GET ALL DATA FROM FIRE_BASE*/
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                insertDataToLocalDB(dataSnapshot, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    private void insertDataToLocalDB(@NonNull DataSnapshot dataSnapshot, boolean isSingle) {

        mAppExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<CategoryTable> categoryTableArrayList = new ArrayList<>();
                if (isSingle) {

                    CategoryTable categoryTableFromDb = mAppDatabase.categoryDao().getCategoryById(Integer.parseInt(dataSnapshot.getKey()));
                    if (null != categoryTableFromDb) {
                        Log.i(TAG, "RECPRD ALREADY EXISTS");
                        return;
                    } else {
                        CategoryTable categoryTable = new CategoryTable();
                        categoryTable.setFirebaseId(Integer.parseInt(Objects.requireNonNull(dataSnapshot.getKey())));
                        CategoryFBClass categoryFBClass = ((CategoryFBClass) dataSnapshot.getValue(CategoryFBClass.class));
                        categoryTable.setCoverImage(categoryFBClass.getImage());
                        categoryTable.setCollectionname(categoryFBClass.getName());
                        categoryTable.setCreatedDate(System.currentTimeMillis());
                        categoryTable.setActive(true);
                        categoryTableArrayList.add(categoryTable);
                        Collections.reverse(categoryTableArrayList);
                        mAppDatabase.categoryDao().insertAllCategories(categoryTableArrayList.toArray(new CategoryTable[categoryTableArrayList.size()]));
                        getAllLiveCategoryDataFromRepositry();
                    }
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CategoryTable categoryTable = new CategoryTable();
                        categoryTable.setFirebaseId(Integer.parseInt(Objects.requireNonNull(snapshot.getKey())));
                        CategoryFBClass categoryFBClass = ((CategoryFBClass) snapshot.getValue(CategoryFBClass.class));
                        categoryTable.setCoverImage(categoryFBClass.getImage());
                        categoryTable.setCollectionname(categoryFBClass.getName());
                        categoryTable.setCreatedDate(System.currentTimeMillis());
                        categoryTable.setActive(true);
                        categoryTableArrayList.add(categoryTable);

                    }
                    Collections.reverse(categoryTableArrayList);
                    mAppDatabase.categoryDao().insertAllCategories(categoryTableArrayList.toArray(new CategoryTable[categoryTableArrayList.size()]));
                    getAllLiveCategoryDataFromRepositry();
                }

            }
        });
    }

    private void getLatestDataAndSave(int lastIdInLocalDB, int idFromFirebase) {
        /*OUR APP DATA IS OLD SO LETS UPDATE*/
        Log.i(TAG, "localIndex " + lastIdInLocalDB + " " + "FB Index " + idFromFirebase);
        for (int i = lastIdInLocalDB; i <= lastIdInLocalDB; i++) {
            mDatabaseReference.child(String.valueOf(i + 1)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    insertDataToLocalDB(dataSnapshot, true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });
        }
        /*GET DATA FROM FIRE_BASE*/
    }

    public LiveData<List<CategoryTable>> getLiveCategoryData() {
        return liveCategoryData;
    }


    private void getLatestIdFromDb(int[] lastIdInLocalDB) {
        mAppExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                lastIdInLocalDB[0] = mAppDatabase.categoryDao().getLastid();
                Log.i(TAG, "LATEST ID FROM DB IS " + lastIdInLocalDB[0]);

            }
        });
    }

}
