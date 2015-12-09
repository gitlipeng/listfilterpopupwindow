package com.link.support.bean;

import java.util.List;


public class FilterBean {
	private String id;
	private String name;
	private boolean isSelect;
	private String normalImage;
	private String pressedImage;

	private List<FilterBean> childList;

	public List<FilterBean> getChildList() {
    	return childList;
    }

	public void setChildList(List<FilterBean> childList) {
    	this.childList = childList;
    }

    
	public String getNormalImage() {
		return normalImage;
	}


	public void setNormalImage(String normalImage) {
		this.normalImage = normalImage;
	}


	public String getPressedImage() {
		return pressedImage;
	}


	public void setPressedImage(String pressedImage) {
		this.pressedImage = pressedImage;
	}


	public String getId() {
    	return id;
    }
	
    public void setId(String id) {
    	this.id = id;
    }
	
    public String getName() {
    	return name;
    }
	
    public void setName(String name) {
    	this.name = name;
    }
	
    public boolean isSelect() {
    	return isSelect;
    }
	
    public void setSelect(boolean isSelect) {
    	this.isSelect = isSelect;
    }
	
}
