package com.zzy.pony.exam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.service.ExamineeRoomArrangeService;



@Controller
@RequestMapping(value="/examAdmin/examineeRoomArrange")
public class ExamineeRoomArrangeController {
	
	@Autowired
	private ExamineeRoomArrangeService examineeRoomArrangeService;

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examineeRoomArrange/main";
	}
	
	@RequestMapping(value="autoExamineeRoomArrange",method=RequestMethod.GET)
	@ResponseBody
	public void autoExamineeRoomArrange(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){		
		examineeRoomArrangeService.autoExamineeRoomArrange(examId, gradeId);
	}
	
	
	
}
