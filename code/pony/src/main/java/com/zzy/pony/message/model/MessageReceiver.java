package com.zzy.pony.message.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_message_receiver database table.
 * 
 */
@Entity
@Table(name="t_message_receiver")
@NamedQuery(name="MessageReceiver.findAll", query="SELECT m FROM MessageReceiver m")
public class MessageReceiver implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(name="RECEIVER_ID")
	private int receiverId;

	@Column(name="RECEIVER_TYPE")
	private String receiverType;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="MSG_ID")
	private Message message;

	public MessageReceiver() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverType() {
		return this.receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}