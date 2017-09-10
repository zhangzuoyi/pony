package com.zzy.pony.evaluation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.evaluation.dao.OutcomeValueDao;
import com.zzy.pony.evaluation.model.OutcomeValue;

@Controller
@RequestMapping(value = "/evaluation/outcomeValues")
public class OutcomeValueController {
	@Autowired
	private OutcomeValueDao dao;
	
	@RequestMapping(value="categories",method = RequestMethod.GET)
	@ResponseBody
	public List<String> categories(Model model){
		return dao.findCategories();
	}
	@RequestMapping(value="findLevel1",method = RequestMethod.GET)
	@ResponseBody
	public List<String> findLevel1(@RequestParam(value="category") String category){
		return dao.findLevel1(category);
	}
	@RequestMapping(value="findLevel2",method = RequestMethod.GET)
	@ResponseBody
	public List<OutcomeValue> findLevel2(@RequestParam(value="category") String category,@RequestParam(value="level1",required=false) String level1){
		return dao.findByCategoryAndLevel1(category, level1);
	}
}
