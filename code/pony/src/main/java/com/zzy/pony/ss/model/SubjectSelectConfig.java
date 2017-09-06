package com.zzy.pony.ss.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zzy.pony.model.SchoolYear;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_subject_select_config database table.
 * 
 */
@Entity
@Table(name="t_subject_select_config")
@NamedQuery(name="SubjectSelectConfig.findAll", query="SELECT s FROM SubjectSelectConfig s")
@JsonIgnoreProperties(value={"studentSubjectSelects"})
public class SubjectSelectConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CONFIG_ID")
	private int configId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME")
	private Date endTime;

	@Column(name="IS_CURRENT")
	private String isCurrent;

	@Column(name="SELECT_NUM")
	private Integer selectNum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;

	private String subjects;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	//bi-directional many-to-one association to StudentSubjectSelect
	@OneToMany(mappedBy="subjectSelectConfig")
	private List<StudentSubjectSelect> studentSubjectSelects;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	public SubjectSelectConfig() {
	}

	public String[] getSubjectArray(){
		return subjects.split(",");
	}
	public int getConfigId() {
		return this.configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsCurrent() {
		return this.isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Integer getSelectNum() {
		return this.selectNum;
	}

	public void setSelectNum(Integer selectNum) {
		this.selectNum = selectNum;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getSubjects() {
		return this.subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
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

	public List<StudentSubjectSelect> getStudentSubjectSelects() {
		return this.studentSubjectSelects;
	}

	public void setStudentSubjectSelects(List<StudentSubjectSelect> studentSubjectSelects) {
		this.studentSubjectSelects = studentSubjectSelects;
	}

	public StudentSubjectSelect addStudentSubjectSelect(StudentSubjectSelect studentSubjectSelect) {
		getStudentSubjectSelects().add(studentSubjectSelect);
		studentSubjectSelect.setSubjectSelectConfig(this);

		return studentSubjectSelect;
	}

	public StudentSubjectSelect removeStudentSubjectSelect(StudentSubjectSelect studentSubjectSelect) {
		getStudentSubjectSelects().remove(studentSubjectSelect);
		studentSubjectSelect.setSubjectSelectConfig(null);

		return studentSubjectSelect;
	}

	public SchoolYear getSchoolYear() {
		return this.schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

}