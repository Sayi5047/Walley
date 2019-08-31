package io.hustler.wallzy.model.base;

import java.util.HashMap;

public class ResCollectionClass {
    private long id;
    private String name;
    private HashMap<Integer,String> covers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, String> getCovers() {
        return covers;
    }

    public void setCovers(HashMap<Integer, String> covers) {
        this.covers = covers;
    }
}
