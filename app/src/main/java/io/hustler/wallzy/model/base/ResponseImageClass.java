package io.hustler.wallzy.model.base;

import java.io.Serializable;

public class ResponseImageClass extends BaseResponse implements Serializable {
    private String url;
    private String name;
    private String fileType;
    private String thumbUrl;
    private long size;
    private long width;
    private long height;
    private long id;
    private long likes;
    private long dislikes;
    private long downloads;
    private String artistName, artsistImage, artistBackLink;


    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtsistImage() {
        return artsistImage;
    }

    public void setArtsistImage(String artsistImage) {
        this.artsistImage = artsistImage;
    }

    public String getArtistBackLink() {
        return artistBackLink;
    }

    public void setArtistBackLink(String artistBackLink) {
        this.artistBackLink = artistBackLink;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
