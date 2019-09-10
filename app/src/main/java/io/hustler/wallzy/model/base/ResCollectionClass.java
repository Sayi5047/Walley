package io.hustler.wallzy.model.base;

import java.util.HashMap;

public class ResCollectionClass {
    private long id;
    private String name;
    private String artistName;
    private String artistImage;
    private String artistLink;
    private boolean curated;
    private int imageCount;

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public String getArtistLink() {
        return artistLink;
    }

    public void setArtistLink(String artistLink) {
        this.artistLink = artistLink;
    }

    public boolean isCurated() {
        return curated;
    }

    public void setCurated(boolean curated) {
        this.curated = curated;
    }

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
