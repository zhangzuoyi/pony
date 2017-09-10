package com.zzy.pony.exam.mapper;

import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExamArrangeMapper {
	List<Map<String, String>> timeList(Integer examId);
	void  addExamArrange(int examId,int gradeId,int subjectId);
	void  updateExamDate(Map<String,Object> map );
	void  updateExamTime(Map<String,Object> map );


}
