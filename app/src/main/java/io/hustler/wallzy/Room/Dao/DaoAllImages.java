package io.hustler.wallzy.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.hustler.wallzy.Room.Domains.AssnCategoryCollectionImagesTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoAllImages {

    @Query(value = "SELECT * FROM ASSN_IMAGE_COLLCAT")
    LiveData<List<AssnCategoryCollectionImagesTable>> getAllImages();

    @Insert(onConflict = REPLACE)
    @Transaction
    void insertimage(AssnCategoryCollectionImagesTable collectionsTable);

    @Insert
    @Transaction
    void insertAllimage(AssnCategoryCollectionImagesTable... collectionsTables);

    @Delete
    void delete(AssnCategoryCollectionImagesTable collectionsTable);

    @Update
    void updateImage(AssnCategoryCollectionImagesTable collectionsTable);

    @Query(value = "SELECT * FROM Assn_Image_collcat WHERE id = :id")
    @Transaction
    AssnCategoryCollectionImagesTable getCollectionById(int id);

    @Query(value = "SELECT * FROM Assn_Image_collcat WHERE isLiked = :val")
    @Transaction
    LiveData<List<AssnCategoryCollectionImagesTable>> getFavImagesFromCollections(boolean val);

}
