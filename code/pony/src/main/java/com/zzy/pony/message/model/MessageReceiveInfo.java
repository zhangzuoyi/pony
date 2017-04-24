package com.zzy.pony.message.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.zzy.pony.model.User;


/**
 * The persistent class for the t_message_receive_info database table.
 * 
 */
@Entity
@Table(name="t_message_receive_info")
@NamedQuery(name="MessageReceiveInfo.findAll", query="SELECT m FROM MessageReceiveInfo m")
public class MessageReceiveInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="IS_VALID")
	private int isValid;

	@Column(name="READ_STATUS")
	private int readStatus;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="MSG_ID")
	private Message message;

	public MessageReceiveInfo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsValid() {
		return this.isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getReadStatus() {
		return this.readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}