package com.zzy.pony.exam.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamArrange;

public interface ExamArrangeService {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId);
}
