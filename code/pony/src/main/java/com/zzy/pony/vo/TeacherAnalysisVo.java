package com.zzy.pony.vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeacherAnalysisVo {
	
	private int teacherId;
	private String teacherName;
	private int subjectId;
	private String subjectName;
	
	
	/**
	 * 教的班级
	 */
	private Set<Integer> classIds;
	/*
	 * 上下午整体的课程 
	 */
	private List<Integer> periodAm;
	private List<Integer> periodPm;
	
	/**
	 * key:周几  value:课程 seq
	 */
	private Map<Integer, List<Integer>> weekPeriod;
	
	/**
	 * key:周几  value:班级 classId
	 */
	private Map<Integer, Set<Integer>> weekClass;
	
	
	/**
	 * 下午课程与班级比例(语数英为1,其他为2)
	 */
	private int pmRatio;
	
	/**
	 * 是否平齐 
	 */
	private boolean PQ;
	
	
	public TeacherAnalysisVo() {}
	
	public TeacherAnalysisVo(boolean PQ) {
		this.PQ = PQ;
	}


	public int getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}


	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
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


	public Set<Integer> getClassIds() {
		return classIds;
	}


	public void setClassIds(Set<Integer> classIds) {
		this.classIds = classIds;
	}


	public List<Integer> getPeriodAm() {
		return periodAm;
	}


	public void setPeriodAm(List<Integer> periodAm) {
		this.periodAm = periodAm;
	}


	public List<Integer> getPeriodPm() {
		return periodPm;
	}


	public void setPeriodPm(List<Integer> periodPm) {
		this.periodPm = periodPm;
	}


	public int getPmRatio() {
		return pmRatio;
	}


	public void setPmRatio(int pmRatio) {
		this.pmRatio = pmRatio;
	}


	public Map<Integer, List<Integer>> getWeekPeriod() {
		return weekPeriod;
	}


	public void setWeekPeriod(Map<Integer, List<Integer>> weekPeriod) {
		this.weekPeriod = weekPeriod;
	}


	public Map<Integer, Set<Integer>> getWeekClass() {
		return weekClass;
	}


	public void setWeekClass(Map<Integer, Set<Integer>> weekClass) {
		this.weekClass = weekClass;
	}


	public boolean isPQ() {
		return PQ;
	}


	public void setPQ(boolean pQ) {
		PQ = pQ;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
