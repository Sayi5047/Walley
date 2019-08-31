package io.hustler.wallzy.model.wallzy.response;

import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResponseImageClass;


import java.util.ArrayList;

public class ResGetAllImages extends BaseResponse {
    private ArrayList<ResponseImageClass> images;
    private int totalCount;
    private int pagenumber;

    public ArrayList<ResponseImageClass> getImages() {
        return images;
    }

    public void setImages(ArrayList<ResponseImageClass> images) {
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
