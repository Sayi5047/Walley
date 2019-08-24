package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.sql.Timestamp;
import java.util.List;

@Entity(tableName = "Mstr_Collections")

public class CollectionsTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "collectionName")
    private String collectionname;

    @ColumnInfo(name = "createdDate")
    private long createdDate;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    @Relation(parentColumn = "id",entityColumn = "collectionId",entity = AssnCategoryCollectionImagesTable.class)
    private List<AssnCategoryCollectionImagesTable> imagesList;


}
