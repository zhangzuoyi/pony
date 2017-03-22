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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.StudentRemark;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentRemarkService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.ExamVo;

@Controller
@RequestMapping(value = "/studentRemark")
public class StudentRemarkController {
	@Autowired
	private StudentRemarkService service;
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
	private DictService dictService;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String main(Model model) {
		model.addAttribute("year", yearService.getCurrent());
		model.addAttribute("term", termService.getCurrent());
		model.addAttribute("classes", scService.findAll());
		model.addAttribute("remarkLevels", dictService.findStudentRemarkLevels());
		return "studentRemark/main";
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<ExamVo> list(Model model) {
		// return service.findByYearAndTerm(yearService.getCurrent(),
		// termService.getCurrent());
		return null;
	}

	@RequestMapping(value = "studentMain", method = RequestMethod.GET)
	public String studentMain(@RequestParam(value = "studentId") int studentId, Model model) {
		model.addAttribute("student", studentService.get(studentId));
		model.addAttribute("remarkLevels", dictService.findStudentRemarkLevels());
		return "studentRemark/studentMain";
	}

	@RequestMapping(value = "findByStudent", method = RequestMethod.GET)
	@ResponseBody
	public List<StudentRemark> findByStudent(@RequestParam(value = "studentId") int studentId, Model model) {
		List<StudentRemark> list=service.findByStudent(studentId);
		for(StudentRemark remark: list){
			remark.getStudent().setSchoolClasses(null);
		}
		return list;
	}

	@RequestMapping(value = "findBySubjectAndClass", method = RequestMethod.GET)
	@ResponseBody
	public List<Exam> findBySubjectAndClass(@RequestParam(value = "subjectId") int subjectId,
			@RequestParam(value = "classId") int classId, Model model) {
		Subject subject = new Subject();
		subject.setSubjectId(subjectId);
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setClassId(classId);
		// List<Exam> list=service.findCurrentBySubjectAndClass(subject,
		// schoolClass);
		// for(Exam g: list){
		// g.setSchoolClasses(null);
		// }
		// return list;
		return null;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(StudentRemark remark, Model model) {
		User user = userService.findById(ShiroUtil.getLoginUser().getId());
		remark.setRemarkTime(new Date());
		remark.setSchoolYear(yearService.getCurrent());
		remark.setTerm(termService.getCurrent());
		remark.setTeacher(user.getTeacher());
		service.add(remark);
		return "success";
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(StudentRemark remark, Model model) {
		service.update(remark);
		
		return "success";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value = "id") int id, Model model) {
		service.delete(id);
		return "success";
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	@ResponseBody
	public ExamVo get(@RequestParam(value = "id") int id, Model model) {
		// return service.getVo(id);
		return null;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}