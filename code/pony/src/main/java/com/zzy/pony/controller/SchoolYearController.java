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

import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.vo.SchoolYearVo;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/schoolYear")
public class SchoolYearController {
	@Autowired
	private SchoolYearService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "schoolYear/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolYear> list(Model model){
		return service.findAll();
	}
	@RequestMapping(value="listVo",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolYearVo> listVo(Model model){
		List<SchoolYearVo> result =  new ArrayList<SchoolYearVo>();
		List<SchoolYear> list = service.findAll();
		for (SchoolYear schoolYear : list) {
			SchoolYearVo vo =new SchoolYearVo();
			vo.setEndYear(schoolYear.getEndYear()+"");
			vo.setIsCurrent(schoolYear.getIsCurrent());
			vo.setName(schoolYear.getName());
			vo.setStartYear(schoolYear.getStartYear()+"");
			vo.setYearId(schoolYear.getYearId()+"");
			result.add(vo);
		}
		return result;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(SchoolYear sy, Model model){
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(SchoolYear sy, Model model){
		service.update(sy);
		return "success";
	}
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value="id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="setCurrent",method = RequestMethod.POST)
	@ResponseBody
	public String setCurrent(@RequestParam(value="id") int id, Model model){
		service.setCurrent(id);
		return "success";
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public SchoolYear get(@RequestParam(value="id") int id, Model model){
		return service.get(id);
	}
	@RequestMapping(value="getCurrent",method = RequestMethod.GET)
	@ResponseBody
	public SchoolYear getCurrent(){
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
