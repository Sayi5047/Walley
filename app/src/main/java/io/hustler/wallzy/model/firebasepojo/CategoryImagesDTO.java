package io.hustler.wallzy.model.firebasepojo;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryImagesDTO implements Serializable {
   public static class Category {
        String name;
        String coverImage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }
    }

    private ArrayList<Category> categoryArrayList;

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }
}