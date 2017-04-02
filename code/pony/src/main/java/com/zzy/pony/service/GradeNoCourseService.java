package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Grade;
import com.zzy.pony.model.GradeNoCourse;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.GradeNoCourseVo;



public interface GradeNoCourseService {
	List<GradeNoCourse> findAll();
	List<GradeNoCourseVo> findAllVo();
	void deleteByGradeAndYearAndTerm(Grade grade,SchoolYear schoolYear,Term term);
	void save(GradeNoCourse gradeNoCourse);
}
