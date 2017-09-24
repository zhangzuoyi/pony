package com.zzy.pony.controller;


import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.User;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/personalInfo")
public class PersonalInfoController {
	
	@Autowired
	private UserService userService;

	

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "personalInfo/main";
	}



	@RequestMapping(value="listOne",method = RequestMethod.GET)
	@ResponseBody
	public UserVo listOne(){
		UserVo vo = new UserVo();
		User user  = userService.findById(ShiroUtil.getLoginUser().getId())  ;
			vo.setLastLoginTime(user.getLastLoginTime());
			vo.setLoginName(user.getLoginName());
			vo.setPsw(user.getPsw());
			vo.setUserId(user.getUserId());
			vo.setUserType(user.getUserType());
			if (Constants.USER_TYPE_STUDENT.equalsIgnoreCase(user.getUserType())){
				vo.setUserName(user.getStudent().getName());
			}
			if (Constants.USER_TYPE_TEACHER.equalsIgnoreCase(user.getUserType())){
				vo.setUserName(user.getTeacher().getName());
			}
		List<String> roles = new ArrayList<String>();
		if(user.getRoles() != null && user.getRoles().size()>0){
			for (Role role : user.getRoles()) {
				roles.add(role.getRoleCode());
			}
		}
		vo.setRoles(roles.toArray(new String[0]));

		return vo;


	}
	



	
	
	
	
	
	
	

	
	
}
