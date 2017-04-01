package com.zzy.pony.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.model.SubjectNoCourse;
import com.zzy.pony.service.SubjectNoCourseService;
import com.zzy.pony.vo.SubjectNoCourseVo;


@Controller
@RequestMapping(value = "/subjectNoCourse")
public class SubjectNoCourseController {
	
	@Autowired
	private SubjectNoCourseService subjectNoCourseService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "subjectNoCourse/main";
	}

	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SubjectNoCourse> list( ){
		List<SubjectNoCourse> resultList =subjectNoCourseService.findAll();					
		return resultList;
	}
	@RequestMapping(value="listAllVo",method = RequestMethod.GET)
	@ResponseBody
	public List<SubjectNoCourseVo> listAllVo( ){
		List<SubjectNoCourseVo> resultList =subjectNoCourseService.findAllVo();					
		return resultList;
	}
	
	
	
	
	
	
	
}
