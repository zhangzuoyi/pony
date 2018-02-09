package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_province database table.
 * 
 */
@Entity
@Table(name="t_province")
@NamedQuery(name="Province.findAll", query="SELECT p FROM Province p")
public class Province implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CODE")
	private String code;

	@Column(name="AREA_CODE")
	private String areaCode;

	@Column(name="NAME")
	private String name;

	@Column(name="SIMPLE_NAME")
	private String simpleName;

	public Province() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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