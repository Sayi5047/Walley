package io.hustler.wallzy.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.hustler.wallzy.Room.Domains.UserTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoUser {
    @Query("SELECT * FROM Mstr_User")
    LiveData<List<UserTable>> getAllImages();

    @Insert(onConflict = REPLACE)
    void insertuser(UserTable mUser);

    @Insert
    void insertAllImages(UserTable... mUsersList);

    @Delete
    void delete(UserTable mUser);

    @Update
    void updateImage(UserTable mUser);

    @Query("SELECT * FROM Mstr_User WHERE id = :uId")
    UserTable getUserById(int uId);


}
