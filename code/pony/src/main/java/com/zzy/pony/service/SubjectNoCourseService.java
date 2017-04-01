package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SubjectNoCourse;
import com.zzy.pony.vo.SubjectNoCourseVo;



public interface SubjectNoCourseService {
	List<SubjectNoCourse> findAll();
	List<SubjectNoCourseVo> findAllVo();
}
