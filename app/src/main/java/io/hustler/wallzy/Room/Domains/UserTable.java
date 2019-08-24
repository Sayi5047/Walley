package io.hustler.wallzy.Room.Domains;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Mstr_User")
public class UserTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "userName")
    private String username;

    @ColumnInfo(name = "createdDate")
    private long cretedDate;

    public UserTable(int id, String username, Long cretedDate) {
        this.id = id;
        this.username = username;
        this.cretedDate = cretedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCretedDate() {
        return cretedDate;
    }

    public void setCretedDate(long cretedDate) {
        this.cretedDate = cretedDate;
    }
}
