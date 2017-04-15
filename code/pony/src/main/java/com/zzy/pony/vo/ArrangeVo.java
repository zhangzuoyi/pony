package com.zzy.pony.vo;



public class ArrangeVo {
	private Integer arrangeId;	
	private Integer classId;
	private String otherLesson;
	private String weekDay;
	private String sourceType;//来源类型 0 预排，1 自动，3 调课
	private Integer periodId;
	private String period;
	private String endTime;
	private String startTime;
	private Integer yearId;
	private Integer termId;
	private Integer subjectId;
	private String subjectName;
	
	private Integer teacherId;
	private Integer weekdayId;
	private Integer seqId; 
	
	
	public ArrangeVo(){}
	public Integer getArrangeId() {
		return arrangeId;
	}
	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getOtherLesson() {
		return otherLesson;
	}
	public void setOtherLesson(String otherLesson) {
		this.otherLesson = otherLesson;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getPeriodId() {
		return periodId;
	}
	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Integer getYearId() {
		return yearId;
	}
	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}
	public Integer getTermId() {
		return termId;
	}
	public void setTermId(Integer termId) {
		this.termId = termId;
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
	public Integer getWeekdayId() {
		return weekdayId;
	}
	public void setWeekdayId(Integer weekdayId) {
		this.weekdayId = weekdayId;
	}
	public Integer getSeqId() {
		return seqId;
	}
	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}	
	
	

	
}
