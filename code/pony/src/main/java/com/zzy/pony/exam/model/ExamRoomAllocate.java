package com.zzy.pony.exam.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.zzy.pony.model.Teacher;


/**
 * The persistent class for the t_exam_room_allocate database table.
 * 
 */
@Entity
@Table(name="t_exam_room_allocate")
@NamedQuery(name="ExamRoomAllocate.findAll", query="SELECT e FROM ExamRoomAllocate e")
public class ExamRoomAllocate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROOM_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int roomId;

	private Integer capacity;

	@Column(name="ROOM_NAME")
	private String roomName;

	@Column(name="ROOM_SEQ")
	private Integer roomSeq;

	//bi-directional many-to-one association to ExamArrange
	@ManyToOne
	@JoinColumn(name="ARRANGE_ID")
	private ExamArrange examArrange;
	
	@ManyToOne
	@JoinColumn(name="TEACHER_ID")
	private Teacher examTeacher;//监考老师

	//bi-directional many-to-one association to ExamineeRoomArrange
	@OneToMany(mappedBy="examRoomAllocate")
	private List<ExamineeRoomArrange> examineeRoomArranges;

	public ExamRoomAllocate() {
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getRoomName() {
		return this.roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Integer getRoomSeq() {
		return this.roomSeq;
	}

	public void setRoomSeq(Integer roomSeq) {
		this.roomSeq = roomSeq;
	}

	public ExamArrange getExamArrange() {
		return this.examArrange;
	}

	public void setExamArrange(ExamArrange examArrange) {
		this.examArrange = examArrange;
	}

	public Teacher getExamTeacher() {
		return examTeacher;
	}

	public void setExamTeacher(Teacher examTeacher) {
		this.examTeacher = examTeacher;
	}

	public List<ExamineeRoomArrange> getExamineeRoomArranges() {
		return this.examineeRoomArranges;
	}

	public void setExamineeRoomArranges(List<ExamineeRoomArrange> examineeRoomArranges) {
		this.examineeRoomArranges = examineeRoomArranges;
	}

	public ExamineeRoomArrange addExamineeRoomArrange(ExamineeRoomArrange examineeRoomArrange) {
		getExamineeRoomArranges().add(examineeRoomArrange);
		examineeRoomArrange.setExamRoomAllocate(this);

		return examineeRoomArrange;
	}

	public ExamineeRoomArrange removeExamineeRoomArrange(ExamineeRoomArrange examineeRoomArrange) {
		getExamineeRoomArranges().remove(examineeRoomArrange);
		examineeRoomArrange.setExamRoomAllocate(null);

		return examineeRoomArrange;
	}

}