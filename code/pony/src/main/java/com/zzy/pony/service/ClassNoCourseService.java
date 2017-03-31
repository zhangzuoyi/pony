package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.vo.ClassNoCourseVo;



public interface ClassNoCourseService {
	List<ClassNoCourse> findAll();
	List<ClassNoCourseVo> findAllVo();
	
}
