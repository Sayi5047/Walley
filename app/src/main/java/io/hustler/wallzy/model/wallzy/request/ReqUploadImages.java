package io.hustler.wallzy.model.wallzy.request;

import io.hustler.wallzy.model.base.BaseRequest;

import java.util.ArrayList;

/*{
    "filePath": "/TestImage_QN_4dSlBZ",
    "size": 2991,
    "fileId": "5d65323eacdf3e33426aa968",
    "url": "https://ik.imagekit.io/le2nup6mnq/TestImage_QN_4dSlBZ",
    "name": "TestImage_QN_4dSlBZ",
    "fileType": "image",
    "thumbnailUrl": "https://ik.imagekit.io/le2nup6mnq/tr:n-media_library_thumbnail/TestImage_QN_4dSlBZ",
    "width": 900,
    "height": 600
}*/
public class ReqUploadImages extends BaseRequest {
    private ArrayList<Image> images = new ArrayList<>();
    private String uploadedBy;

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public static class Image {
        private String filePath;
        private String fileId;
        private String url;
        private String name;
        private String fileType;
        private String thumbUrl;
        private long size;
        private long width;
        private long height;
        private boolean isCollection;
        private long id;
        private ArrayList<String> hashTags;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getWidth() {
            return width;
        }

        public void setWidth(long width) {
            this.width = width;
        }

        public long getHeight() {
            return height;
        }

        public void setHeight(long height) {
            this.height = height;
        }

        public boolean isCollection() {
            return isCollection;
        }

        public void setCollection(boolean collection) {
            isCollection = collection;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public ArrayList<String> getHashTags() {
            return hashTags;
        }

        public void setHashTags(ArrayList<String> hashTags) {
            this.hashTags = hashTags;
        }
    }
}
