package io.hustler.wallzy.model.wallzy.response;


import java.util.ArrayList;

import io.hustler.wallzy.model.base.BaseResponse;

public class ResGetAllCategories extends BaseResponse {

    private ArrayList<BaseCategoryClass> collections = new ArrayList<>();

    public ArrayList<BaseCategoryClass> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<BaseCategoryClass> collections) {
        this.collections = collections;
    }
}
