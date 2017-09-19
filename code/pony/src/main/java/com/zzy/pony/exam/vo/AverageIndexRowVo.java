package com.zzy.pony.exam.vo;

import java.util.ArrayList;
import java.util.List;

import com.zzy.pony.exam.model.AverageIndex;

/**
 * 均量值指标展示成表格行
 * @author ZHANGZUOYI499
 *
 */
public class AverageIndexRowVo {
	private int examId;
	private int gradeId;
	private String section;
	private List<AverageIndex> indexList;//按科目顺序存放
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public List<AverageIndex> getIndexList() {
		if(indexList == null) {
			indexList=new ArrayList<AverageIndex>();
		}
		return indexList;
	}
	public void setIndexList(List<AverageIndex> indexList) {
		this.indexList = indexList;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	
	
}
