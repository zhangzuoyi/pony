package com.zzy.pony.evaluation.vo;

import java.io.Serializable;

/**
 * 用于动态生成评价表格，仅假定第一列分类可能存在的情况
 * @author ZHANGZUOYI499
 *
 */
public class EvaluationRowVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String category;
	private int categoryRowspan;//分类的行跨度
	private Long itemId;

	private String description;

	private String name;

	private float score;

	private int colspan;//列跨度，若没有分类就跨两列
	
	public EvaluationRowVo() {
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryRowspan() {
		return categoryRowspan;
	}

	public void setCategoryRowspan(int categoryRowspan) {
		this.categoryRowspan = categoryRowspan;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	
}