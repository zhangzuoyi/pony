<<<<<<< HEAD
package com.zzy.pony.exam.controller;





import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.exam.vo.ExamineeVo;




@Controller
@RequestMapping(value="/examAdmin/examinee")
public class ExamineeController {
	
	@Autowired
	private ExamineeService examineeService;

	
	@RequestMapping(value="generateNo",method=RequestMethod.GET)
	@ResponseBody
	public void generateNo(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){
		examineeService.generateNo(examId, gradeId, prefixNo, bitNo);
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
=======
package com.zzy.pony.exam.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.exam.vo.ExamineeVo;




@Controller
@RequestMapping(value="/examAdmin/examinee")
public class ExamineeController {
	
	@Autowired
	private ExamineeService examineeService;

	
	@RequestMapping(value="generateNo",method=RequestMethod.GET)
	@ResponseBody
	public void generateNo(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,@RequestParam(value="prefixNo") String prefixNo,@RequestParam(value="bitNo") int bitNo){
		examineeService.generateNo(examId, gradeId, prefixNo, bitNo);
	}
	@RequestMapping(value="listPageByClass",method=RequestMethod.GET)
	@ResponseBody
	public Page<ExamineeVo> listPageByClass(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="examId") int examId,
			@RequestParam(value="classId") int classId){					
		return examineeService.findPageByClass(currentPage, pageSize, examId, classId);		
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
>>>>>>> refs/remotes/origin/master
