package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;


public interface TeacherSubjectDao extends JpaRepository<TeacherSubject,Integer>{
	List<TeacherSubject> findByTeacherAndYearAndTerm(Teacher teacher,SchoolYear year,Term term);
	List<TeacherSubject> findBySchoolClassAndYearAndTerm(SchoolClass schoolClass,SchoolYear year,Term term);
	List<TeacherSubject> findByYearAndTerm(SchoolYear year,Term term);
	List<TeacherSubject> findByTeacherAndYearAndTermAndSchoolClassAndSubject(Teacher teacher,SchoolYear year,Term term,SchoolClass schoolClass,Subject subject);
	List<TeacherSubject> findByTeacherAndSubjectAndYearAndTerm(Teacher teacher,Subject subject,SchoolYear schoolYear,Term term);
	List<TeacherSubject> findBySchoolClassAndSubjectAndYearAndTerm(SchoolClass schoolClass,Subject subject,SchoolYear schoolYear,Term term);

	
}
