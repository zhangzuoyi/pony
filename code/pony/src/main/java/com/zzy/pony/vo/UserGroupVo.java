package com.zzy.pony.vo;

import java.util.List;



public class UserGroupVo {
	private String groupType;
	private String groupName;
	private String groupId;
	private List<String> studentGroup;
	private List<String> teacherGroup;
	
	
	
	private String userName;
	private String userId;
	
	
	
	public UserGroupVo(){}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public List<String> getStudentGroup() {
		return studentGroup;
	}
	public void setStudentGroup(List<String> studentGroup) {
		this.studentGroup = studentGroup;
	}
	public List<String> getTeacherGroup() {
		return teacherGroup;
	}
	public void setTeacherGroup(List<String> teacherGroup) {
		this.teacherGroup = teacherGroup;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	 
	

	
}
