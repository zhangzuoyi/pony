package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.SubjectNoCourse;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.SubjectNoCourseVo;



public interface SubjectNoCourseService {
	List<SubjectNoCourse> findAll();
	List<SubjectNoCourseVo> findAllVo();
	void deleteByGradeAndSubjectAndYearAndTerm(Grade grade,Subject subject,SchoolYear schoolYear,Term term);
	void save(SubjectNoCourse subjectNoCourse);
	List<SubjectNoCourseVo> findCurrentAllVo();
}
