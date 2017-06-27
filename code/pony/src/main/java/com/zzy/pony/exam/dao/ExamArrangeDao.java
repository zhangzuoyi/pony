package com.zzy.pony.exam.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;

public interface ExamArrangeDao extends JpaRepository<ExamArrange, Integer> {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,Exam exam,Grade grade);
}
