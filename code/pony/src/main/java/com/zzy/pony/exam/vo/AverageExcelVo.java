package com.zzy.pony.exam.vo;

public class AverageExcelVo {
	private String uniqueId;
	private String schoolName;
	private String classCode;
	private String name;
	private String subjectName;
	private float subjectResult;
	private float subjectResultSum;
	private float subjectResultAssign;//赋分
	private int rank;
	private int rankSum;
	private int level;
	public AverageExcelVo() {}
	
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
	
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public float getSubjectResult() {
		return subjectResult;
	}
	public void setSubjectResult(float subjectResult) {
		this.subjectResult = subjectResult;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public float getSubjectResultSum() {
		return subjectResultSum;
	}
	public void setSubjectResultSum(float subjectResultSum) {
		this.subjectResultSum = subjectResultSum;
	}
	public int getRankSum() {
		return rankSum;
	}
	public void setRankSum(int rankSum) {
		this.rankSum = rankSum;
	}
	public float getSubjectResultAssign() {
		return subjectResultAssign;
	}
	public void setSubjectResultAssign(float subjectResultAssign) {
		this.subjectResultAssign = subjectResultAssign;
	}
	
	
	
}
