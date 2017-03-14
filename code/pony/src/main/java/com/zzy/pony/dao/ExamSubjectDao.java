package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamSubject;


public interface ExamSubjectDao extends JpaRepository<ExamSubject,Integer>{
	List<ExamSubject> findByExamIn(List<Exam> exams); 

}
