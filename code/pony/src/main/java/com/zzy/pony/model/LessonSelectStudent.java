package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_lesson_select_student database table.
 * 
 */
@Entity
@Table(name="t_lesson_select_student")
@NamedQuery(name="LessonSelectStudent.findAll", query="SELECT l FROM LessonSelectStudent l")
public class LessonSelectStudent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer result;

	private Float score;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private Student student;

	//bi-directional many-to-one association to LessonSelectArrange
	@ManyToOne
	@JoinColumn(name="ARRANGE_ID")
	private LessonSelectArrange lessonSelectArrange;

	public LessonSelectStudent() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getResult() {
		return this.result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public LessonSelectArrange getLessonSelectArrange() {
		return this.lessonSelectArrange;
	}

	public void setLessonSelectArrange(LessonSelectArrange lessonSelectArrange) {
		this.lessonSelectArrange = lessonSelectArrange;
	}

}