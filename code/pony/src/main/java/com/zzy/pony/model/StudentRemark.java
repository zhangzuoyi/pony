package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the t_student_remark database table.
 * 
 */
@Entity
@Table(name="t_student_remark")
@NamedQuery(name="StudentRemark.findAll", query="SELECT s FROM StudentRemark s")
public class StudentRemark implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String remark;

	@Column(name="REMARK_LEVEL")
	private String remarkLevel;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REMARK_TIME")
	private Date remarkTime;

	//bi-directional many-to-one association to Teacher
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private Student student;

	public StudentRemark() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkLevel() {
		return this.remarkLevel;
	}

	public void setRemarkLevel(String remarkLevel) {
		this.remarkLevel = remarkLevel;
	}

	public Date getRemarkTime() {
		return this.remarkTime;
	}

	public void setRemarkTime(Date remarkTime) {
		this.remarkTime = remarkTime;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public SchoolYear getSchoolYear() {
		return this.schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return this.term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}