package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherNoCourseVo;



public interface TeacherNoCourseService {
	List<TeacherNoCourse> findAll();
	List<TeacherNoCourseVo> findAllVo();
	void deleteByTeacherAndYearAndTerm(Teacher teacher,SchoolYear schoolYear,Term term);
	void save(TeacherNoCourse teacherNoCourse);
	List<TeacherNoCourseVo> findCurrentAllVo();
	List<TeacherNoCourseVo> findVoByTeacher(int teacherId);


}
