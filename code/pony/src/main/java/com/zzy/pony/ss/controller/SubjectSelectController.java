package com.zzy.pony.ss.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.StudentSubjectSelectService;

@Controller
@RequestMapping(value = "/ss/select")
public class SubjectSelectController {
	@Autowired
	private StudentSubjectSelectService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "ss/select/main";
	}

	@RequestMapping(value="selected",method = RequestMethod.GET)
	@ResponseBody
	public List<String> selected(Model model){
		return service.findCurrentSelect(ShiroUtil.getLoginUser().getLoginName());
	}

	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public String save(Integer configId,List<String> subjects, Model model){
		service.save(configId, subjects, ShiroUtil.getLoginUser().getLoginName());
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
