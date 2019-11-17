package io.hustler.wallzy.model.firebasepojo;

import java.io.Serializable;

public class CollectionsDTO implements Serializable {
	private String nature;
	private String love;
	private String graphics;
	private String superHeros;

	public void setNature(String nature){
		this.nature = nature;
	}

	public String getNature(){
		return nature;
	}

	public void setLove(String love){
		this.love = love;
	}

	public String getLove(){
		return love;
	}

	public void setGraphics(String graphics){
		this.graphics = graphics;
	}

	public String getGraphics(){
		return graphics;
	}

	public void setSuperHeros(String superHeros){
		this.superHeros = superHeros;
	}

	public String getSuperHeros(){
		return superHeros;
	}
}