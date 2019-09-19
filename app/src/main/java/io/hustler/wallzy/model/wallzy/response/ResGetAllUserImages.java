package io.hustler.wallzy.model.wallzy.response;


import io.hustler.wallzy.model.base.BaseResponse;

import java.util.ArrayList;

public class ResGetAllUserImages extends BaseResponse {
    private ArrayList<ResponseUserImageClass> images;
    private int totalCount;
    private int pagenumber;

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
