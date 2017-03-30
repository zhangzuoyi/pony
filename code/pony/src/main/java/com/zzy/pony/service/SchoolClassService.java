package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Teacher;

public interface SchoolClassService {
	void add(SchoolClass sy);
	List<SchoolClass> findAll();
	SchoolClass get(int id);
	void update(SchoolClass sy);
	void delete(int id);
	List<SchoolClass> findByGrade(int gradeId);
	/**
	 * 担任班主任的班级
	 * @param yearId
	 * @param teacher
	 * @return
	 */
	List<SchoolClass> findByYearIdAndTeacher(Integer yearId, Teacher teacher);
	/**
	 * 当前年份担任班主任的班级
	 * @param teacher
	 * @return
	 */
	List<SchoolClass> findCurrentByTeacher(Teacher teacher);
	List<SchoolClass> findByExam(int examId);
	List<SchoolClass> findByYearAndGrade(int yearId, int gradeId);
	List<SchoolClass> findCurrent();
}
