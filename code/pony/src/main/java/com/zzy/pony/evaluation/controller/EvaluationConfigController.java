package com.zzy.pony.evaluation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.evaluation.dao.EvaluationItemDao;
import com.zzy.pony.evaluation.dao.EvaluationSubjectDao;
import com.zzy.pony.evaluation.model.EvaluationItem;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.evaluation.service.EvaluationService;
import com.zzy.pony.evaluation.vo.EvaluationItemVo;

@Controller
@RequestMapping(value = "/evaluation/config")
public class EvaluationConfigController {
	@Autowired
	private EvaluationSubjectDao subjectDao;
	@Autowired
	private EvaluationService service;
	@Autowired
	private EvaluationItemDao itemDao;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String userMain(Model model){
		return "evaluation/config/main";
	}
	@RequestMapping(value="subjects",method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationSubject> subjects(){
		return subjectDao.findAll();
	}
	@RequestMapping(value="itemTree",method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationItemVo> itemTree(Long subjectId){
		return service.itemTreeData(subjectId);
	}
	@ResponseBody
	@RequestMapping(value="addItem",method = RequestMethod.POST)
	public void addItem(@RequestBody EvaluationItem item){
		service.addItem(item);
	}
	@ResponseBody
	@RequestMapping(value="updateItem",method = RequestMethod.POST)
	public void updateItem(@RequestBody EvaluationItem item){
		service.updateItem(item);
	}
	@ResponseBody
	@RequestMapping(value="deleteItem",method = RequestMethod.DELETE)
	public Map<String, String> deleteItem(Long itemId){
		Map<String, String> map=new HashMap<String, String>();
		int childCount=itemDao.findCountByParentItemId(itemId);
		if(childCount > 0) {
			map.put("result", "fail");
		}else {
			map.put("result", "success");
			service.deleteItem(itemId);
		}
		return map;
	}
}
