package com.zzy.pony.exam.service;

public interface ExamMonitorService {
	void add(int examId,int gradeId,int[] teacherIds);
	void setCount(int examId,int gradeId , int[] teacherIds, int count);
	void delete(int[] ids);
	/**
	 * 监考老师分配
	 * @param examId
	 */
	void monitorArrange(int examId, int gradeId);
}
