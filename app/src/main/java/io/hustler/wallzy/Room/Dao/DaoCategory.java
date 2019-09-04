package io.hustler.wallzy.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.hustler.wallzy.Room.Domains.CategoryTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoCategory {

    @Query(value = "SELECT * FROM Mstr_Category where isActive= :val")
    LiveData<List<CategoryTable>> getAllLiveCategories(boolean val);

    @Query(value = "SELECT * FROM Mstr_Category")
    List<CategoryTable> getAllCategories();

    @Query(value = "SELECT COUNT(*) FROM Mstr_Category where isActive= :val")
    Integer getAllCategoriesCount(boolean val);

    @Insert(onConflict = REPLACE)
    void insertCategroy(CategoryTable categoryTable);

    @Insert
    void insertAllCategories(CategoryTable... categoryTables);

    @Delete
    void delete(CategoryTable categoryTable);

    @Update
    void updateCategory(CategoryTable categoryTable);

    @Query(value = "SELECT * FROM Mstr_Category WHERE id = :id")
    CategoryTable getCategoryById(int id);

    @Query(value = "SELECT * FROM Mstr_Category WHERE isActive = :val")
    LiveData<List<CategoryTable>> getFavImagesFromCategory(boolean val);

    @Query(value = "SELECT id FROM Mstr_Category ORDER BY id DESC LIMIT 1 ")
    int getLastid();
}
