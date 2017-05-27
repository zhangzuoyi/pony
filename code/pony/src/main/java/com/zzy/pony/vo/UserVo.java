package com.zzy.pony.vo;

import java.util.Date;

import com.zzy.pony.config.Constants;

public class UserVo {
	private int userId;

	private Date lastLoginTime;

	private String loginName;
	private String psw;

	private String userType;
	private Integer teacherId;
	private String teacher;
	private Integer studentId;
	private String student;
	
	private String[] roles;
	
	public String getUserTypeName(){
		return Constants.USER_TYPES.get(userType);
	}
	
	public String getShowName(){
		if(Constants.USER_TYPE_TEACHER.equals(userType)){
			return teacher;
		}else if(Constants.USER_TYPE_STUDENT.equals(userType)){
			return student;
		}
		return null;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	
	
	
}
