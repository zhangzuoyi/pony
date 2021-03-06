package com.zzy.pony.exam.vo;

import java.util.Date;
import java.util.List;

public class ExamArrangeVo {
	private int arrangeId;
	private String endTime;
	private Date examDate;
	private String startTime;
	private String groupName;
	private int groupId;
	private int examId;
	private String examName;
	private int gradeId;
	private String gradeName;
	private int subjectId;
	private String subjectName;
	private List<Integer> examRoomIds;
	private List<String> examRoomNames;
	private List<Integer> examineeIds;
	private List<String> examineeNames;
	private int examineeTotal;//考生人数
	public ExamArrangeVo(){}
	public int getArrangeId() {
		return arrangeId;
	}
	public void setArrangeId(int arrangeId) {
		this.arrangeId = arrangeId;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
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
	public List<Integer> getExamRoomIds() {
		return examRoomIds;
	}
	public void setExamRoomIds(List<Integer> examRoomIds) {
		this.examRoomIds = examRoomIds;
	}
	public List<String> getExamRoomNames() {
		return examRoomNames;
	}
	public void setExamRoomNames(List<String> examRoomNames) {
		this.examRoomNames = examRoomNames;
	}
	public List<Integer> getExamineeIds() {
		return examineeIds;
	}
	public void setExamineeIds(List<Integer> examineeIds) {
		this.examineeIds = examineeIds;
	}
	public List<String> getExamineeNames() {
		return examineeNames;
	}
	public void setExamineeNames(List<String> examineeNames) {
		this.examineeNames = examineeNames;
	}
	public int getExamineeTotal() {
		return examineeTotal;
	}
	public void setExamineeTotal(int examineeTotal) {
		this.examineeTotal = examineeTotal;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	
	
	
	
	
	
	
	
	
}
