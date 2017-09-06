package com.zzy.pony.vo;

public class ConditionVo {
	private int yearId;
	private int termId;
	private int gradeId;
	private int examTypeId;//考试类型，由examId替换
	private String[] schoolClasses;
	private String[] subjects;
	private int examId;
	
	private int classId;
	private int studentId;
	private String[] examTypeIds;
	
	private int subjectId;
	private int teacherId;
	
	private int messageId;
	
	private String sourceType;

	private int weekdayId;
	private int periodId;
	
	public ConditionVo(){
		
	}
	
	public int getYearId() {
		return yearId;
	}

	public void setYearId(int yearId) {
		this.yearId = yearId;
	}

	public int getTermId() {
		return termId;
	}

	public void setTermId(int termId) {
		this.termId = termId;
	}

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public int getExamTypeId() {
		return examTypeId;
	}

	public void setExamTypeId(int examTypeId) {
		this.examTypeId = examTypeId;
	}

	public String[] getSchoolClasses() {
		return schoolClasses;
	}
	public void setSchoolClasses(String[] schoolClasses) {
		this.schoolClasses = schoolClasses;
	}
	public String[] getSubjects() {
		return subjects;
	}
	public void setSubjects(String[] subjects) {
		this.subjects = subjects;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String[] getExamTypeIds() {
		return examTypeIds;
	}

	public void setExamTypeIds(String[] examTypeIds) {
		this.examTypeIds = examTypeIds;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public int getWeekdayId() {
		return weekdayId;
	}

	public void setWeekdayId(int weekdayId) {
		this.weekdayId = weekdayId;
	}

	public int getPeriodId() {
		return periodId;
	}

	public void setPeriodId(int periodId) {
		this.periodId = periodId;
	}
}
