package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zzy.pony.model.Teacher;


/**
 * The persistent class for the evaluation_record database table.
 * 
 */
@Entity
@Table(name="evaluation_record")
@NamedQuery(name="EvaluationRecord.findAll", query="SELECT e FROM EvaluationRecord e")
public class EvaluationRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RECORD_ID")
	private Long recordId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECK_TIME")
	private Date checkTime;

	@Column(name="CHECK_USER")
	private String checkUser;

	@Column(name="COMMENTS")
	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Column(name="EVL_RESULT")
	private String evlResult;

	@Column(name="EVL_TIME")
	private String evlTime;

	@Column(name="RANK")
	private int rank;

	@Column(name="TOTAL_SCORE")
	private float totalScore;

	//bi-directional many-to-one association to EvaluationItemData
	@OneToMany(mappedBy="record")
	private List<EvaluationItemData> itemData;

	//bi-directional many-to-one association to Teacher
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	//bi-directional many-to-one association to EvaluationSubject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private EvaluationSubject subject;

	public EvaluationRecord() {
	}

	public Long getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getEvlResult() {
		return this.evlResult;
	}

	public void setEvlResult(String evlResult) {
		this.evlResult = evlResult;
	}

	public String getEvlTime() {
		return this.evlTime;
	}

	public void setEvlTime(String evlTime) {
		this.evlTime = evlTime;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public float getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public List<EvaluationItemData> getItemData() {
		return this.itemData;
	}

	public void setItemData(List<EvaluationItemData> itemData) {
		this.itemData = itemData;
	}

	public EvaluationItemData addItemData(EvaluationItemData itemData) {
		getItemData().add(itemData);
		itemData.setRecord(this);

		return itemData;
	}

	public EvaluationItemData removeItemData(EvaluationItemData itemData) {
		getItemData().remove(itemData);
		itemData.setRecord(null);

		return itemData;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public EvaluationSubject getSubject() {
		return this.subject;
	}

	public void setSubject(EvaluationSubject subject) {
		this.subject = subject;
	}

}