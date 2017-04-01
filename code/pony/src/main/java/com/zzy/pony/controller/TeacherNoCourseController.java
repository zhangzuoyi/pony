package com.zzy.pony.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.service.TeacherNoCourseService;
import com.zzy.pony.vo.TeacherNoCourseVo;


@Controller
@RequestMapping(value = "/teacherNoCourse")
public class TeacherNoCourseController {
	
	@Autowired
	private TeacherNoCourseService teacherNoCourseService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "teacherNoCourse/main";
	}

	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherNoCourse> list( ){
		List<TeacherNoCourse> resultList =teacherNoCourseService.findAll();					
		return resultList;
	}
	@RequestMapping(value="listAllVo",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherNoCourseVo> listAllVo( ){
		List<TeacherNoCourseVo> resultList =teacherNoCourseService.findAllVo();					
		return resultList;
	}
	
	
	
	
	
	
	
}
