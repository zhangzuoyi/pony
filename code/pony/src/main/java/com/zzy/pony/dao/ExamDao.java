package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;


public interface ExamDao extends JpaRepository<Exam,Integer>{
//	List<Exam> findBySubject(Subject subject);
//	List<Exam> findBySchoolYearAndTermAndSubjectAndSchoolClasses(SchoolYear schoolYear,Term term,Subject subject,SchoolClass schoolClass);
	List<Exam> findBySchoolYearAndTerm(SchoolYear schoolYear,Term term);
	List<Exam> findBySchoolClasses(List<SchoolClass> schoolClasses);
	List<Exam> findBySchoolYearAndTerm(SchoolYear schoolYear,Term term,Sort sort);

}
