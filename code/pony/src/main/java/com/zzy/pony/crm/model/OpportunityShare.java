package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the crm_opportunity_share database table.
 * 
 */
@Entity
@Table(name="crm_opportunity_share")
@NamedQuery(name="OpportunityShare.findAll", query="SELECT o FROM OpportunityShare o")
public class OpportunityShare implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Column(name="OPP_ID")
	private Long oppId;

	@Column(name="USER_NAME")
	private String userName;

	public OpportunityShare() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Long getOppId() {
		return this.oppId;
	}

	public void setOppId(Long oppId) {
		this.oppId = oppId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}