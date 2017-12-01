package com.zzy.pony.vo;

import java.util.Map;

public class ClassAnalysisVo {
	
	private int classId;
	
	/**
	 *  key: weekperiod  value:week   
	 */
	private Map<Integer, Integer> adjustTarget;
	
	public ClassAnalysisVo() {}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public Map<Integer, Integer> getAdjustTarget() {
		return adjustTarget;
	}

	public void setAdjustTarget(Map<Integer, Integer> adjustTarget) {
		this.adjustTarget = adjustTarget;
	}
	
	
	
	

}
