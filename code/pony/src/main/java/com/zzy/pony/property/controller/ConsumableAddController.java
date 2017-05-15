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

import com.zzy.pony.property.model.Consumable;
import com.zzy.pony.property.service.ConsumableAddService;
import com.zzy.pony.property.service.PropertyTypeService;
import com.zzy.pony.property.vo.ConsumableVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;




@Controller
@RequestMapping(value = "/property/addConsumable")
public class ConsumableAddController {
	static Logger log=LoggerFactory.getLogger(ConsumableAddController.class);
	
	@Autowired
	private PropertyTypeService propertyTypeService;
	@Autowired
	private UserService userService;
	@Autowired
	private ConsumableAddService consumableAddService;
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/addConsumable/main";
	}
	/*** 
	* <p>Description: </p>
	* @author  WANGCHAO262
	* @date  2017年5月10日 下午5:20:20
	*/
	@ResponseBody
	@RequestMapping(value="addConsumables",method=RequestMethod.POST)
    public void addConsumables(@RequestBody ConsumableVo vo){
    	
		
		String loginName =   ShiroUtil.getLoginUser().getLoginName();
		Consumable consumable = new Consumable();
		consumable.setAmount(vo.getAmount());
		consumable.setBrand(vo.getBrand());
		consumable.setComments(vo.getComments());
		consumable.setCreateTime(new Date());
		consumable.setCreateUser(loginName);
		consumable.setDescription(vo.getDescription());
		consumable.setLocation(vo.getLocation());
		consumable.setName(vo.getName());
		consumable.setPrice(vo.getPrice());
		consumable.setPropertyType(propertyTypeService.get(vo.getPropertyType()));
		consumable.setSpec(vo.getSpec());
		consumable.setUpdateTime(new Date());
		consumable.setUpdateUser(loginName);
		consumable.setUser(userService.findByTeacherId(vo.getOwner()));
		consumableAddService.add(consumable);
    }
}
