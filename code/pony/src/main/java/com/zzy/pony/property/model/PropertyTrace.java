package com.zzy.pony.property.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the t_property_trace database table.
 * 
 */
@Entity
@Table(name="t_property_trace")
@NamedQuery(name="PropertyTrace.findAll", query="SELECT p FROM PropertyTrace p")
public class PropertyTrace implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATE_TIME")
	private Date operateTime;

	private String operation;

	private String operator;

	//bi-directional many-to-one association to Property
	@ManyToOne
	@JoinColumn(name="PROP_ID")
	private Property property;

	public PropertyTrace() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Property getProperty() {
		return this.property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

}