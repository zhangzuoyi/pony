package com.zzy.pony.message.vo;

import java.util.Date;
import java.util.List;

import com.zzy.pony.message.model.MessageAttach;

public class MessageVo {
   private int[] userGroup;
   private int[] users;
   private String title;
   private String content;
   private String sendTime;
   private String sendUser;
   private String[] attachs; 
   private String readStatus;
   private String messageId;
   private String messageReceiveInfoId; 
   private String receiveUser;
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


public String[] getAttachs() {
	return attachs;
}
public void setAttachs(String[] attachs) {
	this.attachs = attachs;
}
public String getReadStatus() {
	return readStatus;
}
public void setReadStatus(String readStatus) {
	this.readStatus = readStatus;
}
public String getMessageId() {
	return messageId;
}
public void setMessageId(String messageId) {
	this.messageId = messageId;
}
public String getMessageReceiveInfoId() {
	return messageReceiveInfoId;
}
public void setMessageReceiveInfoId(String messageReceiveInfoId) {
	this.messageReceiveInfoId = messageReceiveInfoId;
}
public String getReceiveUser() {
	return receiveUser;
}
public void setReceiveUser(String receiveUser) {
	this.receiveUser = receiveUser;
}




   
	
	
}
