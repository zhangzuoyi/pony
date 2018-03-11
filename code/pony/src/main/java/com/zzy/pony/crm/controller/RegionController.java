package com.zzy.pony.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.crm.model.Area;
import com.zzy.pony.crm.model.Province;
import com.zzy.pony.crm.service.RegionService;

@Controller
@RequestMapping(value = "/region")
public class RegionController {
	@Autowired
	private RegionService service;

	@RequestMapping(value="areaList",method=RequestMethod.GET)
	@ResponseBody
	public List<Area> areaList(){
		return service.areas();
	}
	@RequestMapping(value="provinceList",method=RequestMethod.GET)
	@ResponseBody
	public List<Province> provinceList(){
		return service.provinces();
	}
}
