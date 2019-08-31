package io.hustler.wallzy.model.wallzy.response;

import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResponseImageClass;


import java.util.ArrayList;

public class ResGetCollectionIMages extends BaseResponse {
    private int totalNumber = -1;
    private int totalPages = -1;
    private ArrayList<ResponseImageClass> images = new ArrayList<>();

    public ArrayList<ResponseImageClass> getImages() {
        return images;
    }

    public void setImages(ArrayList<ResponseImageClass> images) {
        this.images = images;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
