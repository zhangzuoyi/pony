package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Student;

public interface StudentService {
	void add(Student sy);
	List<Student> findAll();
	Student get(int id);
	void update(Student sy);
	void delete(int id);
	List<Student> findBySchoolClass(Integer classId);
}
