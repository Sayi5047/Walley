package io.hustler.wallzy.model.wallzy.request;


import io.hustler.wallzy.model.base.BaseRequest;

public class ReqGetCollectionOrCategoryImages extends BaseRequest {
    private long id;
    private int pageNumber;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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
