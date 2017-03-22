package com.zzy.pony.vo;

public class conditionVo {
	private int yearId;
	private int termId;
	private int gradeId;
	private int examTypeId;
	private String[] schoolClasses;
	private String[] subjects;
	//private String schoolClasses;
	//private String subjects;
	public conditionVo(){
		
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
	/*public String getSchoolClasses() {
		return schoolClasses;
	}
	public void setSchoolClasses(String schoolClasses) {
		this.schoolClasses = schoolClasses;
	}
	public String getSubjects() {
		return subjects;
	}
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}*/
	
	
	

	
}
