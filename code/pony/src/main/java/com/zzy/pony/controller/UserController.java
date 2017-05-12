package com.zzy.pony.controller;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;











import com.zzy.pony.service.UserService;



@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	

	
	@RequestMapping(value="userNameMap",method = RequestMethod.GET)
	@ResponseBody
	public Map<Integer, String> getUserNameMap(){		
		Map<Integer, String> result = userService.getUserNameMap();
		return result;
	}	
	
	
}
