package com.zzy.pony.vo;

import java.util.Date;

public class ClassHourActualVo {
	private long id;

	private int actualHours;

	private Date businessDate;

	private Date createTime;

	private String createUser;

	private int planHours;

	private int teacherId;

	private int termId;

	private Date updateTime;

	private String updateUser;

	private int yearId;
	private String teacherNo;
	private String teacherName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getActualHours() {
		return actualHours;
	}
	public void setActualHours(int actualHours) {
		this.actualHours = actualHours;
	}
	public Date getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getPlanHours() {
		return planHours;
	}
	public void setPlanHours(int planHours) {
		this.planHours = planHours;
	}
	public int getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	public int getTermId() {
		return termId;
	}
	public void setTermId(int termId) {
		this.termId = termId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public int getYearId() {
		return yearId;
	}
	public void setYearId(int yearId) {
		this.yearId = yearId;
	}
	public String getTeacherNo() {
		return teacherNo;
	}
	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
}
