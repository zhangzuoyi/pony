package com.zzy.pony.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.service.ClassNoCourseService;
import com.zzy.pony.vo.ClassNoCourseVo;



@Controller
@RequestMapping(value = "/classNoCourse")
public class ClassNoCourseController {
	@Autowired
	private ClassNoCourseService classNoCourseService;
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "classNoCourse/main";
	}
	
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassNoCourse> list(Model model){
		List<ClassNoCourse> list = classNoCourseService.findAll();
		return list;
	}
	@RequestMapping(value="listVo",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassNoCourseVo> listVo(Model model){
		List<ClassNoCourseVo> list = classNoCourseService.findAllVo();
		return list;
	}

	
	
	
	
	
	
}
