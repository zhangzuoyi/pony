package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamResult;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;


public interface ExamResultDao extends JpaRepository<ExamResult,Long>{
	List<ExamResult> findByExamAndStudentIn(Exam exam, List<Student> students);
	ExamResult findByExamAndSubjectAndStudent(Exam exam, Subject subject, Student student);
	List<ExamResult> findByExamAndSubject(Exam exam,Subject subject);

}
