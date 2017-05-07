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

import com.zzy.pony.property.model.PropertyType;
import com.zzy.pony.property.service.PropertyTypeService;




@Controller
@RequestMapping(value = "/property/propertyType")
public class PropertyTypeController {
	static Logger log=LoggerFactory.getLogger(PropertyTypeController.class);
	
	@Autowired
	private PropertyTypeService propertyTypeService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/propertyType/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<PropertyType> list(){		
		List<PropertyType> result = propertyTypeService.list();	
		return result;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody PropertyType propertyType){		
		String result = "0";
		if (propertyTypeService.isExist(propertyType)) {
			result  = "1";//存在category
		}else {
			propertyTypeService.add(propertyType);
		}
		
		return result;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody PropertyType propertyType){		
		String result = "0";
		if (propertyTypeService.isExist(propertyType)) {
			result  = "1";//存在category
		}else {
			propertyTypeService.update(propertyType);
		}
		
		return result;
		
		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="typeId") int typeId){		
		propertyTypeService.delete(typeId);
	}
	
	
	
	
}
