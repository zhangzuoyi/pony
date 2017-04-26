package com.zzy.pony.message.vo;

public class MessageVo {
   private int[] userGroup;
   private int[] users;
   private String title;
   private String content;
   public MessageVo(){}
public int[] getUserGroup() {
	return userGroup;
}
public void setUserGroup(int[] userGroup) {
	this.userGroup = userGroup;
}
public int[] getUsers() {
	return users;
}
public void setUsers(int[] users) {
	this.users = users;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
   
	
	
}
