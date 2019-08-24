package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "Mstr_Category")
public class CategoryTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "categoryName")
    private String collectionname;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    @Relation(parentColumn = "id", entityColumn = "categoryId", entity = AssnCategoryCollectionImagesTable.class)
    private List<AssnCategoryCollectionImagesTable> categoryImagesList;


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

    public List<AssnCategoryCollectionImagesTable> getCategoryImagesList() {
        return categoryImagesList;
    }

    public void setCategoryImagesList(List<AssnCategoryCollectionImagesTable> categoryImagesList) {
        this.categoryImagesList = categoryImagesList;
    }
}
