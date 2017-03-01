package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Subject;

public interface ExamService {
	void add(Exam sy);
	List<Exam> findAll();
	Exam get(int id);
	void update(Exam sy, List<Integer> classIds);
	void delete(int id);
	List<Exam> findBySubject(Subject subject);
	List<Exam> findCurrentBySubjectAndClass(Subject subject, SchoolClass schoolClass);
}
