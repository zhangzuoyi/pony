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

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherService;

@Controller
@RequestMapping(value = "/schoolClass")
public class SchoolClassController {
	@Autowired
	private SchoolClassService service;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolYearService yearService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("grades", gradeService.findAll());
		model.addAttribute("teachers",teacherService.findAll());
		model.addAttribute("schoolYear",yearService.getCurrent());
		return "schoolClass/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClass> list(Model model){
		List<SchoolClass> list=service.findAll();
		for(SchoolClass sc: list){
//			sc.getGrade().setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="findByGrade",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClass> findByGrade(@RequestParam(value="gradeId") int gradeId,Model model){
		return service.findByGrade(gradeId);
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(SchoolClass sy, Model model){
		Date now=new Date();
		sy.setCreateTime(now);
		sy.setCreateUser("test");
		sy.setUpdateTime(now);
		sy.setUpdateUser("test");
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(SchoolClass sy, Model model){
		Date now=new Date();
		sy.setUpdateTime(now);
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
	public SchoolClass get(@RequestParam(value="id") int id, Model model){
		SchoolClass sc=service.get(id);
//		sc.getGrade().setSchoolClasses(null);
		return sc;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
