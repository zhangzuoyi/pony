package com.zzy.pony.exam.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.Subject;


/**
 * The persistent class for the t_exam_arrange database table.
 * 
 */
@Entity
@Table(name="t_exam_arrange")
@NamedQuery(name="ExamArrange.findAll", query="SELECT e FROM ExamArrange e")
public class ExamArrange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ARRANGE_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int arrangeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME")
	private Date endTime;

	@Temporal(TemporalType.DATE)
	@Column(name="EXAM_DATE")
	private Date examDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;

	//bi-directional many-to-one association to ExamArrangeGroup
	@ManyToOne
	@JoinColumn(name="GROUP_ID")
	private ExamArrangeGroup group;

	//bi-directional many-to-one association to Exam
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private Exam exam;

	//bi-directional many-to-one association to Grade
	@ManyToOne
	@JoinColumn(name="GRADE_ID")
	private Grade grade;

	//bi-directional many-to-one association to Subject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	//bi-directional many-to-one association to ExamRoomAllocate
	@OneToMany(mappedBy="examArrange")
	private List<ExamRoomAllocate> examRoomAllocates;

	//bi-directional many-to-many association to Examinee
	@ManyToMany
	@JoinTable(
		name="t_examinee_arrange"
		, joinColumns={
			@JoinColumn(name="ARRANGE_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="EXAMINEE_ID")
			}
		)
	private List<Examinee> examinees;

	public ExamArrange() {
	}

	public int getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(int arrangeId) {
		this.arrangeId = arrangeId;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getExamDate() {
		return this.examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public ExamArrangeGroup getGroup() {
		return this.group;
	}

	public void setGroup(ExamArrangeGroup group) {
		this.group = group;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Grade getGrade() {
		return this.grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<ExamRoomAllocate> getExamRoomAllocates() {
		return this.examRoomAllocates;
	}

	public void setExamRoomAllocates(List<ExamRoomAllocate> examRoomAllocates) {
		this.examRoomAllocates = examRoomAllocates;
	}

	public ExamRoomAllocate addExamRoomAllocate(ExamRoomAllocate examRoomAllocate) {
		getExamRoomAllocates().add(examRoomAllocate);
		examRoomAllocate.setExamArrange(this);

		return examRoomAllocate;
	}

	public ExamRoomAllocate removeExamRoomAllocate(ExamRoomAllocate examRoomAllocate) {
		getExamRoomAllocates().remove(examRoomAllocate);
		examRoomAllocate.setExamArrange(null);

		return examRoomAllocate;
	}

	public List<Examinee> getExaminees() {
		return this.examinees;
	}

	public void setExaminees(List<Examinee> examinees) {
		this.examinees = examinees;
	}

}