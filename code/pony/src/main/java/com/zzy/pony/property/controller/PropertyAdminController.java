package com.zzy.pony.property.controller;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.property.model.Property;
import com.zzy.pony.property.model.PropertyType;
import com.zzy.pony.property.service.PropertyTypeService;




@Controller
@RequestMapping(value = "/property/propertyAdmin")
public class PropertyAdminController {
	static Logger log=LoggerFactory.getLogger(PropertyAdminController.class);
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/propertyAdmin/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Property> list(){		
		return null;
	}
	
	@RequestMapping(value="receive",method = RequestMethod.POST)
	@ResponseBody
	public void receive(@RequestBody PropertyType propertyType){		
		
	}
	
	@RequestMapping(value="back",method = RequestMethod.POST)
	@ResponseBody
	public void back(@RequestBody PropertyType propertyType){		
		
		
		
	}
	
	@RequestMapping(value="cancle",method = RequestMethod.GET)
	@ResponseBody
	public void cancle(@RequestParam(value="typeId") int typeId){		
		
	}
	
	
	
	
}
