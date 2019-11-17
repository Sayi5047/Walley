package io.hustler.wallzy.model.firebasepojo;

import java.util.List;
import java.io.Serializable;

public class ResponseDTO implements Serializable {
	private CategoriesDTO categories;
	private CategoryImagesDTO categoryImages;
	private List<String> latestImages;
	private CollectionsDTO collections;
	private CollectionImagesDTO collectionImages;

	public void setCategories(CategoriesDTO categories){
		this.categories = categories;
	}

	public CategoriesDTO getCategories(){
		return categories;
	}

	public void setCategoryImages(CategoryImagesDTO categoryImages){
		this.categoryImages = categoryImages;
	}

	public CategoryImagesDTO getCategoryImages(){
		return categoryImages;
	}

	public void setLatestImages(List<String> latestImages){
		this.latestImages = latestImages;
	}

	public List<String> getLatestImages(){
		return latestImages;
	}

	public void setCollections(CollectionsDTO collections){
		this.collections = collections;
	}

	public CollectionsDTO getCollections(){
		return collections;
	}

	public void setCollectionImages(CollectionImagesDTO collectionImages){
		this.collectionImages = collectionImages;
	}

	public CollectionImagesDTO getCollectionImages(){
		return collectionImages;
	}
}