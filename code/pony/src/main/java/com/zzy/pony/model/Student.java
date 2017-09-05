package com.zzy.pony.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the t_student database table.
 * 
 */
@Entity
@Table(name="t_student")
@NamedQuery(name="Student.findAll", query="SELECT s FROM Student s")
@JsonIgnoreProperties(value={"schoolClasses"})
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="STUDENT_ID")
	private Integer studentId;

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

	@Column(name="ENTRANCE_TYPE")
	private String entranceType;

	@Temporal(TemporalType.DATE)
	@Column(name="GRADUATE_DATE")
	private Date graduateDate;

	@Column(name="GRADUATE_TYPE")
	private String graduateType;

	@Column(name="HOME_ADDR")
	private String homeAddr;

	@Column(name="HOME_ZIPCODE")
	private String homeZipcode;

	@Column(name="ID_NO")
	private String idNo;

	@Column(name="ID_TYPE")
	private String idType;

	private String name;

	private String nation;

	@Column(name="NATIVE_ADDR")
	private String nativeAddr;

	@Column(name="NATIVE_PLACE")
	private String nativePlace;

	private String phone;

	private String sex;

	private String status;

	@Column(name="STUDENT_NO")
	private String studentNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	//bi-directional many-to-many association to SchoolClass
	@ManyToMany
	@JoinTable(
		name="t_student_class_relation"
		, joinColumns={
			@JoinColumn(name="STUDENT_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="CLASS_ID")
			}
		)
	private List<SchoolClass> schoolClasses;

	//bi-directional many-to-one association to SchoolClass
	@ManyToOne
	@JoinColumn(name="CLASS_ID")
	private SchoolClass schoolClass;
	
	@Column(name="EXAM_SUBJECTS")
	private String examSubjects;

	public Student() {
	}

	public Integer getStudentId() {
		return this.studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
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

	public String getEntranceType() {
		return this.entranceType;
	}

	public void setEntranceType(String entranceType) {
		this.entranceType = entranceType;
	}

	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getGraduateType() {
		return this.graduateType;
	}

	public void setGraduateType(String graduateType) {
		this.graduateType = graduateType;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStudentNo() {
		return this.studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
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

	public List<SchoolClass> getSchoolClasses() {
		if(schoolClasses == null){
			schoolClasses=new ArrayList<SchoolClass>();
		}
		return this.schoolClasses;
	}

	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public String getExamSubjects() {
		return examSubjects;
	}

	public void setExamSubjects(String examSubjects) {
		this.examSubjects = examSubjects;
	}

}