package com.zzy.pony.vo;



public class SubjectVo {
	private Integer subjectId;	
	private Integer type;
	private String name;
	private Integer importance;
	private String importanceName;

	public SubjectVo(){}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public String getImportanceName() {
		return importanceName;
	}

	public void setImportanceName(String importanceName) {
		this.importanceName = importanceName;
	}
	
	
	

	
}
