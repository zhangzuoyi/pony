package com.zzy.pony.controller;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


























@Controller
@RequestMapping(value = "/classNoCourse")
public class ClassNoCourseController {
	
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "classNoCourse/main";
	}

	
	
	
	
	
	
}
