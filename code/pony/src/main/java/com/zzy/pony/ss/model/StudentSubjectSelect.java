package com.zzy.pony.ss.model;

import java.io.Serializable;
import javax.persistence.*;

import com.zzy.pony.model.Student;

import java.util.Date;


/**
 * The persistent class for the t_student_subject_select database table.
 * 
 */
@Entity
@Table(name="t_student_subject_select")
@NamedQuery(name="StudentSubjectSelect.findAll", query="SELECT s FROM StudentSubjectSelect s")
public class StudentSubjectSelect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	private String subject;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private Student student;

	//bi-directional many-to-one association to SubjectSelectConfig
	@ManyToOne
	@JoinColumn(name="CONFIG_ID")
	private SubjectSelectConfig subjectSelectConfig;

	public StudentSubjectSelect() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public SubjectSelectConfig getSubjectSelectConfig() {
		return this.subjectSelectConfig;
	}

	public void setSubjectSelectConfig(SubjectSelectConfig subjectSelectConfig) {
		this.subjectSelectConfig = subjectSelectConfig;
	}

}