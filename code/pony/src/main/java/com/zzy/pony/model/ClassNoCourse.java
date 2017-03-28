package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_class_no_course database table.
 * 
 */
@Entity
@Table(name="t_class_no_course")
@NamedQuery(name="ClassNoCourse.findAll", query="SELECT c FROM ClassNoCourse c")
public class ClassNoCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	//bi-directional many-to-one association to SchoolClass
	@ManyToOne
	@JoinColumn(name="CLASS_ID")
	private SchoolClass schoolClass;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	//bi-directional many-to-one association to Weekday
	@ManyToOne
	@JoinColumn(name="SEQ")
	private Weekday weekday;

	//bi-directional many-to-one association to LessonPeriod
	@ManyToOne
	@JoinColumn(name="PERIOD_ID")
	private LessonPeriod lessonPeriod;

	public ClassNoCourse() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SchoolClass getSchoolClass() {
		return this.schoolClass;
	}

	public void setSchoolClass(SchoolClass schoolClass) {
		this.schoolClass = schoolClass;
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

	public Weekday getWeekday() {
		return this.weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	public LessonPeriod getLessonPeriod() {
		return this.lessonPeriod;
	}

	public void setLessonPeriod(LessonPeriod lessonPeriod) {
		this.lessonPeriod = lessonPeriod;
	}

}