package com.zzy.pony.vo;

import java.util.List;
import java.util.Map;

public class GsonVo {
	private int total;
	private List<Map<String, Object>> rows;
	private List<Map<String, Object>> title;
	public  GsonVo(){}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<Map<String, Object>> getRows() {
		return rows;
	}
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}
	public List<Map<String, Object>> getTitle() {
		return title;
	}
	public void setTitle(List<Map<String, Object>> title) {
		this.title = title;
	}
	
	
	
}
