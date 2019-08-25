package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "Mstr_Collections")
public class CollectionsTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "collectionName")
    private String collectionname;

    @ColumnInfo(name = "coverImage")
    private String coverImage;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public class Assn_Collection_Image {

        @Embedded
        CollectionsTable collectionsTable;
        @Relation(parentColumn = "id", entityColumn = "collectionId", entity = AssnCategoryCollectionImagesTable.class)
        private List<AssnCategoryCollectionImagesTable> imagesList;

    }

    @Dao
    public interface DaoCollectionImages {
        @Query("SELECT * FROM Mstr_Collections")
        public List<Assn_Collection_Image> loadCategroyImages();
    }

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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
