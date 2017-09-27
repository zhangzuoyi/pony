package com.zzy.pony.tour.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class TourVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private long tourId;

	private int classId;
	private int classSeq;
	private String gradeName;

	private Date createTime;

	private String createUser;

	private String description;

	private int periodSeq;

	private int subjectId;

	private int teacherId;

	private Date tourDate;

	private int tourTeacherId;

	private Date updateTime;

	private String updateUser;

	private int weekSeq;
	
	private List<TourCategoryVo> categories;
	
	private String itemSummary;//巡课项的汇总描述

	public TourVo() {
	}

	public String getClassName(){
		return gradeName+"("+classSeq+")";
	}
	public long getTourId() {
		return this.tourId;
	}

	public void setTourId(long tourId) {
		this.tourId = tourId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPeriodSeq() {
		return this.periodSeq;
	}

	public void setPeriodSeq(int periodSeq) {
		this.periodSeq = periodSeq;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public Date getTourDate() {
		return this.tourDate;
	}

	public void setTourDate(Date tourDate) {
		this.tourDate = tourDate;
	}

	public int getTourTeacherId() {
		return this.tourTeacherId;
	}

	public void setTourTeacherId(int tourTeacherId) {
		this.tourTeacherId = tourTeacherId;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public int getWeekSeq() {
		return this.weekSeq;
	}

	public void setWeekSeq(int weekSeq) {
		this.weekSeq = weekSeq;
	}

	public List<TourCategoryVo> getCategories() {
		return categories;
	}

	public void setCategories(List<TourCategoryVo> categories) {
		this.categories = categories;
	}

	public String getItemSummary() {
		return itemSummary;
	}

	public void setItemSummary(String itemSummary) {
		this.itemSummary = itemSummary;
	}

	public int getClassSeq() {
		return classSeq;
	}

	public void setClassSeq(int classSeq) {
		this.classSeq = classSeq;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

}