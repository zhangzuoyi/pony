package com.zzy.pony.exam.service;

import java.util.List;

import com.zzy.pony.exam.vo.ExamMonitorVo;

public interface ExamMonitorService {
	void add(int examId,int gradeId,int[] teacherIds);
	void add(int examId,int gradeId,List<ExamMonitorVo> list);
	void setCount(int examId,int gradeId , int[] teacherIds, int count);
	void delete(int[] ids);
	/**
	 * 监考老师分配
	 * @param examId
	 */
	void monitorArrange(int examId, int gradeId);
}
