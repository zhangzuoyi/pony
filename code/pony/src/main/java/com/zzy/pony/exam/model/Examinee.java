package com.zzy.pony.exam.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Student;


/**
 * The persistent class for the t_examinee database table.
 * 
 */
@Entity
@Table(name="t_examinee")
@NamedQuery(name="Examinee.findAll", query="SELECT e FROM Examinee e")
public class Examinee implements Serializable,Comparable<Examinee> {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXAMINEE_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int examineeId;

	@Column(name="REG_NO")
	private String regNo;

	//add seatNo
	@Column(name="SEAT_NO")
	private String seatNo;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private Student student;

	//bi-directional many-to-one association to Exam
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private Exam exam;

	//bi-directional many-to-many association to ExamArrange
	@ManyToMany(mappedBy="examinees")
	private List<ExamArrange> examArranges;

	//bi-directional many-to-one association to ExamineeRoomArrange
	@OneToMany(mappedBy="examinee")
	private List<ExamineeRoomArrange> examineeRoomArranges;
	
	@Column(name="CLASS_RANK")
	private Integer classRank;
	@Column(name="GRADE_RANK")
	private Integer gradeRank;
	@Column(name="TOTAL_SCORE")
	private Float totalScore;

	public Examinee() {
	}

	public int getExamineeId() {
		return this.examineeId;
	}

	public void setExamineeId(int examineeId) {
		this.examineeId = examineeId;
	}

	public String getRegNo() {
		return this.regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Exam getExam() {
		return this.exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Integer getClassRank() {
		return classRank;
	}

	public void setClassRank(Integer classRank) {
		this.classRank = classRank;
	}

	public Integer getGradeRank() {
		return gradeRank;
	}

	public void setGradeRank(Integer gradeRank) {
		this.gradeRank = gradeRank;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}

	public List<ExamArrange> getExamArranges() {
		return this.examArranges;
	}

	public void setExamArranges(List<ExamArrange> examArranges) {
		this.examArranges = examArranges;
	}

	public List<ExamineeRoomArrange> getExamineeRoomArranges() {
		return this.examineeRoomArranges;
	}

	public void setExamineeRoomArranges(List<ExamineeRoomArrange> examineeRoomArranges) {
		this.examineeRoomArranges = examineeRoomArranges;
	}

	public ExamineeRoomArrange addExamineeRoomArrange(ExamineeRoomArrange examineeRoomArrange) {
		getExamineeRoomArranges().add(examineeRoomArrange);
		examineeRoomArrange.setExaminee(this);

		return examineeRoomArrange;
	}

	public ExamineeRoomArrange removeExamineeRoomArrange(ExamineeRoomArrange examineeRoomArrange) {
		getExamineeRoomArranges().remove(examineeRoomArrange);
		examineeRoomArrange.setExaminee(null);

		return examineeRoomArrange;
	}

	@Override
	public int compareTo(Examinee o) {
		// TODO Auto-generated method stub			
		return regNo.compareTo(o.regNo);
	}


    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}