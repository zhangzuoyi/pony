package com.zzy.pony.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.zzy.pony.vo.ExamSubjectVo;


/**
 * The persistent class for the t_exam_subject database table.
 * 
 */
@Entity
@Table(name="t_exam_subject")
@NamedQuery(name="ExamSubject.findAll", query="SELECT e FROM ExamSubject e")
public class ExamSubject implements Serializable {
	public static final int DEFAULT_WEIGHT=100;
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int weight;

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	//bi-directional many-to-one association to Exam
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private Exam exam;

	public ExamSubjectVo toVo() {
		ExamSubjectVo vo = new ExamSubjectVo();
		vo.setExamId(exam.getExamId());
		vo.setId(id);
		vo.setSubject(subject);
		vo.setWeight(weight);

		return vo;
	}
	
	public ExamSubject() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

}