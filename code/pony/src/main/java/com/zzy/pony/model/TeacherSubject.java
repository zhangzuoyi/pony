package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_teacher_subject database table.
 * 
 */
@Entity
@Table(name="t_teacher_subject")
@NamedQuery(name="TeacherSubject.findAll", query="SELECT t FROM TeacherSubject t")
public class TeacherSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TS_ID")
	private Integer tsId;

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	//bi-directional many-to-one association to Teacher
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	//bi-directional many-to-one association to SchoolClass
	@ManyToOne
	@JoinColumn(name="CLASS_ID")
	private SchoolClass schoolClass;
	
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear year;
	
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	public TeacherSubject() {
	}

	public Integer getTsId() {
		return this.tsId;
	}

	public void setTsId(Integer tsId) {
		this.tsId = tsId;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public SchoolYear getYear() {
		return year;
	}

	public void setYear(SchoolYear year) {
		this.year = year;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

}