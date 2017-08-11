package com.zzy.pony.vo;

import java.util.List;



public class SubjectNoCourseVo {
	private int id;
	private int gradeId;
	private String gradeName;
	private int yearId;
	private String yearName;
	private int termId;
	private String termName;
	private int weekdayId;
	private String weekdayName;
	private int lessonPeriodId;
	private String lessonPeriodName;
	private int lessonPeriodSeq;
	private int subjectId;
	private String subjectName;
	private List<Integer> gradeClassIds;
	public SubjectNoCourseVo(){}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getYearId() {
		return yearId;
	}
	public void setYearId(int yearId) {
		this.yearId = yearId;
	}
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public int getWeekdayId() {
		return weekdayId;
	}
	public void setWeekdayId(int weekdayId) {
		this.weekdayId = weekdayId;
	}
	public String getWeekdayName() {
		return weekdayName;
	}
	public void setWeekdayName(String weekdayName) {
		this.weekdayName = weekdayName;
	}
	public int getLessonPeriodId() {
		return lessonPeriodId;
	}
	public void setLessonPeriodId(int lessonPeriodId) {
		this.lessonPeriodId = lessonPeriodId;
	}
	public String getLessonPeriodName() {
		return lessonPeriodName;
	}
	public void setLessonPeriodName(String lessonPeriodName) {
		this.lessonPeriodName = lessonPeriodName;
	}
	public int getTermId() {
		return termId;
	}
	public void setTermId(int termId) {
		this.termId = termId;
	}
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public List<Integer> getGradeClassIds() {
		return gradeClassIds;
	}
	public void setGradeClassIds(List<Integer> gradeClassIds) {
		this.gradeClassIds = gradeClassIds;
	}
	public int getLessonPeriodSeq() {
		return lessonPeriodSeq;
	}
	public void setLessonPeriodSeq(int lessonPeriodSeq) {
		this.lessonPeriodSeq = lessonPeriodSeq;
	}
	
	
	
	
	
}