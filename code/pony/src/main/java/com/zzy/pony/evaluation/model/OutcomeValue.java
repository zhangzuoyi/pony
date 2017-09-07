package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the teacher_outcome_value database table.
 * 
 */
@Entity
@Table(name="teacher_outcome_value")
@NamedQuery(name="OutcomeValue.findAll", query="SELECT o FROM OutcomeValue o")
public class OutcomeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Column(name="CATEGORY")
	private String category;

	@Column(name="LEVEL1")
	private String level1;

	@Column(name="LEVEL2")
	private String level2;

	@Column(name="SCORE")
	private float score;

	public OutcomeValue() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLevel1() {
		return this.level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return this.level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

}