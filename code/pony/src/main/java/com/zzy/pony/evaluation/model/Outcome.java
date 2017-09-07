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
 * The persistent class for the teacher_outcome database table.
 * 
 */
@Entity
@Table(name="teacher_outcome")
@NamedQuery(name="Outcome.findAll", query="SELECT o FROM Outcome o")
public class Outcome implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_UNCHECK=0;//未审核
	public static final int STATUS_CHECKED=1;//已审核

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="OUTCOME_ID")
	private Long outcomeId;

	@Column(name="CATEGORY")
	private String category;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECK_TIME")
	private Date checkTime;

	@Column(name="CHECK_USER")
	private String checkUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="LEVEL1")
	private String level1;

	@Column(name="LEVEL2")
	private String level2;

	@Temporal(TemporalType.DATE)
	@Column(name="OCCUR_DATE")
	private Date occurDate;

	@Column(name="SCORE")
	private float score;
	
	@Column(name="STATUS")
	private int status;

	//bi-directional many-to-one association to Teacher
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher teacher;

	//bi-directional many-to-one association to OutcomeAttach
	@OneToMany(mappedBy="outcome")
	private List<OutcomeAttach> attaches;

	public Outcome() {
	}

	public Long getOutcomeId() {
		return this.outcomeId;
	}

	public void setOutcomeId(Long outcomeId) {
		this.outcomeId = outcomeId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLevel1() {
		return this.level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return this.level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public Date getOccurDate() {
		return this.occurDate;
	}

	public void setOccurDate(Date occurDate) {
		this.occurDate = occurDate;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<OutcomeAttach> getAttaches() {
		return this.attaches;
	}

	public void setAttaches(List<OutcomeAttach> attaches) {
		this.attaches = attaches;
	}

	public OutcomeAttach addAttach(OutcomeAttach attach) {
		getAttaches().add(attach);
		attach.setOutcome(this);

		return attach;
	}

	public OutcomeAttach removeAttach(OutcomeAttach attach) {
		getAttaches().remove(attach);
		attach.setOutcome(null);

		return attach;
	}

}