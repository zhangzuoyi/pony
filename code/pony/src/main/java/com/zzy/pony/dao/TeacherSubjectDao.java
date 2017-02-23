package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;


public interface TeacherSubjectDao extends JpaRepository<TeacherSubject,Integer>{
	List<TeacherSubject> findByTeacherAndYearAndTerm(Teacher teacher,SchoolYear year,Term term);

}
