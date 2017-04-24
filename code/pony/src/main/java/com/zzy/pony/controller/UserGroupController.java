package com.zzy.pony.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;









import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Group;
import com.zzy.pony.model.User;
import com.zzy.pony.service.UserGroupService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.UserGroupVo;



@Controller
@RequestMapping(value = "/userGroup")
public class UserGroupController {
	
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private UserService userService ;
	

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
	public List<UserGroupVo> listByCondition(@RequestParam(value="groupType") String groupType,@RequestParam(value="groupName") String groupName){		
		List<UserGroupVo> result = new ArrayList<UserGroupVo>();
		List<Group> list = userGroupService.listByCondition(groupType, groupName);
		for (Group group : list) {
			UserGroupVo vo = new UserGroupVo();
			vo.setGroupId(group.getGroupId()+"");
			vo.setGroupName(group.getName());
			vo.setGroupType(group.getGroupType());
			if(Constants.USER_GROUP_TYPE_TEACHER.equalsIgnoreCase(group.getGroupType())){
				List<String> teacherGroup = new ArrayList<String>();
				for (User user : group.getUsers()) {
					teacherGroup.add(user.getTeacher().getTeacherId()+"");
				}
				vo.setTeacherGroup(teacherGroup);				
			}
			if(Constants.USER_GROUP_TYPE_STUDENT.equalsIgnoreCase(group.getGroupType())){
				List<String> studentGroup = new ArrayList<String>();
				for (User user : group.getUsers()) {
					studentGroup.add(user.getStudent().getStudentId()+"");
				}
				vo.setStudentGroup(studentGroup);
			}
			result.add(vo);
			
		}
		
		return result;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public void add(@RequestBody UserGroupVo vo){
		Group group = new Group();
		if (Constants.USER_GROUP_TYPE_TEACHER.equalsIgnoreCase(vo.getGroupType())) {
			group.setGroupType(vo.getGroupType());
			group.setName(vo.getGroupName());
			List<User> users = new ArrayList<User>();
			for (String teacherId : vo.getTeacherGroup()) {
				User user = userService.findByTeacherId(Integer.valueOf(teacherId));
				users.add(user);
			}
			group.setUsers(users);
			userGroupService.add(group);	
		}
		if (Constants.USER_GROUP_TYPE_STUDENT.equalsIgnoreCase(vo.getGroupType())) {
			group.setGroupType(vo.getGroupType());
			group.setName(vo.getGroupName());
			List<User> users = new ArrayList<User>();
			for (String studentId : vo.getStudentGroup()) {
				User user = userService.findByStudentId(Integer.valueOf(studentId));
				if (user != null) {
					users.add(user);
				}
			}
			group.setUsers(users);
			userGroupService.add(group);
		}
		
			
	}
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public void update(@RequestBody UserGroupVo vo){
		
		Group group = new Group();
		if (Constants.USER_GROUP_TYPE_TEACHER.equalsIgnoreCase(vo.getGroupType())) {
			group.setGroupId(Integer.valueOf(vo.getGroupId()));
			group.setGroupType(vo.getGroupType());
			group.setName(vo.getGroupName());
			List<User> users = new ArrayList<User>();
			for (String teacherId : vo.getTeacherGroup()) {
				User user = userService.findByTeacherId(Integer.valueOf(teacherId));
				if (user != null) {
					users.add(user);
				}
			}
			group.setUsers(users);
			userGroupService.update(group);	
		}
		if (Constants.USER_GROUP_TYPE_STUDENT.equalsIgnoreCase(vo.getGroupType())) {
			group.setGroupId(Integer.valueOf(vo.getGroupId()));
			group.setGroupType(vo.getGroupType());
			group.setName(vo.getGroupName());
			List<User> users = new ArrayList<User>();
			for (String studentId : vo.getStudentGroup()) {
				User user = userService.findByStudentId(Integer.valueOf(studentId));
				if (user != null) {
					users.add(user);
				}
			}
			group.setUsers(users);
			userGroupService.update(group);
		}
		
		
	}
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="groupId") Integer groupId){
		Group group =  userGroupService.get(groupId);
		userGroupService.delete(group);
	}
	
	@RequestMapping(value="listByGroupId",method = RequestMethod.GET)
	@ResponseBody
	public List<UserGroupVo> listByGroupId(@RequestParam(value="groupId") String groupId){		
		List<UserGroupVo> result = new ArrayList<UserGroupVo>();
		Group group = userGroupService.get(Integer.valueOf(groupId));
		for (User user : group.getUsers()) {
			UserGroupVo vo = new UserGroupVo();
			vo.setGroupId(group.getGroupId()+"");
			vo.setGroupName(group.getName());
			vo.setGroupType(group.getGroupType());
			vo.setUserId(user.getUserId()+"");
			if (Constants.USER_GROUP_TYPE_STUDENT.equalsIgnoreCase(group.getGroupType())) {
				vo.setUserName(user.getStudent().getName());				
			}else if (Constants.USER_GROUP_TYPE_TEACHER.equalsIgnoreCase(group.getGroupType())) {
				vo.setUserName(user.getTeacher().getName());
			}			
			result.add(vo);		
		}			
		return result;
	}
	
}
