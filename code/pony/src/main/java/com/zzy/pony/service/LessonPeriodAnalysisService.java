package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.TeacherAnalysisVo;

public interface LessonPeriodAnalysisService {
	
	List<ArrangeVo> findAllLessonArrange(int yearId,int termId,int gradeId);
	
	
	/**
	 * @param arrangeVos
	 * @return 所有教师
	 */
	List<TeacherAnalysisVo> findAllTeacher(List<ArrangeVo> arrangeVos);
	
	/**
	 * @param teacherAnalysisVos
	 * 分析下午
	 */
	void analysisXW(List<TeacherAnalysisVo> teacherAnalysisVos);
	/**
	 * @param teacherAnalysisVos
	 * 分析上午
	 */
	void analysisSW(List<TeacherAnalysisVo> teacherAnalysisVos);
	
	/**
	 * @param teacherAnalysisVos
	 * 分析平齐
	 */
	void analysisPQ(List<TeacherAnalysisVo> teacherAnalysisVos);
	
	/**
	 * @param teacherAnalysisVos
	 * 调整平齐
	 */
	void adjustPQ(List<TeacherAnalysisVo> teacherAnalysisVos);

	
}
