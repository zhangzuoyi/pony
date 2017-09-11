package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


/**
 * The persistent class for the evaluation_subject database table.
 * 
 */
@Entity
@Table(name="evaluation_subject")
@NamedQuery(name="EvaluationSubject.findAll", query="SELECT e FROM EvaluationSubject e")
@JsonIgnoreProperties(value={"items","records"})
public class EvaluationSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SUBJECT_ID")
	private Long subjectId;

	@Column(name="NAME")
	private String name;

	//bi-directional many-to-one association to EvaluationItem
	@OneToMany(mappedBy="subject")
	private List<EvaluationItem> items;

	//bi-directional many-to-one association to EvaluationRecord
	@OneToMany(mappedBy="subject")
	private List<EvaluationRecord> records;

	public EvaluationSubject() {
	}

	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EvaluationItem> getItems() {
		return this.items;
	}

	public void setItems(List<EvaluationItem> items) {
		this.items = items;
	}

	public EvaluationItem addItem(EvaluationItem item) {
		getItems().add(item);
		item.setSubject(this);

		return item;
	}

	public EvaluationItem removeItem(EvaluationItem item) {
		getItems().remove(item);
		item.setSubject(null);

		return item;
	}

	public List<EvaluationRecord> getRecords() {
		return this.records;
	}

	public void setRecords(List<EvaluationRecord> records) {
		this.records = records;
	}

	public EvaluationRecord addRecord(EvaluationRecord record) {
		getRecords().add(record);
		record.setSubject(this);

		return record;
	}

	public EvaluationRecord removeRecord(EvaluationRecord record) {
		getRecords().remove(record);
		record.setSubject(null);

		return record;
	}

}