package io.hustler.wallzy.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.hustler.wallzy.Room.Dao.DaoAllImages;
import io.hustler.wallzy.Room.Dao.DaoCategory;
import io.hustler.wallzy.Room.Dao.DaoCollections;
import io.hustler.wallzy.Room.Dao.DaoUser;
import io.hustler.wallzy.Room.Domains.AssnCategoryCollectionImagesTable;
import io.hustler.wallzy.Room.Domains.CategoryTable;
import io.hustler.wallzy.Room.Domains.CollectionsTable;
import io.hustler.wallzy.Room.Domains.UserTable;

@Database(entities = {UserTable.class,
        CategoryTable.class,
        CollectionsTable.class,
        AssnCategoryCollectionImagesTable.class,
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mAppDatabaseinstance;
    private static final Object LOCK = new Object();
    private static final String WALLZY_DATABASE = "Wallzy_Database";

    public abstract DaoUser userDao();

    public abstract DaoCategory categoryDao();

    public abstract DaoCollections collectionsDao();

    public abstract DaoAllImages allImagesDao();

    public static AppDatabase getmAppDatabaseinstance(Context context) {
        if (null != mAppDatabaseinstance) {
            synchronized (LOCK) {
                mAppDatabaseinstance = Room.databaseBuilder(context, AppDatabase.class, WALLZY_DATABASE).build();
            }
        }
        return mAppDatabaseinstance;
    }


}
