package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_area database table.
 * 
 */
@Entity
@Table(name="t_area")
@NamedQuery(name="Area.findAll", query="SELECT a FROM Area a")
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CODE")
	private String code;

	@Column(name="NAME")
	private String name;

	@Column(name="SIMPLE_NAME")
	private String simpleName;

	public Area() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return this.simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

}