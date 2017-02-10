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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.vo.LessonArrangeVo;

@Controller
@RequestMapping(value = "/lessonArrange")
public class LessonSelectArrangeController {
	@Autowired
	private LessonArrangeService service;
	@Autowired
	private SchoolClassService scService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("classes",scService.findAll());
		return "lessonArrange/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonArrange> list(Model model){
		List<LessonArrange> list=service.findAll();
		for(LessonArrange g: list){
//			g.setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public LessonArrangeVo findByClass(@RequestParam(value="classId") Integer classId, Model model){
		return service.findArrangeVo(classId);
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody LessonArrange sy, Model model){
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody LessonArrange sy, Model model){
		service.update(sy);
		return "success";
	}
	@RequestMapping(value="delete/{id}",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable("id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public LessonArrange get(@RequestParam(value="id") int id, Model model){
		LessonArrange g=service.get(id);
//		g.setSchoolClasses(null);
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
