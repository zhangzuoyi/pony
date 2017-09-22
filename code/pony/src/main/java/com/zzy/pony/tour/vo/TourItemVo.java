package com.zzy.pony.tour.vo;

import java.io.Serializable;

public class TourItemVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private long itemId;

	private String category;

	private String name;
	
	private boolean isCheck;
	
	private long tourId;

	public TourItemVo() {
	}

	public long getItemId() {
		return this.itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public long getTourId() {
		return tourId;
	}

	public void setTourId(long tourId) {
		this.tourId = tourId;
	}

}