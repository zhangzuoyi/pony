package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_exam_type database table.
 * 
 */
@Entity
@Table(name="t_exam_type")
@NamedQuery(name="ExamType.findAll", query="SELECT e FROM ExamType e")
public class ExamType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TYPE_ID")
	private int typeId;

	private String name;

	public ExamType() {
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}