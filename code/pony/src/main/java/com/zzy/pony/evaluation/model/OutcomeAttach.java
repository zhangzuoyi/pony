package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the teacher_outcome_attach database table.
 * 
 */
@Entity
@Table(name="teacher_outcome_attach")
@NamedQuery(name="OutcomeAttach.findAll", query="SELECT o FROM OutcomeAttach o")
public class OutcomeAttach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;

	@Column(name="OLD_FILE_NAME")
	private String oldFileName;

	@Column(name="SAVE_PATH")
	private String savePath;

	//bi-directional many-to-one association to Outcome
	@ManyToOne
	@JoinColumn(name="OUTCOME_ID")
	private Outcome outcome;

	public OutcomeAttach() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOldFileName() {
		return this.oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String getSavePath() {
		return this.savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public Outcome getOutcome() {
		return this.outcome;
	}

	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

}