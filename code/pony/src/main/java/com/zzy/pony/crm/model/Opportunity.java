package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the crm_opportunity database table.
 * 
 */
@Entity
@Table(name="crm_opportunity")
@NamedQuery(name="Opportunity.findAll", query="SELECT o FROM Opportunity o")
public class Opportunity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;

	@Column(name="AMOUNT_ESTIMATE")
	private Double amountEstimate;

	@Column(name="COMMENTS")
	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Column(name="CUSTOMER_ID")
	private Long customerId;

	@Temporal(TemporalType.DATE)
	@Column(name="DATE_ESTIMATE")
	private Date dateEstimate;

	@Column(name="MANAGER")
	private String manager;

	@Column(name="NAME")
	private String name;

	@Column(name="SOURCE")
	private Integer source;

	@Column(name="STAGE")
	private Integer stage;

	@Column(name="TYPE")
	private Integer type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	public Opportunity() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Double getAmountEstimate() {
		return this.amountEstimate;
	}

	public void setAmountEstimate(Double amountEstimate) {
		this.amountEstimate = amountEstimate;
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

	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Date getDateEstimate() {
		return this.dateEstimate;
	}

	public void setDateEstimate(Date dateEstimate) {
		this.dateEstimate = dateEstimate;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSource() {
		return this.source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getStage() {
		return this.stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

}