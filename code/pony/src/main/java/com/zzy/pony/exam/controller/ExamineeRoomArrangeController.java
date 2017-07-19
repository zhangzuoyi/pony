package com.zzy.pony.exam.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value="/examAdmin/examineeRoomArrange")
public class ExamineeRoomArrangeController {
	
	

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examineeRoomArrange/main";
	}
	
	@RequestMapping(value="autoExamineeRoomArrange",method=RequestMethod.GET)
	@ResponseBody
	public void autoExamineeRoomArrange(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
		
		
	}
	
	
	
}
