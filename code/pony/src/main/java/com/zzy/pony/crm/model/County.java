package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_county database table.
 * 
 */
@Entity
@Table(name="t_county")
@NamedQuery(name="County.findAll", query="SELECT c FROM County c")
public class County implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CODE")
	private String code;

	@Column(name="CITY_CODE")
	private String cityCode;

	@Column(name="NAME")
	private String name;

	@Column(name="PROVINCE_CODE")
	private String provinceCode;

	@Column(name="SIMPLE_NAME")
	private String simpleName;

	public County() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getSimpleName() {
		return this.simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

}