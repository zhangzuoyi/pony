package com.zzy.pony.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class IndexController {
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model){
		return "indexLayUI";
	}
	@RequestMapping(value="login",method = RequestMethod.GET)
	public String loginPage(Model model){
		return "login";
	}
	@RequestMapping(value="login",method = RequestMethod.POST)
	public String login(Model model){
		return "login";
	}
}
