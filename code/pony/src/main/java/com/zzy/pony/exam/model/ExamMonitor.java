package com.zzy.pony.exam.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.Teacher;


/**
 * The persistent class for the t_exam_arrange database table.
 * 
 */
@Entity
@Table(name="t_exam_monitor")
public class ExamMonitor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="MONITOR_COUNT")
	private Integer monitorCount;

	//bi-directional many-to-one association to Exam
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private Exam exam;
	
	@ManyToOne
	@JoinColumn(name="GRADE_ID")
	private Grade grade;

	//bi-directional many-to-one association to Grade
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	public ExamMonitor() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getMonitorCount() {
		return monitorCount;
	}

	public void setMonitorCount(Integer monitorCount) {
		this.monitorCount = monitorCount;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

}