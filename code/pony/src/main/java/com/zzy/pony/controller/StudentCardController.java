package com.zzy.pony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolYearService;

@Controller
@RequestMapping(value = "/studentCard")
public class StudentCardController {
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private GradeService gradeService;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("years",yearService.findAll());
		model.addAttribute("currentYear",yearService.getCurrent());
		model.addAttribute("grades",gradeService.findAll());
		return "studentCard/main";
	}
}
