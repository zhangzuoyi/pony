package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_lesson_arrange database table.
 * 
 */
@Entity
@Table(name="t_lesson_arrange")
@NamedQuery(name="LessonArrange.findAll", query="SELECT l FROM LessonArrange l")
public class LessonArrange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ARRANGE_ID")
	private Integer arrangeId;

	@Column(name="CLASS_ID")
	private int classId;

	@Column(name="OTHER_LESSON")
	private String otherLesson;

	@Column(name="WEEK_DAY")
	private String weekDay;

	//bi-directional many-to-one association to LessonPeriod
	@ManyToOne
	@JoinColumn(name="PERIOD_ID")
	private LessonPeriod lessonPeriod;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;
	
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	public LessonArrange() {
	}

	public Integer getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getOtherLesson() {
		return this.otherLesson;
	}

	public void setOtherLesson(String otherLesson) {
		this.otherLesson = otherLesson;
	}

	public String getWeekDay() {
		return this.weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public LessonPeriod getLessonPeriod() {
		return this.lessonPeriod;
	}

	public void setLessonPeriod(LessonPeriod lessonPeriod) {
		this.lessonPeriod = lessonPeriod;
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

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

}