package com.zzy.pony.property.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zzy.pony.model.User;


/**
 * The persistent class for the t_consumable database table.
 * 
 */
@Entity
@Table(name="t_consumable")
@NamedQuery(name="Consumable.findAll", query="SELECT c FROM Consumable c")
public class Consumable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CSE_ID")
	private int cseId;

	private Integer amount;

	private String brand;

	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	private String description;

	private String location;

	private String name;

	private Float price;

	private String spec;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	//bi-directional many-to-one association to PropertyType
	@ManyToOne
	@JoinColumn(name="TYPE_ID")
	private PropertyType propertyType;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="OWNER_ID")
	private User user;

	//bi-directional many-to-one association to ConsumableTrace
//	@OneToMany(mappedBy="consumable")
//	private List<ConsumableTrace> traces;

	public Consumable() {
	}

	public int getCseId() {
		return this.cseId;
	}

	public void setCseId(int cseId) {
		this.cseId = cseId;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public PropertyType getPropertyType() {
		return this.propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}