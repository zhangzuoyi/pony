package com.zzy.pony.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.vo.TeacherSubjectVo;

@Controller
@RequestMapping(value = "/teacherSubject")
public class TeacherSubjectController {
	@Autowired
	private TeacherSubjectService service;
	@Autowired
	private SchoolClassService scService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TeacherService teacherService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("classes", scService.findAll());
		model.addAttribute("subjects", subjectService.findAll());
		model.addAttribute("teachers", teacherService.findAll());
		
		return "teacherSubject/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubject> list(Model model){
		List<TeacherSubject> list=service.findAll();

		return list;
	}
	@RequestMapping(value="listTree",method = RequestMethod.GET)
	@ResponseBody
	public String listTree(){		
			
		StringBuilder result = new StringBuilder();
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();		
		List<TeacherSubjectVo> teacherSubjects = service.findCurrentAll();
		for (TeacherSubjectVo teacherSubjectVo : teacherSubjects) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tsId", teacherSubjectVo.getTsId());
			map.put("label", teacherSubjectVo.getClassName()+":"+teacherSubjectVo.getTeacherName()+"("+teacherSubjectVo.getSubjectName()+")");						
			lists.add(map);			
		}
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		String treeDatas= gson.toJson(lists);	
		result.append("{\"treeData\"");
		result.append(":");
		result.append(treeDatas);
		result.append("}");
		return result.toString();
	}
	
	@RequestMapping(value="findByTeacherAndSubject",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubjectVo> findByTeacherAndSubject(@RequestParam(value="teacherId") int teacherId,@RequestParam(value="subjectId") int subjectId){
		List<TeacherSubjectVo> list=service.findCurrentVoByTeacherAndSubject(teacherId, subjectId);

		return list;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(TeacherSubject sy, Model model){
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(TeacherSubject sy, Model model){
		service.update(sy);
		return "success";
	}
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value="id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public TeacherSubject get(@RequestParam(value="id") int id, Model model){
		TeacherSubject g=service.get(id);
		return g;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
