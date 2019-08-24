package io.hustler.wallzy.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.hustler.wallzy.Room.Domains.AssnCategoryCollectionImagesTable;
import io.hustler.wallzy.Room.Domains.CollectionsTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoAllImages {

    @Query(value = "SELECT * FROM ASSN_IMAGE_COLLCAT")
    LiveData<List<AssnCategoryCollectionImagesTable>> getAllImages();

    @Insert(onConflict = REPLACE)
    void insertimage(AssnCategoryCollectionImagesTable collectionsTable);

    @Insert
    void insertAllimage(AssnCategoryCollectionImagesTable... collectionsTables);

    @Delete
    void delete(AssnCategoryCollectionImagesTable collectionsTable);

    @Update
    void updateImage(AssnCategoryCollectionImagesTable collectionsTable);

    @Query(value = "SELECT * FROM Assn_Image_collcat WHERE id = :id")
    CollectionsTable getCollectionById(int id);

    @Query(value = "SELECT * FROM Assn_Image_collcat WHERE isLiked = :val")
    LiveData<List<CollectionsTable>> getFavImagesFromCollections(boolean val);

}
