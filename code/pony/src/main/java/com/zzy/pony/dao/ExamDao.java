package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;


public interface ExamDao extends JpaRepository<Exam,Integer>{
	List<Exam> findBySubject(Subject subject);
	List<Exam> findBySchoolYearAndTermAndSubjectAndSchoolClasses(SchoolYear schoolYear,Term term,Subject subject,SchoolClass schoolClass);
}
