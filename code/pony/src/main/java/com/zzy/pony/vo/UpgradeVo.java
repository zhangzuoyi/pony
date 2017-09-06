package com.zzy.pony.vo;

import java.util.List;

import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;

public class UpgradeVo {
	private Grade srcGrade;
	private Grade targetGrade;
	private List<SchoolClass> srcClasses;
	private List<SchoolClass> targetClasses;
	public Grade getSrcGrade() {
		return srcGrade;
	}
	public void setSrcGrade(Grade srcGrade) {
		this.srcGrade = srcGrade;
	}
	public Grade getTargetGrade() {
		return targetGrade;
	}
	public void setTargetGrade(Grade targetGrade) {
		this.targetGrade = targetGrade;
	}
	public List<SchoolClass> getSrcClasses() {
		return srcClasses;
	}
	public void setSrcClasses(List<SchoolClass> srcClasses) {
		this.srcClasses = srcClasses;
	}
	public List<SchoolClass> getTargetClasses() {
		return targetClasses;
	}
	public void setTargetClasses(List<SchoolClass> targetClasses) {
		this.targetClasses = targetClasses;
	}
	
	
}
