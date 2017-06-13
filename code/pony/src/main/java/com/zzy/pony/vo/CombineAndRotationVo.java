package com.zzy.pony.vo;

import java.util.List;



public class CombineAndRotationVo {
	private Integer combineId;
	private Integer rotationId;
	private Integer termId;	
	private String termName;
	private Integer yearId;
	private String yearName;
	private List<Integer> tsIds;
	private List<String> tsNames;
	
	
	
	public CombineAndRotationVo(){}



	public Integer getCombineId() {
		return combineId;
	}



	public void setCombineId(Integer combineId) {
		this.combineId = combineId;
	}
	


	public Integer getRotationId() {
		return rotationId;
	}



	public void setRotationId(Integer rotationId) {
		this.rotationId = rotationId;
	}



	public Integer getTermId() {
		return termId;
	}



	public void setTermId(Integer termId) {
		this.termId = termId;
	}



	public String getTermName() {
		return termName;
	}



	public void setTermName(String termName) {
		this.termName = termName;
	}



	public Integer getYearId() {
		return yearId;
	}



	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}



	public String getYearName() {
		return yearName;
	}



	public void setYearName(String yearName) {
		this.yearName = yearName;
	}



	public List<Integer> getTsIds() {
		return tsIds;
	}



	public void setTsIds(List<Integer> tsIds) {
		this.tsIds = tsIds;
	}



	public List<String> getTsNames() {
		return tsNames;
	}



	public void setTsNames(List<String> tsNames) {
		this.tsNames = tsNames;
	}



	
		
	
	

	
}
