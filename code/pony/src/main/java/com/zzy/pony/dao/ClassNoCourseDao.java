package com.zzy.pony.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;


public interface ClassNoCourseDao extends JpaRepository<ClassNoCourse,Integer>{
	List<ClassNoCourse> findBySchoolClassAndSchoolYearAndTerm(SchoolClass schoolClass,SchoolYear schoolYear,Term term);

}
