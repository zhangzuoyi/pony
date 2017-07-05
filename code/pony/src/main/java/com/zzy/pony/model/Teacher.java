package com.zzy.pony.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;


/**
 * The persistent class for the t_teacher database table.
 * 
 */
@Entity
@Table(name="t_teacher")
@BatchSize(size=50)
@NamedQuery(name="Teacher.findAll", query="SELECT t FROM Teacher t")
public class Teacher implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TEACHER_ID")
	private Integer teacherId;

	private String birthday;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	private String email;

	@Temporal(TemporalType.DATE)
	@Column(name="ENTRANCE_DATE")
	private Date entranceDate;

	@Temporal(TemporalType.DATE)
	@Column(name="GRADUATE_DATE")
	private Date graduateDate;

	@Column(name="GRADUATE_SCHOOL")
	private String graduateSchool;

	@Column(name="HOME_ADDR")
	private String homeAddr;

	@Column(name="HOME_ZIPCODE")
	private String homeZipcode;

	@Column(name="ID_NO")
	private String idNo;

	@Column(name="ID_TYPE")
	private String idType;

	private String major;
	
	private String degree;

	private String name;

	private String nation;

	@Column(name="NATIVE_ADDR")
	private String nativeAddr;

	@Column(name="NATIVE_PLACE")
	private String nativePlace;

	private String phone;

	private String sex;

	@Temporal(TemporalType.DATE)
	@Column(name="START_WORK_DATE")
	private Date startWorkDate;

	@Column(name="TEACHER_NO")
	private String teacherNo;

	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	@Column(name="WORK_LENGTH")
	private Integer workLength;

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	public Teacher() {
	}

	public Integer getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEntranceDate() {
		return this.entranceDate;
	}

	public void setEntranceDate(Date entranceDate) {
		this.entranceDate = entranceDate;
	}

	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getGraduateSchool() {
		return this.graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getHomeAddr() {
		return this.homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getHomeZipcode() {
		return this.homeZipcode;
	}

	public void setHomeZipcode(String homeZipcode) {
		this.homeZipcode = homeZipcode;
	}

	public String getIdNo() {
		return this.idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIdType() {
		return this.idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativeAddr() {
		return this.nativeAddr;
	}

	public void setNativeAddr(String nativeAddr) {
		this.nativeAddr = nativeAddr;
	}

	public String getNativePlace() {
		return this.nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getStartWorkDate() {
		return this.startWorkDate;
	}

	public void setStartWorkDate(Date startWorkDate) {
		this.startWorkDate = startWorkDate;
	}

	public String getTeacherNo() {
		return this.teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getWorkLength() {
		return this.workLength;
	}

	public void setWorkLength(Integer workLength) {
		this.workLength = workLength;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

}