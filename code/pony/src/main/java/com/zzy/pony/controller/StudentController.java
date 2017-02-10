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

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.StudentService;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
	@Autowired
	private StudentService service;
	@Autowired
	private SchoolClassService classService;
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		List<SchoolClass> list=classService.findAll();
		for(SchoolClass sc: list){
//			sc.getGrade().setSchoolClasses(null);
		}
		model.addAttribute("classes", list);
		model.addAttribute("sexes", dictService.findSexes());
		model.addAttribute("credentials", dictService.findCredentials());
		return "student/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Student> list(Model model){
		List<Student> list=service.findAll();
		for(Student g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<Student> findByClass(@RequestParam(value="classId") int classId, Model model){
		List<Student> list=service.findBySchoolClass(classId);
		for(Student g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Student sy, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser("test");
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
		sy.setStatus(Constants.STUDENT_STATUS_DEFAULT);
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(Student sy, Model model){
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
	public Student get(@RequestParam(value="id") int id, Model model){
		Student g=service.get(id);
		g.setSchoolClasses(null);
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
