package com.zzy.pony.exam.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_exam_room database table.
 * 
 */
@Entity
@Table(name="t_exam_room")
@NamedQuery(name="ExamRoom.findAll", query="SELECT e FROM ExamRoom e")
public class ExamRoom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private Integer capacity;

	private String name;

	public ExamRoom() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}