package io.hustler.wallzy.model.firebasepojo;

import java.util.List;
import java.io.Serializable;

public class CollectionImagesDTO implements Serializable {
	private List<String> nature;
	private List<String> love;
	private List<String> graphics;
	private List<String> superHeros;

	public void setNature(List<String> nature){
		this.nature = nature;
	}

	public List<String> getNature(){
		return nature;
	}

	public void setLove(List<String> love){
		this.love = love;
	}

	public List<String> getLove(){
		return love;
	}

	public void setGraphics(List<String> graphics){
		this.graphics = graphics;
	}

	public List<String> getGraphics(){
		return graphics;
	}

	public void setSuperHeros(List<String> superHeros){
		this.superHeros = superHeros;
	}

	public List<String> getSuperHeros(){
		return superHeros;
	}
}