package com.zzy.pony.vo;

public class ExamResultVo {
	private Long resultId;
	private Integer studentId;
	private String studentNo;
	private String studentName;
	private Integer subjectId;
	private String subjectName;
	private Integer examId;
	private String examName;
	private Float score;
	private int classRank;
	private int gradeRank;
	private int classId;
	private int classSeq;
	
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public Long getResultId() {
		return resultId;
	}
	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
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
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getClassSeq() {
		return classSeq;
	}
	public void setClassSeq(int classSeq) {
		this.classSeq = classSeq;
	}
	
	
	
}
