package com.zzy.pony.exam.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the average_index database table.
 * 
 */
@Entity
@Table(name="average_index")
@NamedQuery(name="AverageIndex.findAll", query="SELECT a FROM AverageIndex a")
public class AverageIndex implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;

	@Column(name="EXAM_ID")
	private int examId;

	@Column(name="GRADE_ID")
	private int gradeId;

	@Column(name="INDEX_VALUE")
	private float indexValue;

	@Column(name="SECTION")
	private String section;

	@Column(name="SUBJECT_ID")
	private int subjectId;

	public AverageIndex() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getExamId() {
		return this.examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public float getIndexValue() {
		return this.indexValue;
	}

	public void setIndexValue(float indexValue) {
		this.indexValue = indexValue;
	}

	public String getSection() {
		return this.section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

}