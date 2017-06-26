package com.zzy.pony.exam.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_examinee_room_arrange database table.
 * 
 */
@Entity
@Table(name="t_examinee_room_arrange")
@NamedQuery(name="ExamineeRoomArrange.findAll", query="SELECT e FROM ExamineeRoomArrange e")
public class ExamineeRoomArrange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private Integer seq;

	//bi-directional many-to-one association to Examinee
	@ManyToOne
	@JoinColumn(name="EXAMINEE_ID")
	private Examinee examinee;

	//bi-directional many-to-one association to ExamRoomAllocate
	@ManyToOne
	@JoinColumn(name="ROOM_ID")
	private ExamRoomAllocate examRoomAllocate;

	public ExamineeRoomArrange() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Examinee getExaminee() {
		return this.examinee;
	}

	public void setExaminee(Examinee examinee) {
		this.examinee = examinee;
	}

	public ExamRoomAllocate getExamRoomAllocate() {
		return this.examRoomAllocate;
	}

	public void setExamRoomAllocate(ExamRoomAllocate examRoomAllocate) {
		this.examRoomAllocate = examRoomAllocate;
	}

}