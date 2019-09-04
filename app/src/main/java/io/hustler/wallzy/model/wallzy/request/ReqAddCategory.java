package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;

import java.util.ArrayList;

public class ReqAddCategory {

    ArrayList<BaseCategoryClass> categories = new ArrayList<>();
    private String uploadedBy;

    public ArrayList<BaseCategoryClass> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<BaseCategoryClass> categories) {
        this.categories = categories;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
