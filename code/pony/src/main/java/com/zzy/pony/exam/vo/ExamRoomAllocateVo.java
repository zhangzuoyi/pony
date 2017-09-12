package com.zzy.pony.exam.vo;

import java.util.Date;

import com.zzy.pony.util.DateTimeUtil;

public class ExamRoomAllocateVo {
	private int roomId;
	private String roomName;
	private Integer roomSeq;
	private Date examDate;
	private Date startTime;
	private Date endTime;
	private Integer gradeId;
	private String gradeName;
	private Integer subjectId;
	private String subjectName;
	private Integer teacherId;
	private String teacherName;
	private String teacherNo;
	private int capacity;
	/**
	 * 时间段，日期(yyyy-MM-dd)+空格+开始时间(HH:mm)
	 * @return
	 */
	public String getTimeAllocated(){
		String result="";
		if(examDate != null){
			result =DateTimeUtil.dateToStr(examDate, "yyyy-MM-dd");
		}
		result +=" ";
		if(startTime != null){
			result +=DateTimeUtil.dateToStr(startTime, "HH:mm");
		}
		return result;
	}
	public boolean isFit(String room,String examDate,String startTime){
		if(getTimeAllocated().equals(examDate+" "+startTime) && roomName.equals(room)){
			return true;
		}
		return false;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public Integer getRoomSeq() {
		return roomSeq;
	}
	public void setRoomSeq(Integer roomSeq) {
		this.roomSeq = roomSeq;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getGradeId() {
		return gradeId;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherNo() {
		return teacherNo;
	}
	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
