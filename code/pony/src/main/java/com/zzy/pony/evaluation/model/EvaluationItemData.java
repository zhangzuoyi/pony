package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the evaluation_item_data database table.
 * 
 */
@Entity
@Table(name="evaluation_item_data")
@NamedQuery(name="EvaluationItemData.findAll", query="SELECT e FROM EvaluationItemData e")
public class EvaluationItemData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Column(name="CHECK_SCORE")
	private float checkScore;

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

	@Column(name="SCORE")
	private float score;
	
	//打分依据
	@Column(name="ACCORDING")
	private String according;

	//bi-directional many-to-one association to EvaluationRecord
	@ManyToOne
	@JoinColumn(name="RECORD_ID")
	private EvaluationRecord record;

	//bi-directional many-to-one association to EvaluationItem
	@ManyToOne
	@JoinColumn(name="ITEM_ID")
	private EvaluationItem item;

	public EvaluationItemData() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getCheckScore() {
		return this.checkScore;
	}

	public void setCheckScore(float checkScore) {
		this.checkScore = checkScore;
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

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public EvaluationRecord getRecord() {
		return this.record;
	}

	public void setRecord(EvaluationRecord record) {
		this.record = record;
	}

	public EvaluationItem getItem() {
		return this.item;
	}

	public void setItem(EvaluationItem item) {
		this.item = item;
	}

	public String getAccording() {
		return according;
	}

	public void setAccording(String according) {
		this.according = according;
	}

}