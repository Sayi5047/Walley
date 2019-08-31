package io.hustler.wallzy.model.wallzy.request;



import java.util.ArrayList;

import io.hustler.wallzy.model.base.BaseRequest;
import io.hustler.wallzy.model.base.ResCollectionClass;

public class ReqAddCollection extends BaseRequest {

    ArrayList<ResCollectionClass> collections = new ArrayList<>();
    private String uploadedBy;

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ArrayList<ResCollectionClass> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<ResCollectionClass> collections) {
        this.collections = collections;
    }
}
