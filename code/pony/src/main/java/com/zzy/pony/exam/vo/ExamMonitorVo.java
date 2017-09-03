package com.zzy.pony.exam.vo;

import java.util.ArrayList;
import java.util.List;

public class ExamMonitorVo {
	private int id;
	private Integer monitorCount;
	private Integer examId;
	private Integer teacherId;
	private String teacherNo;
	private String teacherName;
	private String subjectName;
	
	//自动安排考场时的属性
	private int countLeft;//剩余的监考次数
	private List<String> timeAllocated;//已安排的时段，日期(yyyy-MM-dd)+空格+开始时间(HH:mm)
	
	public void init(){
		if(monitorCount != null){
			countLeft=monitorCount;
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getMonitorCount() {
		return monitorCount;
	}
	public void setMonitorCount(Integer monitorCount) {
		this.monitorCount = monitorCount;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherNo() {
		return teacherNo;
	}
	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getCountLeft() {
		return countLeft;
	}
	public void setCountLeft(int countLeft) {
		this.countLeft = countLeft;
	}
	public List<String> getTimeAllocated() {
		if(timeAllocated == null){
			timeAllocated=new ArrayList<String>();
		}
		return timeAllocated;
	}
	public void setTimeAllocated(List<String> timeAllocated) {
		this.timeAllocated = timeAllocated;
	}
	
}
