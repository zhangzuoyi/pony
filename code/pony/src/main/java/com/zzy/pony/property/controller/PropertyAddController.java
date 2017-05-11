package com.zzy.pony.property.controller;




import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.property.model.Property;
import com.zzy.pony.property.service.DepartmentService;
import com.zzy.pony.property.service.PropertyAddService;
import com.zzy.pony.property.service.PropertyTypeService;
import com.zzy.pony.property.vo.PropertyVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.DateTimeUtil;




@Controller
@RequestMapping(value = "/property/add")
public class PropertyAddController {
	static Logger log=LoggerFactory.getLogger(PropertyAddController.class);
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private PropertyTypeService propertyTypeService;
	@Autowired
	private PropertyAddService propertyAddService;
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/add/main";
	}
	/*** 
	* <p>Description: </p>
	* @author  WANGCHAO262
	* @date  2017年5月10日 下午5:20:20
	*/
	@ResponseBody
	@RequestMapping(value="addPropertys",method=RequestMethod.POST)
    public void addPropertys(@RequestBody PropertyVo vo){
    	//先查出当天的编码最大值
		int maxCode = propertyAddService.maxCode(DateTimeUtil.dateToStr(new Date(), DateTimeUtil.FORMAL_SHORT_FORMAT));
		
		String loginName =   ShiroUtil.getLoginUser().getLoginName();
		for (int i = 1; i <= vo.getNumber(); i++) {
			Property property = new Property();
			property.setBrand(vo.getBrand());
			property.setBuyDate(vo.getBuyDate());
			property.setComments(vo.getComments());
			property.setCreateTime(new Date());
			property.setCreateUser(loginName);
			property.setDepartment(departmentService.get(vo.getDepartment()));
			property.setDescription(vo.getDescription());
			property.setLocation(vo.getLocation());
			property.setName(vo.getName());
			property.setOwner(userService.findByTeacherId(vo.getOwner()));
			property.setPrice(vo.getPrice());
			property.setProductDate(vo.getProductDate());
			property.setPropertyType(propertyTypeService.get(vo.getPropertyType()));
			property.setSpec(vo.getSpec());
			property.setStatus(vo.getStatus());
			property.setUpdateTime(new Date());
			property.setUpdateUser(loginName);
			property.setUser(userService.findByTeacherId(vo.getUser()));
			//编码规则  年月日+序号 例如  201705100001
			String preffix =  DateTimeUtil.dateToStr(new Date(), DateTimeUtil.FORMAL_SHORT_FORMAT);
			String suffix =  String.format("%04d", maxCode+i);
			property.setPropCode(preffix+suffix);
			propertyAddService.add(property);			
		}
		
		
		
		
    }
}
