package com.zzy.pony.controller;












import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.model.ExamType;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.ExamTypeService;



@Controller
@RequestMapping(value = "/examType")
public class ExamTypeController {
	@Autowired
	private ExamTypeService examTypeService;
	
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamType> list(Model model){
		List<ExamType> list=examTypeService.findAll();
		
		return list;
	}
	
	
	
	
	
}
