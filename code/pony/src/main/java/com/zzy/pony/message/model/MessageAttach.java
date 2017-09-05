package com.zzy.pony.message.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_message_attach database table.
 * 
 */
@Entity
@Table(name="t_message_attach")
@NamedQuery(name="MessageAttach.findAll", query="SELECT m FROM MessageAttach m")
public class MessageAttach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ATTACH_ID")
	private int attachId;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="ORIGINAL_NAME")
	private String originalName;

	@Column(name="SAVE_PATH")
	private String savePath;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="MSG_ID")
	private Message message;

	public MessageAttach() {
	}

	public int getAttachId() {
		return this.attachId;
	}

	public void setAttachId(int attachId) {
		this.attachId = attachId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalName() {
		return this.originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getSavePath() {
		return this.savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}