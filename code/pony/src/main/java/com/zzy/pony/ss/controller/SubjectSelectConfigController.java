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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.vo.LessonArrangeVo;

@Controller
@RequestMapping(value = "/ss/config")
public class SubjectSelectConfigController {
	@Autowired
	private SubjectSelectConfigService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "ss/config/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SubjectSelectConfig> list(Model model){
		List<SubjectSelectConfig> list=service.findAll();

		return list;
	}

	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody SubjectSelectConfig config, Model model){
		config.setCreateTime(new Date());
		config.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		config.setUpdateTime(new Date());
		config.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		service.add(config);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody SubjectSelectConfig config, Model model){
		service.update(config,ShiroUtil.getLoginUser().getLoginName());
		return "success";
	}
	@RequestMapping(value="delete/{id}",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable("id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="current",method = RequestMethod.GET)
	@ResponseBody
	public SubjectSelectConfig current(Model model){
		return service.getCurrent();
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
