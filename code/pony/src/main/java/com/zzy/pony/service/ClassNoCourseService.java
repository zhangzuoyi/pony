package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ClassNoCourseVo;



public interface ClassNoCourseService {
	List<ClassNoCourse> findAll();
	List<ClassNoCourseVo> findAllVo();
	void deleteByClassAndYearAndTerm(SchoolClass schoolClass,SchoolYear schoolYear,Term term);
	void save(ClassNoCourse classNoCourse);
	List<ClassNoCourseVo> findCurrentAllVo();
	List<ClassNoCourseVo> findVoByClass(int classId);
}
