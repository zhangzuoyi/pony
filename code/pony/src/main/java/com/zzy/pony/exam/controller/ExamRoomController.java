package com.zzy.pony.exam.controller;

import java.util.List;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zzy.pony.exam.model.ExamRoom;



import com.zzy.pony.exam.service.ExamRoomService;

import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/examAdmin/examRoom")
public class ExamRoomController {
	
	@Autowired
	private ExamRoomService examRoomService;
	
	

	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamRoom> list(){
       return examRoomService.list(); 
	}
	
	
	
}
