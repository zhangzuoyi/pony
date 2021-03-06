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

import com.zzy.pony.model.CommonDict;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.LessonPeriodVo;

@Controller
@RequestMapping(value = "/lessonPeriod")
public class LessonPeriodController {
	@Autowired
	private LessonPeriodService service;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
//		model.addAttribute("years", yearService.findAll());
//		model.addAttribute("terms", termService.findAll());
		model.addAttribute("currentYear", yearService.getCurrent());
		model.addAttribute("currentTerm", termService.getCurrent());
		return "lessonPeriod/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonPeriod> list(Model model){
		List<LessonPeriod> list=service.findAll();

		return list;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody LessonPeriod sy, Model model){
		SchoolYear schoolYear =	  yearService.getCurrent();
		Term term = termService.getCurrent();
		sy.setSchoolYear(schoolYear);
		sy.setTerm(term);
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody LessonPeriod sy, Model model){
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
	public LessonPeriod get(@RequestParam(value="id") int id, Model model){
		LessonPeriod sc=service.get(id);
		return sc;
	}
	@RequestMapping(value="findBySchoolYearAndTerm",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonPeriodVo> findBySchoolYearAndTerm(Model model){
		List<LessonPeriodVo> result = new ArrayList<LessonPeriodVo>();
		SchoolYear schoolYear =	  yearService.getCurrent();
		Term term = termService.getCurrent();
		List<CommonDict> importances = dictService.findImportances();
		List<LessonPeriod> lessonPeriods = service.findBySchoolYearAndTerm(schoolYear, term);
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			LessonPeriodVo vo = new LessonPeriodVo();
			vo.setEndTime(lessonPeriod.getEndTime());
			vo.setImportance(lessonPeriod.getImportance());
			vo.setPeriodId(lessonPeriod.getPeriodId());
			vo.setSeq(lessonPeriod.getSeq());
			vo.setStage(lessonPeriod.getStage());
			vo.setStartTime(lessonPeriod.getStartTime());
			vo.setTermName(schoolYear.getName());
			vo.setYearName(term.getName());
			for (CommonDict commonDict : importances) {
				if (lessonPeriod.getImportance()!=null && commonDict.getCode().equalsIgnoreCase(lessonPeriod.getImportance().toString())) {
					vo.setImportanceName(commonDict.getValue());
					break;
				}
			}
			result.add(vo);
			
		}		
		return result;
	}
	
	

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
