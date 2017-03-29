package com.zzy.pony.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentCardService;
import com.zzy.pony.vo.StudentCard;

@Controller
@RequestMapping(value = "/studentCard")
public class StudentCardController {
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private StudentCardService service;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("years",yearService.findAll());
		model.addAttribute("currentYear",yearService.getCurrent());
		model.addAttribute("grades",gradeService.findAll());
		return "studentCard/main";
	}
	@RequestMapping(value="getCard",method = RequestMethod.GET)
	@ResponseBody
	public StudentCard getCard(@RequestParam(value="studentId") int studentId, Model model){
		StudentCard card=service.get(studentId);
		card.getStudent().setSchoolClasses(null);
		return card;
	}
}
