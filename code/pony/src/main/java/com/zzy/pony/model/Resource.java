package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_resource database table.
 * 
 */
@Entity
@Table(name="t_resource")
@NamedQuery(name="Resource.findAll", query="SELECT r FROM Resource r")
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RES_ID")
	private int resId;

	private String comments;

	@Column(name="PRES_ID")
	private int presId;

	@Column(name="RES_KEY")
	private String resKey;

	@Column(name="RES_LEVEL")
	private int resLevel;

	@Column(name="RES_NAME")
	private String resName;

	//bi-directional many-to-many association to Role
	@ManyToMany(mappedBy="resources")
	private List<Role> roles;

	public Resource() {
	}

	public int getResId() {
		return this.resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getPresId() {
		return this.presId;
	}

	public void setPresId(int presId) {
		this.presId = presId;
	}

	public String getResKey() {
		return this.resKey;
	}

	public void setResKey(String resKey) {
		this.resKey = resKey;
	}

	public int getResLevel() {
		return this.resLevel;
	}

	public void setResLevel(int resLevel) {
		this.resLevel = resLevel;
	}

	public String getResName() {
		return this.resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}