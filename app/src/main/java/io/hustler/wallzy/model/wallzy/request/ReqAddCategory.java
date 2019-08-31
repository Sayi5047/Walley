package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.wallzy.response.ResCategoryClass;

import java.util.ArrayList;

public class ReqAddCategory {

    ArrayList<ResCategoryClass> categories = new ArrayList<>();
    private String uploadedBy;

    public ArrayList<ResCategoryClass> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ResCategoryClass> categories) {
        this.categories = categories;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
