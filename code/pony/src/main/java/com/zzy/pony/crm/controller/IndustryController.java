package com.zzy.pony.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.crm.model.Industry;
import com.zzy.pony.crm.service.IndustryService;

@Controller
@RequestMapping(value = "/industry")
public class IndustryController {
	@Autowired
	private IndustryService service;

	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public List<Industry> list(){
		return service.findAll();
	}
}
