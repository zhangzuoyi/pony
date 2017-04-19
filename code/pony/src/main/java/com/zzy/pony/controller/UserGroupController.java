package com.zzy.pony.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Group;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.PreLessonArrangeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.UserGroupService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ArrangeVo;



@Controller
@RequestMapping(value = "/userGroup")
public class UserGroupController {
	
	@Autowired
	private UserGroupService userGroupService;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "userGroup/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Group> list(){		
		List<Group> result = userGroupService.list();	
		return result;
	}	
	@RequestMapping(value="listByCondition",method = RequestMethod.GET)
	@ResponseBody
	public List<Group> listByCondition(@RequestParam(value="groupType") String groupType,@RequestParam(value="groupName") String groupName){		
		List<Group> result = userGroupService.listByCondition(groupType, groupName);	
		return result;
	}
	
	
	

	
	
	
	
	
	
	
	
}
