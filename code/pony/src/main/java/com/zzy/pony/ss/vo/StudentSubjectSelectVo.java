package com.zzy.pony.ss.vo;

import java.util.Date;

public class StudentSubjectSelectVo {
	
	private int id;
	private int configId;
	private int studentId;
	private String subject;
	private Date createTime;
	
	public StudentSubjectSelectVo() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	

}
