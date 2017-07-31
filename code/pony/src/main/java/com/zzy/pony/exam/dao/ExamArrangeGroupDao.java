package com.zzy.pony.exam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.ExamArrangeGroup;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;

public interface ExamArrangeGroupDao extends JpaRepository<ExamArrangeGroup, Integer> {
	
	List<ExamArrangeGroup> findByExamAndGrade(Exam exam,Grade grade);

}
