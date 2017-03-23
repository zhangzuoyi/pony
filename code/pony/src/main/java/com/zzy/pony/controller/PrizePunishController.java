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
import com.zzy.pony.model.PrizePunish;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.PrizePunishService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.TermService;

@Controller
@RequestMapping(value = "/prizePunish")
public class PrizePunishController {
	@Autowired
	private PrizePunishService service;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SchoolClassService scService;
	@Autowired
	private StudentService studentService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String main(Model model) {
		model.addAttribute("year", yearService.getCurrent());
		model.addAttribute("term", termService.getCurrent());
		model.addAttribute("classes", scService.findAll());
		model.addAttribute("ppTypes", Constants.PP_TYPES);
		return "prizePunish/main";
	}

	@RequestMapping(value = "studentMain", method = RequestMethod.GET)
	public String studentMain(@RequestParam(value = "studentId") int studentId, Model model) {
		model.addAttribute("student", studentService.get(studentId));
		model.addAttribute("ppTypes", Constants.PP_TYPES);
		return "prizePunish/studentMain";
	}

	@RequestMapping(value = "findByStudent", method = RequestMethod.GET)
	@ResponseBody
	public List<PrizePunish> findByStudent(@RequestParam(value = "studentId") int studentId, Model model) {
		List<PrizePunish> list = service.findByStudent(studentId);
		for (PrizePunish pp : list) {
			pp.getStudent().setSchoolClasses(null);
		}
		return list;
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(PrizePunish pp, Model model) {
		pp.setSchoolYear(yearService.getCurrent());
		pp.setTerm(termService.getCurrent());
		service.add(pp, ShiroUtil.getLoginUser().getLoginName());
		return "success";
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(PrizePunish pp, Model model) {
		service.update(pp, ShiroUtil.getLoginUser().getLoginName());

		return "success";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value = "id") int id, Model model) {
		service.delete(id);
		return "success";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}