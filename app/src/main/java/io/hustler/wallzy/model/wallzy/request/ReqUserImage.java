package io.hustler.wallzy.model.wallzy.request;


import io.hustler.wallzy.model.base.BaseRequest;

public class ReqUserImage extends BaseRequest {
    private long imageId;
    private long userId;
    private boolean guest;

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }
}
