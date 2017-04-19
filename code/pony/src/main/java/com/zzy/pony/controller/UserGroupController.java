package com.zzy.pony.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import com.zzy.pony.model.Group;
import com.zzy.pony.service.UserGroupService;



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
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public void add(@RequestBody Group group){
		userGroupService.add(group);		
	}
	@RequestMapping(value="update",method = RequestMethod.GET)
	@ResponseBody
	public void update(@RequestBody Group group){
		userGroupService.update(group);
	}
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(Integer groupId){
		Group group =  userGroupService.get(groupId);
		userGroupService.delete(group);
	}
	
	
	
	

	
	
	
	
	
	
	
	
}
