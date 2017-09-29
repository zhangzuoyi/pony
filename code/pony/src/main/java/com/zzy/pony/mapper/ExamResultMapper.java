package com.zzy.pony.mapper;

import java.util.List;
import java.util.Map;

import com.zzy.pony.vo.ExamResultVo;

public interface ExamResultMapper {
	List<ExamResultVo> find(Integer examId, Integer classId, Integer subjectId);
	List<ExamResultVo> findByStudent(Integer studentId);
	List<Integer> findSubjectByExam(int examId); 
	List<ExamResultVo> findExamReuslt(Integer examId, Integer studentId, Integer subjectId);
	List<Map<String, Object>> findTotalScoreByExam(int examId);
	void deleteByExam(int examId);
	void insert(Integer examId,Integer studentId,Float score, Integer subjectId, String loginName);
}
