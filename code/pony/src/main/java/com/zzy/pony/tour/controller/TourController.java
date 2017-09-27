package com.zzy.pony.tour.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.tour.service.TourService;
import com.zzy.pony.tour.vo.TourCategoryVo;
import com.zzy.pony.tour.vo.TourConditionVo;
import com.zzy.pony.tour.vo.TourVo;

@Controller
@RequestMapping(value = "/tour")
public class TourController {
	@Autowired
	private TourService service;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model) {
		return "tour/main";
	}
	@RequestMapping(value="categories",method = RequestMethod.GET)
	@ResponseBody
	public List<TourCategoryVo> categories(Model model) {
		return service.categories();
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public void add(@RequestBody TourVo tour,Model model) {
		tour.setCreateTime(new Date());
		tour.setCreateUser(ShiroUtil.getLoginName());
		tour.setUpdateTime(new Date());
		tour.setUpdateUser(ShiroUtil.getLoginName());
		service.add(tour);
	}
	@RequestMapping(value="findPage",method = RequestMethod.POST)
	@ResponseBody
	public Page<TourVo> findPage(@RequestBody TourConditionVo condition,Model model) {
		return service.findPage(condition);
	}
}
