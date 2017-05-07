package com.zzy.pony.property.controller;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping(value = "/property/add")
public class PropertyAddController {
	static Logger log=LoggerFactory.getLogger(PropertyAddController.class);
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/add/main";
	}
	
}
