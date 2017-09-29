package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ExamResultVo;

public interface ExamResultService {
	List<ExamResultVo> findByClass(Integer examId, Integer classId, Integer subjectId);
	List<ExamResultVo> findByStudent(Integer studentId);
	void save(List<ExamResultVo> vos);
	void batchSave(Map<Student, Float> scores, Integer examId, Integer subjectId, String loginName);
	List<Subject>  findSubjectByExam(int examId);
	/**
	 * 上传一次考试的所有结果
	 * @param examId
	 * @param resultList
	 * @param loginName
	 */
	void uploadAll(Integer examId,List<ExamResultVo> resultList,String loginName);
}
