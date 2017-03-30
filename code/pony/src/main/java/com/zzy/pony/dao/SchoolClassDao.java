package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Teacher;


public interface SchoolClassDao extends JpaRepository<SchoolClass,Integer>{
	List<SchoolClass> findByClassIdIn(List<Integer> ids);
	List<SchoolClass> findByGrade(Grade grade);
	List<SchoolClass> findByYearIdAndTeacher(Integer yearId, Teacher teacher);
	List<SchoolClass> findByYearIdAndGradeIn(Integer yearId, List<Grade> grades);
	List<SchoolClass> findByYearIdAndGrade(Integer yearId, Grade grade);
	List<SchoolClass> findByYearId(Integer yearId);
}
