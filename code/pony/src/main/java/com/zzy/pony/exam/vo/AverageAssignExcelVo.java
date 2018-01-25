package com.zzy.pony.exam.vo;

import java.math.BigDecimal;
import java.util.Map;

public class AverageAssignExcelVo {
	private String uniqueId;
	private String schoolName;
	private String classCode;
	private String name;
	private String sex;
	private Map<String, BigDecimal> initScore;
	private Map<String, BigDecimal> assignScore;
	private float totalScore;
	private float assignTotalScore;

	private String examineeNo;//考生号
	private String studentNo;//学号
	
	
	public AverageAssignExcelVo() {}
	
	


	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}




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


	public Map<String, BigDecimal> getInitScore() {
		return initScore;
	}


	public void setInitScore(Map<String, BigDecimal> initScore) {
		this.initScore = initScore;
	}


	public Map<String, BigDecimal> getAssignScore() {
		return assignScore;
	}


	public void setAssignScore(Map<String, BigDecimal> assignScore) {
		this.assignScore = assignScore;
	}


	public float getTotalScore() {
		return totalScore;
	}


	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}


	public float getAssignTotalScore() {
		return assignTotalScore;
	}


	public void setAssignTotalScore(float assignTotalScore) {
		this.assignTotalScore = assignTotalScore;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getExamineeNo() {
		return examineeNo;
	}

	public void setExamineeNo(String examineeNo) {
		this.examineeNo = examineeNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
}
