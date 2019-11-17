package io.hustler.wallzy.model.firebasepojo;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoriesDTO implements Serializable {
    private ArrayList<String> Categories;

    public CategoriesDTO(ArrayList<String> categories) {
        Categories = categories;

    }

    public ArrayList<String> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<String> categories) {
        Categories = categories;
    }
}