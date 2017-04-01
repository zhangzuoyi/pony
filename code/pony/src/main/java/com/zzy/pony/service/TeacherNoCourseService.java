package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.vo.TeacherNoCourseVo;



public interface TeacherNoCourseService {
	List<TeacherNoCourse> findAll();
	List<TeacherNoCourseVo> findAllVo();
}
