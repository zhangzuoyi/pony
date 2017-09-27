package com.zzy.pony.exam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.exam.vo.ExamineeVo;




@Controller
@RequestMapping(value="/examAdmin/examinee")
public class ExamineeController {
	
	@Autowired
	private ExamineeService examineeService;

	
	@RequestMapping(value="generateNo",method=RequestMethod.POST)
	@ResponseBody
	public void generateNo(MultipartFile fileUpload,HttpServletRequest request,
			@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("fileUpload");
		//按照附件excel中的总成绩排名生成
		if (file != null) {
			
			examineeService.generateNoByFile(examId, gradeId, prefixNo, bitNo, file);
			
		}else {
			examineeService.generateNo(examId, gradeId, prefixNo, bitNo);
		}
		
		
	}
	@RequestMapping(value="listPageByClass",method=RequestMethod.GET)
	@ResponseBody
	public Page<ExamineeVo> listPageByClass(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="examId") int examId,
			@RequestParam(value="classId") int classId){					
		return examineeService.findPageByClass(currentPage, pageSize, examId, classId);		
	}
	@RequestMapping(value="listByClass",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamineeVo> listByClass(
			@RequestParam(value="examId") int examId,
			@RequestParam(value="classId") int classId){					
		return examineeService.findByClass(examId, classId);
	}
	@RequestMapping(value="listBySubjects",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamineeVo> listBySubjects(
			@RequestParam(value="examId") int examId,
			@RequestParam(value="subjects[]") String[] subjects){					
		return examineeService.findByExamIdAndSubjects(examId, subjects);
	}
	@RequestMapping(value="listPageByClassAndArrange",method=RequestMethod.GET)
	@ResponseBody
	public Page<ExamineeVo> listPageByClassAndArrange(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="examId") int examId,
			@RequestParam(value="classId") int classId,
			@RequestParam(value="arrangeId") int arrangeId){					
		return examineeService.findPageByClassAndArrange(currentPage, pageSize, examId, classId, arrangeId);		
	}
	@RequestMapping(value="isGenerateShow",method=RequestMethod.GET)
	@ResponseBody
	public boolean isGenerateShow(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
		
		return examineeService.isGenerateShow(examId, gradeId);
	}
	
	
	
	
}