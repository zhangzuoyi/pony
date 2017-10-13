package com.zzy.pony.exam.mapper;


import java.util.List;
import java.util.Map;

import com.zzy.pony.exam.vo.ExamArrangeVo;

public interface ExamArrangeMapper {
	List<Map<String, String>> timeList(Integer examId, int gradeId);
	void  addExamArrange(int examId,int gradeId,int subjectId);
	void  updateExamDate(Map<String,Object> map );
	void  updateExamTime(Map<String,Object> map );
	ExamArrangeVo findVoByExamAndGradeAndSubject(int examId,int gradeId,int subjectId);
	List<ExamArrangeVo> findByExamAndGrade(int examId,int gradeId);	
	List<ExamArrangeVo> findByExamAndGradeAndGroupIsNull(int examId,int gradeId);	

	


}
