package com.zzy.pony.evaluation.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EvaluationItemVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long itemId;

	private String description;

	private Integer level;

	private String name;

	private Long parentItemId;
	private String parentItemName;

	private float score;

	private Integer seq;

	private String type;//目录，叶子节点

	private List<EvaluationItemVo> children;

	public EvaluationItemVo() {
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Long parentItemId) {
		this.parentItemId = parentItemId;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public List<EvaluationItemVo> getChildren() {
		if(children == null) {
			children=new ArrayList<EvaluationItemVo>();
		}
		return children;
	}

	public void setChildren(List<EvaluationItemVo> children) {
		this.children = children;
	}

	
}