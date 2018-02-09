package com.zzy.pony.crm.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the crm_industry database table.
 * 
 */
@Entity
@Table(name="crm_industry")
@NamedQuery(name="Industry.findAll", query="SELECT i FROM Industry i")
public class Industry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private long id;

	@Column(name="NAME")
	private String name;

	@Column(name="TYPE")
	private String type;

	public Industry() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}