package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.base.BaseRequest;

import java.util.ArrayList;

public class ReqAaddTag extends BaseRequest {
    private long imageId;
    private ArrayList<String> tags;

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
