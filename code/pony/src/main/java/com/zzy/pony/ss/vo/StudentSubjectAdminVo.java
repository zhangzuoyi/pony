package com.zzy.pony.ss.vo;

public class StudentSubjectAdminVo {

	private int studentId;
	private String studentName;
	private String group;
	private String[] selectSubjects;
	
	public StudentSubjectAdminVo() {}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String[] getSelectSubjects() {
		return selectSubjects;
	}

	public void setSelectSubjects(String[] selectSubjects) {
		this.selectSubjects = selectSubjects;
	}
	
	
	
	
}
