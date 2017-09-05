package com.zzy.pony.vo;

public class LessonSelectTimeVo {
	private Integer arrangeId;
	private Integer timeId;

	private String weekday;
	
	private Integer periodId;
	private Integer periodSeq;
	public Integer getTimeId() {
		return timeId;
	}
	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}

	public String getWeekday() {
		return weekday;
	}
	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	public Integer getPeriodId() {
		return periodId;
	}
	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}
	public Integer getPeriodSeq() {
		return periodSeq;
	}
	public void setPeriodSeq(Integer periodSeq) {
		this.periodSeq = periodSeq;
	}
	public Integer getArrangeId() {
		return arrangeId;
	}
	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}
	
	
}
