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

import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.dao.SchoolYearDao;
import com.zzy.pony.dao.TermDao;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamType;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.ExamVo;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {
	@Autowired
	private ExamService service;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolClassService scService;
	@Autowired
	private ExamTypeDao etDao;
	@Autowired
	private SchoolYearDao schoolYearDao;
	@Autowired
	private TermDao termDao;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("year",yearService.getCurrent());
		model.addAttribute("term",termService.getCurrent());
		model.addAttribute("subjects",subjectService.findAll());
		model.addAttribute("classes",scService.findAll());
		model.addAttribute("types",etDao.findAll());
		return "exam/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamVo> list(Model model) {
		return service.findByYearAndTerm(yearService.getCurrent(), termService.getCurrent());
	}

	@RequestMapping(value="findBySubject",method = RequestMethod.GET)
	@ResponseBody
	public List<Exam> findBySubject(@RequestParam(value="subjectId") int subjectId,Model model){
		Subject subject=new Subject();
		subject.setSubjectId(subjectId);
		List<Exam> list=service.findBySubject(subject);
		for(Exam g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="findBySubjectAndClass",method = RequestMethod.GET)
	@ResponseBody
	public List<Exam> findBySubjectAndClass(@RequestParam(value="subjectId") int subjectId,@RequestParam(value="classId") int classId,Model model){
		Subject subject=new Subject();
		subject.setSubjectId(subjectId);
		SchoolClass schoolClass=new SchoolClass();
		schoolClass.setClassId(classId);
		List<Exam> list=service.findCurrentBySubjectAndClass(subject, schoolClass);
		for(Exam g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	@RequestMapping(value="findByYearAndTerm",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamVo> findByYearAndTerm(@RequestParam(value="yearId") int yearId,@RequestParam(value="termId") int termId,Model model){
		SchoolYear schoolYear=  schoolYearDao.findOne(yearId);
		Term term = termDao.findOne(termId);
		List<ExamVo> list=service.findByYearAndTerm(schoolYear, term);
		for (ExamVo examVo : list) {
			examVo.setSchoolClasses(null);
			examVo.setSubjects(null);
		}
		return list;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Exam sy, Integer[] classIds, Integer[] subjectIds, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setUpdateTime(new Date());
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		List<SchoolClass> classList=new ArrayList<SchoolClass>();
		for(Integer cid: classIds){
			SchoolClass sc=new SchoolClass();
			sc.setClassId(cid);
			classList.add(sc);
		}
		sy.setSchoolClasses(classList);
		service.add(sy, subjectIds);
		return "success";
	}
	@RequestMapping(value="add2",method = RequestMethod.POST)
	@ResponseBody
	public String add2(@RequestBody ExamVo vo, Model model){
		Exam sy=new Exam();
		sy.setName(vo.getName());
		sy.setExamDate(vo.getExamDate());
		sy.setSchoolYear(vo.getSchoolYear());
		sy.setTerm(vo.getTerm());
		sy.setType(vo.getType());
		sy.setCreateTime(new Date());
		sy.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setUpdateTime(new Date());
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		List<SchoolClass> classList=new ArrayList<SchoolClass>();
		for(Integer cid: vo.getClassIds()){
			SchoolClass sc=new SchoolClass();
			sc.setClassId(cid);
			classList.add(sc);
		}
		sy.setSchoolClasses(classList);
		service.add(sy, vo.getSubjectIds());
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(Exam sy, Integer[] classIds,Integer[] subjectIds, Model model){
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
		List<Integer> ids=new ArrayList<Integer>();
		for(Integer cid: classIds){
			if(cid != null)
				ids.add(cid);
		}
		service.update(sy, ids, subjectIds);
		return "success";
	}
	@RequestMapping(value="edit2",method = RequestMethod.POST)
	@ResponseBody
	public String edit2(@RequestBody ExamVo vo, Model model){
		Exam sy=new Exam();
		sy.setExamId(vo.getExamId());
		sy.setName(vo.getName());
		sy.setExamDate(vo.getExamDate());
		sy.setType(vo.getType());
		sy.setUpdateTime(new Date());
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		List<Integer> ids=new ArrayList<Integer>();
		for(Integer cid: vo.getClassIds()){
			if(cid != null)
				ids.add(cid);
		}
		service.update(sy, ids, vo.getSubjectIds());
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
	public ExamVo get(@RequestParam(value="id") int id, Model model){
		return service.getVo(id);
	}
	@RequestMapping(value="examTypes",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamType> examTypes(Model model){
		return etDao.findAll();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
