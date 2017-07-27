package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ExamVo;

public interface ExamService {
	void add(Exam sy, Integer[] subjectIds);
	List<Exam> findAll();
	Exam get(int id);
	void update(Exam sy, List<Integer> classIds, Integer[] subjectIds);
	void delete(int id);
	List<Exam> findBySubject(Subject subject);
	List<Exam> findCurrentBySubjectAndClass(Subject subject, SchoolClass schoolClass);
	List<ExamVo> findByYearAndTerm(SchoolYear year, Term term); 
	ExamVo getVo(int id);
	List<ExamVo> findByYearAndTermOrderByExamDate(SchoolYear year, Term term); 

	
}
