package com.zzy.pony.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.CommonDict;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.vo.SubjectVo;

@Controller
@RequestMapping(value = "/subject")
public class SubjectController {
	@Autowired
	private SubjectService service;
	@Autowired
	private DictService dictService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("types", Constants.SUBJECT_TYPES);
		return "subject/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SubjectVo> list(Model model){
		List<Subject> list=service.findAll();
		List<CommonDict> importances = dictService.findImportances();
		List<SubjectVo> result = new ArrayList<SubjectVo>();
		for (Subject subject : list) {
			SubjectVo vo = new SubjectVo();
			vo.setImportance(subject.getImportance());
			vo.setName(subject.getName());
			vo.setSubjectId(subject.getSubjectId());
			vo.setType(subject.getType());
			for (CommonDict commonDict : importances) {
				if (subject.getImportance()!=null && commonDict.getCode().equalsIgnoreCase(subject.getImportance().toString())) {
					vo.setImportanceName(commonDict.getValue());
					break;
				}
			}
			result.add(vo);			
		}

		return result;
	}
	@RequestMapping(value="findClassSubject",method = RequestMethod.GET)
	@ResponseBody
	public List<Subject> findClassSubject(Model model){
		List<Subject> list=service.findClassSubject();

		return list;
	}
	@RequestMapping(value="findByExam",method = RequestMethod.GET)
	@ResponseBody
	public List<Subject> findByExam(@RequestParam(value="examId") int examId,Model model){
		List<Subject> subjects = service.findByExam(examId);

		return subjects;
	}
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<Subject> findByClass(@RequestParam(value="classId") int classId,Model model){
		List<Subject> subjects = service.findByClass(classId);
		return subjects;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Subject sy, Model model){
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody Subject sy, Model model){
		service.update(sy);
		return "success";
	}
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public String delete(@RequestParam(value="id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public Subject get(@RequestParam(value="id") int id, Model model){
		Subject g=service.get(id);
		return g;
	}
	
	@RequestMapping(value="getSubjects",method = RequestMethod.GET)
	@ResponseBody
	public List<Subject> getSubjects(@RequestParam(value="subjectIds[]") int[] subjectIds){		
		return service.findSubjects(subjectIds);
	}
	
	

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
