package com.zzy.pony.ss.vo;

public class StudentSubjectStatisticsVo implements Comparable<StudentSubjectStatisticsVo> {
	
	private String group;//组合形式 
	private int count;//组合人数
	public StudentSubjectStatisticsVo() {}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public int compareTo(StudentSubjectStatisticsVo o) {
		// TODO Auto-generated method stub				
		return o.getCount() - this.getCount();
	}
	
	
	
	
}
