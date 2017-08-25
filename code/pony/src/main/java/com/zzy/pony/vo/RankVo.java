package com.zzy.pony.vo;



public class RankVo {

	private int studentId;
	private String studentName;
	private int seq;
	private String subjectName;
	private float subjectScore;
	private int subjectClassRank;
	private int subjectGradeRank;
	private float totalScore;
	private int classRank;
	private int gradeRank;

	public RankVo(){}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public float getSubjectScore() {
		return subjectScore;
	}

	public void setSubjectScore(float subjectScore) {
		this.subjectScore = subjectScore;
	}

	public int getSubjectClassRank() {
		return subjectClassRank;
	}

	public void setSubjectClassRank(int subjectClassRank) {
		this.subjectClassRank = subjectClassRank;
	}

	public int getSubjectGradeRank() {
		return subjectGradeRank;
	}

	public void setSubjectGradeRank(int subjectGradeRank) {
		this.subjectGradeRank = subjectGradeRank;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public int getClassRank() {
		return classRank;
	}

	public void setClassRank(int classRank) {
		this.classRank = classRank;
	}

	public int getGradeRank() {
		return gradeRank;
	}

	public void setGradeRank(int gradeRank) {
		this.gradeRank = gradeRank;
	}
}
