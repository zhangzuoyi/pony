package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.model.Student;
import com.zzy.pony.vo.ExamResultVo;

public interface ExamResultService {
	List<ExamResultVo> findByClass(Integer examId, Integer classId);
	List<ExamResultVo> findByStudent(Integer studentId);
	void save(List<ExamResultVo> vos);
	void batchSave(Map<Student, Float> scores, Integer examId, String loginName);
}
