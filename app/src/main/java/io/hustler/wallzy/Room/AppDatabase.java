package io.hustler.wallzy.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

    public abstract class DaoUser {
    }

    ;

    public abstract class DaoCategory {
    }

    ;

    public abstract class DaoCollections {
    }

    ;

    public abstract class DaoFavourites {
    }

    ;

    public abstract class DaoAssncatColl {
    }

    ;

    public abstract class DaoAllImages {
    }

    ;

    public static AppDatabase getmAppDatabaseinstance(Context context) {
        if (null != mAppDatabaseinstance) {
            synchronized (LOCK) {
                mAppDatabaseinstance = Room.databaseBuilder(context, AppDatabase.class, WALLZY_DATABASE).build();
            }
        }
        return mAppDatabaseinstance;
    }


}
