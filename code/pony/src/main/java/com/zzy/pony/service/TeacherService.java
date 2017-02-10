package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Teacher;

public interface TeacherService {
	void add(Teacher sy);
	List<Teacher> findAll();
	Teacher get(int id);
	void update(Teacher sy);
	void delete(int id);
	Teacher findByTeacherNo(String teacherNo);
}
