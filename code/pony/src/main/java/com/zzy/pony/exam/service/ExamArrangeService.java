package com.zzy.pony.exam.service;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;

public interface ExamArrangeService {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId);
	Page<ExamArrange> findByExam(Pageable pageable,int examId);
	Page<ExamArrange> findByGrade(Pageable pageable,int gradeId);
	Page<ExamArrange> findAll(Pageable pageable);
	void add(int[] subjects);
	void addExamDate(int[] examArranges,Date examDate);
	void addExamTime(int[] examArranges,Date startTime,Date endTime);
	void addGroup(int[] examArranges,String groupName,Grade grade,Exam exam);
	void delete(int arrangeId);
	
}
