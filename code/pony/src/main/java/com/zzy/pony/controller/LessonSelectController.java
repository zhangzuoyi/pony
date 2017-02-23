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

import com.zzy.pony.model.Grade;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonSelectService;
import com.zzy.pony.vo.LessonSelectArrangeVo;
/**
 * 选课管理
 * 先定位到学生，再展示出该学生课表，点击课表弹出课程供选择
 * 可先展示该学生课程列表
 * @author zhangzuoyi
 *
 */
@Controller
@RequestMapping(value = "/lessonSelect")
public class LessonSelectController {
	@Autowired
	private LessonSelectService service;
	@Autowired
	private GradeService gradeService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("grades",gradeService.findAll());
		return "lessonSelect/main";
	}
//	@RequestMapping(value="list",method = RequestMethod.GET)
//	@ResponseBody
//	public List<Grade> list(Model model){
//		List<Grade> list=service.findAll();
//		for(Grade g: list){
//		}
//		return list;
//	}
//	@RequestMapping(value="add",method = RequestMethod.POST)
//	@ResponseBody
//	public String add(Grade sy, Model model){
//		service.add(sy);
//		return "success";
//	}
//	@RequestMapping(value="edit",method = RequestMethod.POST)
//	@ResponseBody
//	public String edit(Grade sy, Model model){
//		service.update(sy);
//		return "success";
//	}
//	@RequestMapping(value="delete",method = RequestMethod.POST)
//	@ResponseBody
//	public String delete(@RequestParam(value="id") int id, Model model){
//		service.delete(id);
//		return "success";
//	}
	@RequestMapping(value="lessonForSelect",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonSelectArrangeVo> lessonForSelect(@RequestParam(value="studentId") int studentId, Model model){
		return service.lessonForStudentSelect(studentId);
	}
	@RequestMapping(value="selectLesson",method = RequestMethod.POST)
	@ResponseBody
	public List<LessonSelectArrangeVo> selectLesson(@RequestParam(value="studentId") int studentId,@RequestParam(value="arrangeId") int arrangeId, Model model){
		service.selectLesson(studentId, arrangeId);
		return service.lessonStudentSelected(studentId);
	}
	@RequestMapping(value="lessonSelected",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonSelectArrangeVo> lessonSelected(@RequestParam(value="studentId") int studentId, Model model){
		return service.lessonStudentSelected(studentId);
	}
	@RequestMapping(value="deleteSelect",method = RequestMethod.POST)
	@ResponseBody
	public List<LessonSelectArrangeVo> deleteSelect(@RequestParam(value="studentId") int studentId,@RequestParam(value="arrangeId") int arrangeId, Model model){
		service.deleteSelect(studentId, arrangeId);
		return service.lessonStudentSelected(studentId);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
