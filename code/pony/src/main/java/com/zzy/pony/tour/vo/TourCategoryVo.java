package com.zzy.pony.tour.vo;

import java.util.ArrayList;
import java.util.List;

public class TourCategoryVo {
	private String category;
	private List<TourItemVo> items;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<TourItemVo> getItems() {
		if(items == null) {
			items=new ArrayList<TourItemVo>();
		}
		return items;
	}
	public void setItems(List<TourItemVo> items) {
		this.items = items;
	}
	
	
}
