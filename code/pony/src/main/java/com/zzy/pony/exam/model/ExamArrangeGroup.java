package com.zzy.pony.exam.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;


/**
 * The persistent class for the t_exam_arrange_group database table.
 * 
 */
@Entity
@Table(name="t_exam_arrange_group")
@NamedQuery(name="ExamArrangeGroup.findAll", query="SELECT e FROM ExamArrangeGroup e")
public class ExamArrangeGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GROUP_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int groupId;

	private String name;

	//bi-directional many-to-one association to ExamArrange
	@OneToMany(mappedBy="group")
	private List<ExamArrange> examArranges;

	//bi-directional many-to-one association to Grade
	@ManyToOne
	@JoinColumn(name="GRADE_ID")
	private Grade grade;

	//bi-directional many-to-one association to Exam
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private Exam exam;

	public ExamArrangeGroup() {
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ExamArrange> getExamArranges() {
		return this.examArranges;
	}

	public void setExamArranges(List<ExamArrange> examArranges) {
		this.examArranges = examArranges;
	}

	public ExamArrange addExamArrange(ExamArrange examArrange) {
		getExamArranges().add(examArrange);
		examArrange.setGroup(this);

		return examArrange;
	}

	public ExamArrange removeExamArrange(ExamArrange examArrange) {
		getExamArranges().remove(examArrange);
		examArrange.setGroup(null);

		return examArrange;
	}

	public Grade getGrade() {
		return this.grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

}