package io.hustler.wallzy.model.wallzy.response;

import java.util.ArrayList;

import io.hustler.wallzy.model.base.BaseResponse;

public class ResImageSearch extends BaseResponse {
    private ArrayList<TagImage> tagImages = new ArrayList<>();
    private long imageCount = 0;
    private long pageNumber = 0;
    private long totalPages = 0;

    public static class TagImage {
        private String rawUrl, thumbUrl;
        private long id;

        public String getRawUrl() {
            return rawUrl;
        }

        public void setRawUrl(String rawUrl) {
            this.rawUrl = rawUrl;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    public ArrayList<TagImage> getTagImages() {
        return tagImages;
    }

    public void setTagImages(ArrayList<TagImage> tagImages) {
        this.tagImages = tagImages;
    }

    public long getImageCount() {
        return imageCount;
    }

    public void setImageCount(long imageCount) {
        this.imageCount = imageCount;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
