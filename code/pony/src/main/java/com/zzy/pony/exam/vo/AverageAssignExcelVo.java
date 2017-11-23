package com.zzy.pony.exam.vo;

import java.util.Map;

public class AverageAssignExcelVo {
	private String schoolName;
	private String classCode;
	private String name;
	private Map<String, Float> initScore;
	private Map<String, Float> assignScore;
	
	
	public AverageAssignExcelVo() {}


	public String getSchoolName() {
		return schoolName;
	}


	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}


	public String getClassCode() {
		return classCode;
	}


	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Map<String, Float> getInitScore() {
		return initScore;
	}


	public void setInitScore(Map<String, Float> initScore) {
		this.initScore = initScore;
	}


	public Map<String, Float> getAssignScore() {
		return assignScore;
	}


	public void setAssignScore(Map<String, Float> assignScore) {
		this.assignScore = assignScore;
	}
	
	
	
	
	
}
