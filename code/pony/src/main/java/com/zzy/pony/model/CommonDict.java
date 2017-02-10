package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_common_dict database table.
 * 
 */
@Entity
@Table(name="t_common_dict")
@NamedQuery(name="CommonDict.findAll", query="SELECT c FROM CommonDict c")
public class CommonDict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DICT_ID")
	private Integer dictId;

	private String code;

	@Column(name="DICT_TYPE")
	private String dictType;

	private String value;

	public CommonDict() {
	}

	public Integer getDictId() {
		return this.dictId;
	}

	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDictType() {
		return this.dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}