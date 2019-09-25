package io.hustler.wallzy.model.wallzy.response;


import java.util.ArrayList;

import io.hustler.wallzy.model.base.BaseResponse;

public class ResGetAllUserImages extends BaseResponse {
    private ArrayList<ResponseUserImageClass> images;
    private int totalCount;
    private int pagenumber;
    private int donwloadCount, likeCount, wallCount;

    public int getDonwloadCount() {
        return donwloadCount;
    }

    public void setDonwloadCount(int donwloadCount) {
        this.donwloadCount = donwloadCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getWallCount() {
        return wallCount;
    }

    public void setWallCount(int wallCount) {
        this.wallCount = wallCount;
    }

    public ArrayList<ResponseUserImageClass> getImages() {
        return images;
    }

    public void setImages(ArrayList<ResponseUserImageClass> images) {
        this.images = images;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }
}
