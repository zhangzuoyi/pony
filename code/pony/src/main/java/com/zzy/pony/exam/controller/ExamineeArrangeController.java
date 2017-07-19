package com.zzy.pony.exam.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.service.ExamineeArrangeService;




@Controller
@RequestMapping(value="/examAdmin/examineeArrange")
public class ExamineeArrangeController {
	
	@Autowired
	private ExamineeArrangeService examineeArrangeService;

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examineeArrange/main";
	}
	
	
	@RequestMapping(value="submitByClass",method=RequestMethod.GET)
	@ResponseBody
	public void submitByClass(@RequestParam(value="examId") int examId,@RequestParam(value="classIds[]") int[] classIds,@RequestParam(value="arrangeIds[]") int[] arrangeIds){
		
		examineeArrangeService.submitByClass(examId, classIds, arrangeIds);
	}
	
	@RequestMapping(value="submitByStudent",method=RequestMethod.GET)
	@ResponseBody
	public void submitByStudent(@RequestParam(value="examineeIds[]") int[] examineeIds,@RequestParam(value="arrangeIds[]") int[] arrangeIds){
		examineeArrangeService.submitByStudent(examineeIds, arrangeIds);		
	}
	
	@RequestMapping(value="delete",method=RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="arrangeIds[]") int[] arrangeIds){
		examineeArrangeService.delete(arrangeIds);
	}
	
	
	
	
}
