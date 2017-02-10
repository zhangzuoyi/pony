package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_lesson_select_arrange database table.
 * 
 */
@Entity
@Table(name="t_lesson_select_arrange")
@NamedQuery(name="LessonSelectArrange.findAll", query="SELECT l FROM LessonSelectArrange l")
public class LessonSelectArrange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ARRANGE_ID")
	private Integer arrangeId;

	private String classroom;

	private int credit;

	@Column(name="LOWER_LIMIT")
	private int lowerLimit;

	private int peroid;

	@Column(name="UPPER_LIMIT")
	private int upperLimit;

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

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	//bi-directional many-to-many association to Grade
	@ManyToMany
	@JoinTable(
		name="t_lesson_select_grade"
		, joinColumns={
			@JoinColumn(name="ARRANGE_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="GRADE_ID")
			}
		)
	private List<Grade> grades;

	//bi-directional many-to-one association to LessonSelectStudent
	@OneToMany(mappedBy="lessonSelectArrange")
	private List<LessonSelectStudent> students;

	//bi-directional many-to-one association to LessonSelectTime
	@OneToMany(mappedBy="lessonSelectArrange")
	private List<LessonSelectTime> lessonSelectTimes;

	public LessonSelectArrange() {
	}

	public Integer getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}

	public String getClassroom() {
		return this.classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public int getCredit() {
		return this.credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getLowerLimit() {
		return this.lowerLimit;
	}

	public void setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public int getPeroid() {
		return this.peroid;
	}

	public void setPeroid(int peroid) {
		this.peroid = peroid;
	}

	public int getUpperLimit() {
		return this.upperLimit;
	}

	public void setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
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

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<Grade> getGrades() {
		return this.grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public List<LessonSelectStudent> getStudents() {
		return this.students;
	}

	public void setStudents(List<LessonSelectStudent> students) {
		this.students = students;
	}

	public LessonSelectStudent addStudent(LessonSelectStudent student) {
		getStudents().add(student);
		student.setLessonSelectArrange(this);

		return student;
	}

	public LessonSelectStudent removeStudent(LessonSelectStudent student) {
		getStudents().remove(student);
		student.setLessonSelectArrange(null);

		return student;
	}

	public List<LessonSelectTime> getLessonSelectTimes() {
		return this.lessonSelectTimes;
	}

	public void setLessonSelectTimes(List<LessonSelectTime> lessonSelectTimes) {
		this.lessonSelectTimes = lessonSelectTimes;
	}

	public LessonSelectTime addLessonSelectTime(LessonSelectTime lessonSelectTime) {
		getLessonSelectTimes().add(lessonSelectTime);
		lessonSelectTime.setLessonSelectArrange(this);

		return lessonSelectTime;
	}

	public LessonSelectTime removeLessonSelectTime(LessonSelectTime lessonSelectTime) {
		getLessonSelectTimes().remove(lessonSelectTime);
		lessonSelectTime.setLessonSelectArrange(null);

		return lessonSelectTime;
	}

}