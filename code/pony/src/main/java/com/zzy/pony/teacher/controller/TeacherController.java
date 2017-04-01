package com.zzy.pony.teacher.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.TeacherSubjectVo;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
	@Autowired
	private TeacherService service;
	@Autowired
	private TeacherSubjectService tsService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="courses",method = RequestMethod.GET)
	public String courses(Model model){
		User user=userService.findById(ShiroUtil.getLoginUser().getId());
		List<TeacherSubjectVo> subjects=tsService.findCurrentVoByTeacher(user.getTeacher());
		model.addAttribute("courses", subjects);
		return "teacher/courses";
	}
	@RequestMapping(value="examresult",method = RequestMethod.GET)
	public String examresult(Model model){
		User user=userService.findById(ShiroUtil.getLoginUser().getId());
		List<TeacherSubject> subjects=tsService.findCurrentByTeacher(user.getTeacher());
		List<Subject> result=new ArrayList<Subject>();
		for(TeacherSubject ts: subjects){
			result.add(ts.getSubject());
		}
		model.addAttribute("subjects", result);
		return "teacher/examresult";
	}
	@RequestMapping(value="findClassBySubject",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClass> findClassBySubject(@RequestParam(value="subjectId") int subjectId,Model model){
		User user=userService.findById(ShiroUtil.getLoginUser().getId());
		List<TeacherSubject> subjects=tsService.findCurrentByTeacher(user.getTeacher());
		List<SchoolClass> list=new ArrayList<SchoolClass>();
		for(TeacherSubject ts: subjects){
			if(ts.getSubject().getSubjectId() == subjectId){
				list.add(ts.getSchoolClass());
			}
		}
		return list;
	}
	//examresult 成绩管理 按科目，班级，考试做为查询条件
	//resultAnalysis 成绩分析

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
