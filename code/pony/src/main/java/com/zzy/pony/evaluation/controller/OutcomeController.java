package com.zzy.pony.evaluation.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.service.OutcomeService;
import com.zzy.pony.evaluation.vo.OutcomeVo;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.security.ShiroUtil;

@Controller
@RequestMapping(value = "/evaluation/outcome")
public class OutcomeController {
	@Autowired
	private OutcomeService service;
	
	@RequestMapping(value="userMain",method = RequestMethod.GET)
	public String userMain(Model model){
		return "evaluation/outcome/userMain";
	}
	@RequestMapping(value="mylist",method = RequestMethod.GET)
	@ResponseBody
	public List<OutcomeVo> mylist(){
		Integer teacherId=ShiroUtil.getLoginUser().getTeacherId();
		return service.findByTeacher(teacherId);
	}
	@RequestMapping(value="addOutcome",method = RequestMethod.POST)
	@ResponseBody
	public String addOutcome(@RequestBody Outcome outcome) {
		outcome.setCreateTime(new Date());
		outcome.setCreateUser(ShiroUtil.getLoginName());
		outcome.setStatus(Outcome.STATUS_UNCHECK);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(ShiroUtil.getLoginUser().getTeacherId());
		outcome.setTeacher(teacher);
		service.add(outcome);
		return "success";
	}
}
