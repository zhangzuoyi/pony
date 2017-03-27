package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_weekday database table.
 * 
 */
@Entity
@Table(name="t_weekday")
@NamedQuery(name="Weekday.findAll", query="SELECT w FROM Weekday w")
public class Weekday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int seq;

	@Column(name="HAVE_CLASS")
	private Integer haveClass;

	private String name;

	public Weekday() {
	}

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public Integer getHaveClass() {
		return this.haveClass;
	}

	public void setHaveClass(Integer haveClass) {
		this.haveClass = haveClass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}