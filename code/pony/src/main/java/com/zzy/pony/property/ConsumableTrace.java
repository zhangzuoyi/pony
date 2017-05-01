package com.zzy.pony.property;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the t_consumable_trace database table.
 * 
 */
@Entity
@Table(name="t_consumable_trace")
@NamedQuery(name="ConsumableTrace.findAll", query="SELECT c FROM ConsumableTrace c")
public class ConsumableTrace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="AMOUNT_AFTER")
	private Integer amountAfter;

	private String comments;

	private Integer num;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATE_TIME")
	private Date operateTime;

	private String operation;

	private String operator;

	//bi-directional many-to-one association to Consumable
	@ManyToOne
	@JoinColumn(name="CSE_ID")
	private Consumable consumable;

	public ConsumableTrace() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAmountAfter() {
		return this.amountAfter;
	}

	public void setAmountAfter(Integer amountAfter) {
		this.amountAfter = amountAfter;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Consumable getConsumable() {
		return this.consumable;
	}

	public void setConsumable(Consumable consumable) {
		this.consumable = consumable;
	}

}