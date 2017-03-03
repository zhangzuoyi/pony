package com.zzy.pony.student.controller;

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
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.LessonSelectService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.LessonSelectArrangeStudentVo;
import com.zzy.pony.vo.TeacherSubjectVo;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
	@Autowired
	private TeacherService service;
	@Autowired
	private TeacherSubjectService tsService;
	@Autowired
	private UserService userService;
	@Autowired
	private LessonSelectService selectService;
	
	@RequestMapping(value="chooseCourseMain",method = RequestMethod.GET)
	public String chooseCourseMain(Model model){
		User user=userService.findById(ShiroUtil.getLoginUser().getId());
		List<TeacherSubjectVo> subjects=tsService.findCurrentVoByTeacher(user.getTeacher());
		model.addAttribute("courses", subjects);
		return "student/chooseCourse";
	}
	@RequestMapping(value="chooseCourseList",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonSelectArrangeStudentVo> chooseCourseList(Model model){
		User user=userService.findById(ShiroUtil.getLoginUser().getId());
		
		return selectService.findByStudent(user.getStudent().getStudentId());
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
