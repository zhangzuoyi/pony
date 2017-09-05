package com.zzy.pony.message.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




import com.zzy.pony.model.User;


/**
 * The persistent class for the t_message database table.
 * 
 */
@Entity
@Table(name="t_message")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MSG_ID")
	private int msgId;

	@Lob
	private String content;

	@Column(name="IS_VALID")
	private int isValid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SEND_TIME")
	private Date sendTime;

	private String title;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;

	//bi-directional many-to-one association to MessageAttach
	
	@OneToMany(mappedBy="message",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<MessageAttach> messageAttaches;

	//bi-directional many-to-one association to MessageReceiveInfo
	@OneToMany(mappedBy="message",cascade=CascadeType.ALL)
	private List<MessageReceiveInfo> messageReceiveInfos;

	//bi-directional many-to-one association to MessageReceiver
	@OneToMany(mappedBy="message",cascade=CascadeType.ALL)
	private List<MessageReceiver> messageReceivers;

	public Message() {
	}

	public int getMsgId() {
		return this.msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsValid() {
		return this.isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<MessageAttach> getMessageAttaches() {
		return this.messageAttaches;
	}

	public void setMessageAttaches(List<MessageAttach> messageAttaches) {
		this.messageAttaches = messageAttaches;
	}

	public MessageAttach addMessageAttach(MessageAttach messageAttach) {
		getMessageAttaches().add(messageAttach);
		messageAttach.setMessage(this);

		return messageAttach;
	}

	public MessageAttach removeMessageAttach(MessageAttach messageAttach) {
		getMessageAttaches().remove(messageAttach);
		messageAttach.setMessage(null);

		return messageAttach;
	}

	public List<MessageReceiveInfo> getMessageReceiveInfos() {
		return this.messageReceiveInfos;
	}

	public void setMessageReceiveInfos(List<MessageReceiveInfo> messageReceiveInfos) {
		this.messageReceiveInfos = messageReceiveInfos;
	}

	public MessageReceiveInfo addMessageReceiveInfo(MessageReceiveInfo messageReceiveInfo) {
		getMessageReceiveInfos().add(messageReceiveInfo);
		messageReceiveInfo.setMessage(this);

		return messageReceiveInfo;
	}

	public MessageReceiveInfo removeMessageReceiveInfo(MessageReceiveInfo messageReceiveInfo) {
		getMessageReceiveInfos().remove(messageReceiveInfo);
		messageReceiveInfo.setMessage(null);

		return messageReceiveInfo;
	}

	public List<MessageReceiver> getMessageReceivers() {
		return this.messageReceivers;
	}

	public void setMessageReceivers(List<MessageReceiver> messageReceivers) {
		this.messageReceivers = messageReceivers;
	}

	public MessageReceiver addMessageReceiver(MessageReceiver messageReceiver) {
		getMessageReceivers().add(messageReceiver);
		messageReceiver.setMessage(this);

		return messageReceiver;
	}

	public MessageReceiver removeMessageReceiver(MessageReceiver messageReceiver) {
		getMessageReceivers().remove(messageReceiver);
		messageReceiver.setMessage(null);

		return messageReceiver;
	}

}