package io.hustler.wallzy.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.hustler.wallzy.Room.Domains.CollectionsTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoCollections {
    @Query(value = "SELECT * FROM MSTR_COLLECTIONS")
    LiveData<List<CollectionsTable>> getAllCollections();

    @Insert(onConflict = REPLACE)
    @Transaction
    void insertCollection(CollectionsTable collectionsTable);

    @Insert
    void insertAllColections(CollectionsTable... collectionsTables);

    @Delete
    void delete(CollectionsTable collectionsTable);

    @Update
    @Transaction
    void updateCollection(CollectionsTable collectionsTable);

    @Query(value = "SELECT * FROM MSTR_COLLECTIONS WHERE id = :id")
    @Transaction
    CollectionsTable getCollectionById(int id);

    @Query(value = "SELECT * FROM MSTR_COLLECTIONS WHERE isActive = :val")
    @Transaction
    LiveData<List<CollectionsTable>> getFavImagesFromCollections(boolean val);

}
