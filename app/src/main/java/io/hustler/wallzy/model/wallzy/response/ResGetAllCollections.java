package io.hustler.wallzy.model.wallzy.response;

import io.hustler.wallzy.model.base.BaseResponse;
import io.hustler.wallzy.model.base.ResCollectionClass;


import java.util.ArrayList;

public class ResGetAllCollections extends BaseResponse {
   private ArrayList<ResCollectionClass> collections;

    public ArrayList<ResCollectionClass> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<ResCollectionClass> collections) {
        this.collections = collections;
    }
}
