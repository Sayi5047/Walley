package io.hustler.wallzy.model.wallzy.request;


import io.hustler.wallzy.model.base.BaseRequest;

public class ReqGetCollectionorCategoryImages extends BaseRequest {
    private long id;
    private int pageNumber;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }


}
