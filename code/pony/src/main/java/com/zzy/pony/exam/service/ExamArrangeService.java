package com.zzy.pony.exam.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamArrange;

public interface ExamArrangeService {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId);
	Page<ExamArrange> findByExam(Pageable pageable,int examId);
	Page<ExamArrange> findByGrade(Pageable pageable,int gradeId);
	Page<ExamArrange> findAll(Pageable pageable);
	void add(int[] subjects);
}
