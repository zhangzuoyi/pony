package com.zzy.pony.property.controller;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.property.model.Consumable;
import com.zzy.pony.property.model.ConsumableTrace;
import com.zzy.pony.property.service.ConsumableAdminService;
import com.zzy.pony.property.service.ConsumableTraceService;
import com.zzy.pony.property.vo.ConsumableVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;




@Controller
@RequestMapping(value = "/property/consumableAdmin")
public class ConsumableAdminController {
	static Logger log=LoggerFactory.getLogger(ConsumableAdminController.class);
	
	@Autowired
	private ConsumableAdminService consumableAdminService;
	@Autowired
	private UserService userService;
	@Autowired
	private ConsumableTraceService consumableTraceService;
	
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/consumableAdmin/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<ConsumableVo> list(){	
		List<ConsumableVo> result = new ArrayList<ConsumableVo>();
		List<Consumable> consumables =  consumableAdminService.list();
		for (Consumable consumable : consumables) {
			ConsumableVo vo = new ConsumableVo();
			vo.setAmount(consumable.getAmount());
			vo.setPropertyTypeName(consumable.getPropertyType().getName());
			vo.setName(consumable.getName());
			vo.setSpec(consumable.getSpec());
			vo.setLocation(consumable.getLocation());
			vo.setCseId(consumable.getCseId());
			if(consumable.getUser() != null){
				vo.setOwnerName(userService.findUserNameById(consumable.getUser().getUserId()));		
			}
		result.add(vo);
		}
		
		return result;
		
	}
	@ResponseBody
	@RequestMapping(value="instock",method=RequestMethod.GET)
	public void instock(@RequestParam(value="selectConsumable") int cseId,@RequestParam(value="number") int number,@RequestParam(value="comments") String comments){
		Consumable consumable = consumableAdminService.get(cseId);
		consumable.setAmount(consumable.getAmount()+number);
		consumableAdminService.add(consumable);		
		ConsumableTrace consumableTrace = new ConsumableTrace();
		consumableTrace.setComments(comments);
		consumableTrace.setConsumable(consumable);
		consumableTrace.setNum(number);
		consumableTrace.setOperateTime(new Date());
		consumableTrace.setOperator(ShiroUtil.getLoginUser().getLoginName());
		consumableTrace.setOperation("入库");
		consumableTrace.setAmountAfter(consumable.getAmount()+number);
		consumableTraceService.add(consumableTrace);		
	}
	@ResponseBody
	@RequestMapping(value="outstock",method=RequestMethod.GET)
	public void outstock(@RequestParam(value="selectConsumable") int cseId,@RequestParam(value="number") int number,@RequestParam(value="comments") String comments){
		Consumable consumable = consumableAdminService.get(cseId);
		consumable.setAmount(consumable.getAmount()-number);
		consumableAdminService.add(consumable);		
		ConsumableTrace consumableTrace = new ConsumableTrace();
		consumableTrace.setComments(comments);
		consumableTrace.setConsumable(consumable);
		consumableTrace.setNum(number);
		consumableTrace.setOperateTime(new Date());
		consumableTrace.setOperator(ShiroUtil.getLoginUser().getLoginName());
		consumableTrace.setOperation("出库");
		consumableTrace.setAmountAfter(consumable.getAmount()-number);
		consumableTraceService.add(consumableTrace);		
	}
	
	
	
	
	
	
	
}
