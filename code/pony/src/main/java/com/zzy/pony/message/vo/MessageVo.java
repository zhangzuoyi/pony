package com.zzy.pony.message.vo;

import java.util.Date;

public class MessageVo {
   private int[] userGroup;
   private int[] users;
   private String title;
   private String content;
   private String sendTime;
   private String sendUser;
   private String[] attach; 
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
public String getSendTime() {
	return sendTime;
}
public void setSendTime(String sendTime) {
	this.sendTime = sendTime;
}
public String getSendUser() {
	return sendUser;
}
public void setSendUser(String sendUser) {
	this.sendUser = sendUser;
}
public String[] getAttach() {
	return attach;
}
public void setAttach(String[] attach) {
	this.attach = attach;
}

   
	
	
}
