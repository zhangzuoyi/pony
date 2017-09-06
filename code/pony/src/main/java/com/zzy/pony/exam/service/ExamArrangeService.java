package com.zzy.pony.exam.service;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamArrangeGroup;

public interface ExamArrangeService {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId);
	List<ExamArrange> findByExamAndGrade(int examId,int gradeId);
	Page<ExamArrange> findByExam(Pageable pageable,int examId);
	Page<ExamArrange> findByGrade(Pageable pageable,int gradeId);
	Page<ExamArrange> findAll(Pageable pageable);
	void add(int[] subjects,int  examId,int gradeId);
	void addExamDate(int[] examArranges,Date examDate);
	void addExamTime(int[] examArranges,Date startTime,Date endTime);
	void addGroup(int[] examArranges,String groupName,String gradeId,String examId);
	void delete(int arrangeId);
	ExamArrange findByExamAndGradeAndSubject(int examId,int gradeId,int subjectId);
	List<ExamArrange> findByExam(int examId);
	List<ExamArrange> findByExamAndGroupIsNull(int examId);
	List<ExamArrangeGroup> findByExamAndGroup(int examId,int gradeId);
	ExamArrange get(int arrangeId);
}
