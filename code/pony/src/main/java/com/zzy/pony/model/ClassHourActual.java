package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the class_hour_actual database table.
 * 
 */
@Entity
@Table(name="class_hour_actual")
@NamedQuery(name="ClassHourActual.findAll", query="SELECT c FROM ClassHourActual c")
public class ClassHourActual implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;

	@Column(name="ACTUAL_HOURS")
	private int actualHours;

	@Temporal(TemporalType.DATE)
	@Column(name="BUSINESS_DATE")
	private Date businessDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Column(name="PLAN_HOURS")
	private int planHours;

	@Column(name="TEACHER_ID")
	private int teacherId;

	@Column(name="TERM_ID")
	private int termId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	@Column(name="YEAR_ID")
	private int yearId;

	public ClassHourActual() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getActualHours() {
		return this.actualHours;
	}

	public void setActualHours(int actualHours) {
		this.actualHours = actualHours;
	}

	public Date getBusinessDate() {
		return this.businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
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

	public int getPlanHours() {
		return this.planHours;
	}

	public void setPlanHours(int planHours) {
		this.planHours = planHours;
	}

	public int getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getTermId() {
		return this.termId;
	}

	public void setTermId(int termId) {
		this.termId = termId;
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

	public int getYearId() {
		return this.yearId;
	}

	public void setYearId(int yearId) {
		this.yearId = yearId;
	}

}