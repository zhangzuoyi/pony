package com.zzy.pony.controller;



import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Role;
import com.zzy.pony.model.User;
import com.zzy.pony.service.RoleService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.Sha1HashUtil;
import com.zzy.pony.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "user/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<UserVo> list(){		
		
		return   userService.list();
		
	}
	@RequestMapping(value="listPage",method = RequestMethod.GET)
	@ResponseBody
	public Page<UserVo> listPage(@RequestParam(value="currentPage",defaultValue="0") int currentPage,@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="userType",defaultValue="") String userType,@RequestParam(value="userName",defaultValue="") String userName){		
//		Pageable pageable=new PageRequest(currentPage, pageSize,Direction.ASC,"userId");
//		Page<UserVo> result=null;		
//		Page<User> userPage = userService.findAll(pageable);
//		List<UserVo> content = new ArrayList<UserVo>();
//		for (User user : userPage) {
//			UserVo vo = new UserVo();
//			vo.setLastLoginTime(user.getLastLoginTime());
//			vo.setLoginName(user.getLoginName());
//			vo.setPsw(user.getPsw());
//			vo.setUserId(user.getUserId());
//			vo.setUserType(user.getUserType());
//			if (Constants.USER_TYPE_STUDENT.equalsIgnoreCase(user.getUserType())){
//				vo.setUserName(user.getStudent().getName());
//			}
//			if (Constants.USER_TYPE_TEACHER.equalsIgnoreCase(user.getUserType())){
//				vo.setUserName(user.getTeacher().getName());
//			}
//			List<String> roles = new ArrayList<String>();
//			if(user.getRoles() != null && user.getRoles().size()>0){
//				for (Role role : user.getRoles()) {
//					roles.add(role.getRoleCode());
//				}
//			}
//			vo.setRoles(roles.toArray(new String[0]));
//			content.add(vo);
//			
//		}
//		result = new PageImpl<UserVo>(content, pageable, userPage.getTotalElements());					
//		return result;
		
		return userService.findPage(currentPage, pageSize, userType, userName);
		
	}


	
	@RequestMapping(value="setRole",method = RequestMethod.POST)
	@ResponseBody
	public void setRole(@RequestBody UserVo userVo){		
		
		User user = userService.findById(userVo.getUserId());
		List<Role> roles = new ArrayList<Role>();
		if (userVo.getRoles() != null && userVo.getRoles().length>0) {
			for (String roleCode : userVo.getRoles()) {
				Role role = roleService.get(roleCode);
				roles.add(role);
			}
			user.setRoles(roles);
		}
		userService.update(user);		
	}
	@RequestMapping(value="resetPsw",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> resetPsw(@RequestParam(value="userId") int userId,@RequestParam(value="initPsw") String initPsw,@RequestParam(value="psw") String psw){
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "success");
		User user = userService.findById(userId);
		//校验原始密码是否以一致
		if (!Sha1HashUtil.hashPassword(initPsw, user.getLoginName()).equalsIgnoreCase(user.getPsw())) {
			map.put("result", "fail");
		}		
		//加密
		user.setPsw(Sha1HashUtil.hashPassword(psw,user.getLoginName()));
		userService.update(user);
		return map;
		
	}


	
	
	
	
	
	
	
	@RequestMapping(value="userNameMap",method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, String> getUserNameMap(){		
		Map<Integer, String> result = userService.getUserNameMap();
		return result;
	}	
	
	
}
