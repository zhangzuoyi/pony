package com.zzy.pony.exam.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping(value="/examAdmin/classExamineeRoomArrange")
public class ClassExamineeRoomArrangeController {
	
	

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/classExamineeRoomArrange/main";
	}
	
	
	
	
}
