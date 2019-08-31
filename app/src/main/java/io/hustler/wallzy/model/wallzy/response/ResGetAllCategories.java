package io.hustler.wallzy.model.wallzy.response;


import java.util.ArrayList;

import io.hustler.wallzy.model.base.BaseResponse;

public class ResGetAllCategories extends BaseResponse {

    private ArrayList<ResCategoryClass> collections = new ArrayList<>();

    public ArrayList<ResCategoryClass> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<ResCategoryClass> collections) {
        this.collections = collections;
    }
}
