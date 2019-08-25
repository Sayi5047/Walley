package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "Mstr_Category", indices = {@Index(value = {"categoryName", "coverImage", "fb_id"}, unique = true)})
public class CategoryTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "fb_id")
    public int firebaseId;

    @ColumnInfo(name = "categoryName")

    private String collectionname;

    @ColumnInfo(name = "coverImage")
    private String coverImage;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    @ColumnInfo(name = "isActive")
    private boolean isActive;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectionname() {
        return collectionname;
    }

    public void setCollectionname(String collectionname) {
        this.collectionname = collectionname;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(int firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }


    public class Assn_Category_Image {
        @Embedded
        CategoryTable categoryTable;
        @Relation(parentColumn = "id", entityColumn = "categoryId", entity = AssnCategoryCollectionImagesTable.class)
        private List<AssnCategoryCollectionImagesTable> categoryImagesList;

    }

    @Dao
    public interface UserPetDao {
        @Query("SELECT * from mstr_category")
        public List<Assn_Category_Image> loadCategroyImages();
    }
}
