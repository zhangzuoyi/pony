package com.zzy.pony.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;





















import com.zzy.pony.model.Role;
import com.zzy.pony.model.User;
import com.zzy.pony.service.RoleService;
import com.zzy.pony.service.UserService;
import com.zzy.pony.vo.UserVo;



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
	public Page<UserVo> listPage(@RequestParam(value="currentPage",defaultValue="0") int currentPage,@RequestParam(value="pageSize",defaultValue="20") int pageSize){		
		Pageable pageable=new PageRequest(currentPage, pageSize,Direction.ASC,"userId");
		Page<UserVo> result=null;		
		Page<User> userPage = userService.findAll(pageable);
		List<UserVo> content = new ArrayList<UserVo>();
		for (User user : userPage) {
			UserVo vo = new UserVo();
			vo.setLastLoginTime(user.getLastLoginTime());
			vo.setLoginName(user.getLoginName());
			vo.setPsw(user.getPsw());
			vo.setUserId(user.getUserId());
			vo.setUserType(user.getUserType());
			List<String> roles = new ArrayList<String>();
			if(user.getRoles() != null && user.getRoles().size()>0){
				for (Role role : user.getRoles()) {
					roles.add(role.getRoleCode());
				}
			}
			vo.setRoles(roles.toArray(new String[0]));
			content.add(vo);
			
		}
		result = new PageImpl<UserVo>(content, pageable, userPage.getTotalElements());					
		return result;
		
		
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
	
	
	
	
	
	
	
	@RequestMapping(value="userNameMap",method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, String> getUserNameMap(){		
		Map<Integer, String> result = userService.getUserNameMap();
		return result;
	}	
	
	
}
