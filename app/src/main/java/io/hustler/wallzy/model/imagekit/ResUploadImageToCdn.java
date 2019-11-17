package io.hustler.wallzy.model.imagekit;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ResUploadImageToCdn implements Serializable {

	@SerializedName("filePath")
	private String filePath;

	@SerializedName("size")
	private int size;

	@SerializedName("fileId")
	private String fileId;

	@SerializedName("url")
	private String url;

	@SerializedName("name")
	private String name;

	@SerializedName("fileType")
	private String fileType;

	@SerializedName("thumbnailUrl")
	private String thumbnailUrl;

	@SerializedName("width")
	private int width;

	@SerializedName("height")
	private int height;

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setSize(int size){
		this.size = size;
	}

	public int getSize(){
		return size;
	}

	public void setFileId(String fileId){
		this.fileId = fileId;
	}

	public String getFileId(){
		return fileId;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFileType(){
		return fileType;
	}

	public void setThumbnailUrl(String thumbnailUrl){
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getThumbnailUrl(){
		return thumbnailUrl;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"ResUploadImageToCdn{" +
			"filePath = '" + filePath + '\'' + 
			",size = '" + size + '\'' + 
			",fileId = '" + fileId + '\'' + 
			",url = '" + url + '\'' + 
			",name = '" + name + '\'' + 
			",fileType = '" + fileType + '\'' + 
			",thumbnailUrl = '" + thumbnailUrl + '\'' + 
			",width = '" + width + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}
}