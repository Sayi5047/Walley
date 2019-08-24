package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Assn_Image_collcat",
        foreignKeys = {
                @ForeignKey(entity = CollectionsTable.class,
                        parentColumns = "id",
                        childColumns = "collectionId", onDelete = ForeignKey.NO_ACTION),
                @ForeignKey(entity = CategoryTable.class,
                        parentColumns = "id",
                        childColumns = "categoryId", onDelete = ForeignKey.NO_ACTION)})

public class AssnCategoryCollectionImagesTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "isCollection")
    private boolean isCollection;

    @ColumnInfo(name = "imageName")
    private String imageName;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    @ColumnInfo(name = "url")
    private String imageUrl;

    @ColumnInfo(name = "collectionId")
    private long CollectionId;

    @ColumnInfo(name = "categoryId")
    private long categoryId;

    @ColumnInfo(name = "isLiked")
    private boolean liked;


    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(long collectionId) {
        CollectionId = collectionId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
