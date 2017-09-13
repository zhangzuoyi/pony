package com.zzy.pony.evaluation.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the evaluation_item database table.
 * 
 */
@Entity
@Table(name="evaluation_item")
@NamedQuery(name="EvaluationItem.findAll", query="SELECT e FROM EvaluationItem e")
public class EvaluationItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TYPE_DIR="D";//目录
	public static final String TYPE_LEAF="L";//叶子节点
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ITEM_ID")
	private Long itemId;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="LEVEL")
	private Integer level;

	@Column(name="NAME")
	private String name;

	@Column(name="PARENT_ITEM_ID")
	private Long parentItemId;

	@Column(name="SCORE")
	private float score;

	@Column(name="SEQ")
	private Integer seq;

	@Column(name="TYPE")
	private String type;//目录，叶子节点

	@Column(name="DATA_SOURCE")
	private String dataSource;//数据来源
	//bi-directional many-to-one association to EvaluationSubject
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private EvaluationSubject subject;

	//bi-directional many-to-one association to EvaluationItemData
	@OneToMany(mappedBy="item")
	private List<EvaluationItemData> itemData;

	public EvaluationItem() {
	}

	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentItemId() {
		return this.parentItemId;
	}

	public void setParentItemId(Long parentItemId) {
		this.parentItemId = parentItemId;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public EvaluationSubject getSubject() {
		return this.subject;
	}

	public void setSubject(EvaluationSubject subject) {
		this.subject = subject;
	}

	public List<EvaluationItemData> getItemData() {
		return this.itemData;
	}

	public void setItemData(List<EvaluationItemData> itemData) {
		this.itemData = itemData;
	}

	public EvaluationItemData addItemData(EvaluationItemData itemData) {
		getItemData().add(itemData);
		itemData.setItem(this);

		return itemData;
	}

	public EvaluationItemData removeItemData(EvaluationItemData itemData) {
		getItemData().remove(itemData);
		itemData.setItem(null);

		return itemData;
	}

}