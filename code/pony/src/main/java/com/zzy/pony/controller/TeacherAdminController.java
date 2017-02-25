package com.zzy.pony.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.zzy.pony.model.Teacher;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TeacherService;

@Controller
@RequestMapping(value = "/teacherAdmin")
public class TeacherAdminController {
	@Autowired
	private TeacherService service;
	@Autowired
	private DictService dictService;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("degrees", dictService.findEducationDegrees());
		model.addAttribute("sexes", dictService.findSexes());
		model.addAttribute("credentials", dictService.findCredentials());
		model.addAttribute("subjects", subjectService.findAll());
		return "teacherAdmin/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Teacher> list(Model model){
		List<Teacher> list=service.findAll();

		return list;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Teacher sy, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser("test");
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(Teacher sy, Model model){
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
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
	public Teacher get(@RequestParam(value="id") int id, Model model){
		Teacher g=service.get(id);
		return g;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
