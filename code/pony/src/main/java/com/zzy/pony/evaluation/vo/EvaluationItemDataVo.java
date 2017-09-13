package com.zzy.pony.evaluation.vo;

import java.io.Serializable;

public class EvaluationItemDataVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private float checkScore;

	private float inputScore;
	
	private String according;
	
	private Long itemId;
	private String itemName;
	private float itemScore;

	public EvaluationItemDataVo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getCheckScore() {
		return checkScore;
	}

	public void setCheckScore(float checkScore) {
		this.checkScore = checkScore;
	}

	public float getInputScore() {
		return inputScore;
	}

	public void setInputScore(float inputScore) {
		this.inputScore = inputScore;
	}

	public String getAccording() {
		return according;
	}

	public void setAccording(String according) {
		this.according = according;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public float getItemScore() {
		return itemScore;
	}

	public void setItemScore(float itemScore) {
		this.itemScore = itemScore;
	}

	
}