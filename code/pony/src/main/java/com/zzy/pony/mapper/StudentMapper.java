package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.model.Student;

public interface StudentMapper {
	List<Student> findByGradeOrderByStudentId(int  gradeId);
}
