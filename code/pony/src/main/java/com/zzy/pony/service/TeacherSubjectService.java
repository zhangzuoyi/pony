package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.TeacherSubject;

public interface TeacherSubjectService {
	void add(TeacherSubject sy);
	List<TeacherSubject> findAll();
	TeacherSubject get(int id);
	void update(TeacherSubject sy);
	void delete(int id);
}
