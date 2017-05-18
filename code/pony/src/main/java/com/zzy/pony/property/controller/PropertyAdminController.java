package com.zzy.pony.property.controller;




import java.util.ArrayList;
import java.util.Date;
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
import com.zzy.pony.property.model.PropertyTrace;
import com.zzy.pony.property.service.PropertyAdminService;
import com.zzy.pony.property.service.PropertyTraceService;
import com.zzy.pony.property.vo.PropertyVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;




@Controller
@RequestMapping(value = "/property/propertyAdmin")
public class PropertyAdminController {
	static Logger log=LoggerFactory.getLogger(PropertyAdminController.class);
	
	@Autowired
	private PropertyAdminService propertyAdminService;
	@Autowired
	private UserService userService;
	@Autowired
	private PropertyTraceService propertyTraceService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/propertyAdmin/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<PropertyVo> list(){	
		List<PropertyVo> result = new ArrayList<PropertyVo>();
		List<Property> propertys =  propertyAdminService.list();
		for (Property property : propertys) {
			PropertyVo vo = new PropertyVo();
			vo.setPropId(property.getPropId());
			vo.setPropertyTypeName(property.getPropertyType().getName());
			vo.setName(property.getName());
			vo.setSpec(property.getSpec());
			vo.setPropCode(property.getPropCode());
			vo.setLocation(property.getLocation());
			vo.setOwnerName(userService.findUserNameById(property.getOwner().getUserId()));
			vo.setStatus(property.getStatus());
			if(property.getUser() != null){
				vo.setUserName(userService.findUserNameById(property.getUser().getUserId()));		
			}
		result.add(vo);
		}
		
		return result;
		
	}
	
	@RequestMapping(value="receive",method = RequestMethod.POST)
	@ResponseBody
	public void receive(@RequestParam(value="selectPropertys[]") List<Integer> properties,@RequestParam(value="user") int teacherId ){		
		
		for (Integer propId : properties) {	
			Property property = propertyAdminService.get(propId);
			propertyAdminService.receive(property, teacherId);
			
			PropertyTrace propertyTrace = new PropertyTrace();
			propertyTrace.setOperateTime(new Date());
			propertyTrace.setOperation("领用");
			propertyTrace.setOperator(ShiroUtil.getLoginUser().getLoginName());
			propertyTrace.setProperty(property);
			propertyTraceService.add(propertyTrace);
		}
		
		
	}
	
	@RequestMapping(value="back",method = RequestMethod.POST)
	@ResponseBody
	public void back(@RequestBody List<Integer> properties){		
		
		for (Integer propId : properties) {
			Property property = propertyAdminService.get(propId);
			propertyAdminService.back(property);
			
			PropertyTrace propertyTrace = new PropertyTrace();
			propertyTrace.setOperateTime(new Date());
			propertyTrace.setOperation("退还");
			propertyTrace.setOperator(ShiroUtil.getLoginUser().getLoginName());
			propertyTrace.setProperty(property);
			propertyTraceService.add(propertyTrace);
		}
		
	}
	
	@RequestMapping(value="cancle",method = RequestMethod.POST)
	@ResponseBody
	public void cancle(@RequestBody List<Integer> properties){		
		for (Integer propId : properties) {
			Property property =  propertyAdminService.get(propId);
			propertyAdminService.cancle(property);
			
			PropertyTrace propertyTrace = new PropertyTrace();
			propertyTrace.setOperateTime(new Date());
			propertyTrace.setOperation("作废");
			propertyTrace.setOperator(ShiroUtil.getLoginUser().getLoginName());
			propertyTrace.setProperty(property);
			propertyTraceService.add(propertyTrace);
		}
	}
	
	
	
	
}
