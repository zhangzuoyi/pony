package com.zzy.pony.exam.controller;





import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
@RequestMapping(value="/examAdmin/examinee")
public class ExamineeController {
	
	

	
	@RequestMapping(value="generateNo",method=RequestMethod.GET)
	@ResponseBody
	public void generateNo(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){
		
	}
	
	
	
	
}
