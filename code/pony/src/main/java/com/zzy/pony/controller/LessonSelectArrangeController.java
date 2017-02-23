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

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.LessonSelectArrangeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.LessonSelectArrangeVo;
import com.zzy.pony.vo.LessonSelectTimeVo;
/**
 * 可选课程设置
 * @author zhangzuoyi
 *
 */
@Controller
@RequestMapping(value = "/lessonSelectArrange")
public class LessonSelectArrangeController {
	@Autowired
	private LessonSelectArrangeService service;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private LessonPeriodService periodService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		SchoolYear currentYear=yearService.getCurrent();
		Term term=termService.getCurrent();
		model.addAttribute("subjects",subjectService.findSelectiveSubject());
		model.addAttribute("teachers",teacherService.findAll());
		model.addAttribute("currentYear",currentYear);
		model.addAttribute("currentTerm",term);
		model.addAttribute("grades",gradeService.findAll());
		List<LessonPeriod> periods=periodService.findBySchoolYearAndTerm(currentYear, term);
		model.addAttribute("periods", periods);
		model.addAttribute("weekdays", Constants.WEEKDAYS);
		return "lessonSelectArrange/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<LessonSelectArrangeVo> list(Model model){
		return service.findCurrent();
	}
//	@RequestMapping(value="findByClass",method = RequestMethod.GET)
//	@ResponseBody
//	public LessonSelectArrangeVo findByClass(@RequestParam(value="classId") Integer classId, Model model){
//		return service.findArrangeVo(classId);
//	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(LessonSelectArrangeVo sy, Model model){
//		service.add(sy);
		System.out.println(sy.getPeriod());
		for(Integer gradeId: sy.getGradeIds()){
			System.out.println(gradeId);
		}
		for(LessonSelectTimeVo timeVo: sy.getLessonSelectTimes()){
			System.out.println(timeVo.getWeekday());
			System.out.println(timeVo.getPeriodId());
		}
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody LessonSelectArrange sy, Model model){
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
	public LessonSelectArrangeVo get(@RequestParam(value="id") int id, Model model){
		return service.findById(id);
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
