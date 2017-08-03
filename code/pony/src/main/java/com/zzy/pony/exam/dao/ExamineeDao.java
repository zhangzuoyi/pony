package com.zzy.pony.exam.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Student;



public interface ExamineeDao extends JpaRepository<Examinee, Integer> {
	
	List<Examinee> findByExamAndStudent(Exam exam,Student student);
}
