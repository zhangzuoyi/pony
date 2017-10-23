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

	
	/**
	 * @return 解决fileUpload问题，返回空即可
	 */
	@RequestMapping(value="fileUpload",method=RequestMethod.POST)
	@ResponseBody
	public String fileUpload(){		
		return "0";				
	}
	
	
	@RequestMapping(value="generateNo",method=RequestMethod.POST)
	@ResponseBody
	public String generateNo(MultipartFile fileUpload,HttpServletRequest request,
			@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){		
			examineeService.generateNo(examId, gradeId, prefixNo, bitNo);		
		return "0";
		
		
	}
	@RequestMapping(value="generateNoByFile",method=RequestMethod.POST)
	@ResponseBody
	public String generateNoByFile(MultipartFile fileUpload,HttpServletRequest request,
			@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("fileUpload");					
		examineeService.generateNoByFile(examId, gradeId, prefixNo, bitNo, file);				
		return "0";
		
		
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